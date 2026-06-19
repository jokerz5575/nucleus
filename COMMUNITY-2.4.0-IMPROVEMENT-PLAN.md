# Clinvio Nucleus Community 2.4.0 - Improvement Plan

**Date:** 2026-06-19
**Status:** Draft
**Target:** v2.4.0 stabilization release

---

## Scope

This plan is based on the current Community repository state after the v2.3.0 action-plan work. It follows:

- `CONTRIBUTING.md`: Java 21+, Maven wrapper, module-per-concern, tests for new behavior, docs for public API changes, existing code style.
- `SECURITY.md`: coordinated vulnerability handling, protected encryption/JWT secrets, production HTTPS assumptions, AES-256-GCM/PBKDF2 disclosure.
- Existing module boundaries: core, components, themes, htmx, persistence, business, security, and spring-boot-starter.

The goal is to make the Community edition releasable, internally consistent, and safer for production adopters without expanding into paid-tier module scope.

---

## Current Baseline

### Architecture

The repository is an 8-module Maven reactor:

| Module | Responsibility |
|--------|----------------|
| `clinvio-nucleus-core` | Component contract, component registry, rendering, Thymeleaf dialect, events |
| `clinvio-nucleus-components` | Java component implementations and component templates |
| `clinvio-nucleus-themes` | Static Clinvio CSS theme resources |
| `clinvio-nucleus-htmx` | HTMX request filter, interceptor, response builder |
| `clinvio-nucleus-persistence` | Base entity, SQLite configuration, AES-256-GCM encryption |
| `clinvio-nucleus-business` | Base services, controllers, DTOs, workflow, notifications, utilities |
| `clinvio-nucleus-security` | Spring Security, JWT, CSRF, CORS, token blacklist, `@Secured` interceptor |
| `clinvio-nucleus-spring-boot-starter` | Auto-configuration and metadata aggregation |

### Implemented Since The v2.3.0 Plan

- Tests now exist across most modules.
- Previously missing components now have Java implementations: checkbox, toggle, searchable select, toast, navbar, sidebar.
- Core includes templates for all declared component types except `weekly-schedule`.
- Components includes `templates/cv/components/*` for a subset of component types.
- CI workflows exist for build, release, pages, and CodeQL.

### Observed Verification Result

Command run:

```bash
./mvnw test
```

Result:

- `clinvio-nucleus-core`: passed, 43 tests.
- `clinvio-nucleus-components`: passed, 99 tests.
- `clinvio-nucleus-themes`: no test sources.
- `clinvio-nucleus-htmx`: failed before assertions because Mockito inline Byte Buddy could not self-attach on local Java 25.
- Remaining reactor modules were skipped after HTMX failed.

Additional command run:

```bash
./mvnw test -pl clinvio-nucleus-persistence,clinvio-nucleus-business,clinvio-nucleus-security,clinvio-nucleus-spring-boot-starter
```

Result:

- `clinvio-nucleus-persistence`: passed, 17 tests.
- `clinvio-nucleus-business`: Mockito-based `CvBaseServiceTest` failed with the same Java 25 mock-maker initialization issue.
- `clinvio-nucleus-security` and starter were skipped after business failed.

The immediate build risk is test-toolchain compatibility, not failing business assertions.

---

## Priority Legend

| Tag | Meaning |
|-----|---------|
| P0 | Release blocker, correctness bug, or security-sensitive inconsistency |
| P1 | Stability or production hardening issue |
| P2 | Maintainability, test depth, documentation, or developer experience |
| P3 | Enhancement that improves quality but can miss the release |

---

## Phase 1 - P0 Release And Security Correctness

### 1.1 Fix the encryption annotation contract

**Affected:** `clinvio-nucleus-persistence`, `README.md`, `docs/`, `CONTRIBUTING.md`, `SPECIFICATION.md`

**Problem:** `CONTRIBUTING.md` and docs say encrypted fields require both `@SensitiveData` and explicit `@Convert(converter = EncryptedStringConverter.class)`. The current `SensitiveData` annotation embeds `@Convert` and its Javadoc says explicit `@Convert` is unnecessary.

**Risk:** Users may believe encryption is active when JPA ignores a converter declared only through a composed annotation. This is security-sensitive because plaintext persistence is possible if the documented warning is correct.

**Action:**

- Decide and document one supported contract.
- Prefer the safer contract already stated in `CONTRIBUTING.md`: require both annotations explicitly.
- Remove `@Convert` from `SensitiveData` if JPA meta-annotation conversion is not guaranteed.
- Update `SensitiveData` Javadoc to state that it marks fields for audits/documentation and must be paired with explicit `@Convert`.
- Add a persistence integration test that saves and reloads an entity with both annotations and verifies the raw DB value is ciphertext.
- Add a negative/documentation test or architecture check that flags `@SensitiveData` fields missing explicit `@Convert`.

**Acceptance Criteria:**

- Docs, Javadoc, examples, and `CONTRIBUTING.md` all show the same annotation pattern.
- A JPA-backed test proves ciphertext is stored in the database.
- Missing explicit converters are detectable before release.

### 1.2 Make JWT secret handling production-safe

**Affected:** `clinvio-nucleus-security`

**Problem:** `SecurityProperties.Jwt` defines a default JWT secret. `SECURITY.md` says JWT secrets should be protected and rotated regularly. A built-in default secret creates an easy accidental-production footgun.

**Action:**

- Mirror persistence behavior: allow a development default only outside `prod`/`production` profiles, or require explicit configuration whenever security is enabled.
- Fail startup in production if `clinvio.security.jwt.secret` is blank or still equal to the framework default.
- Validate minimum key length before constructing `CvJwtService`.
- Add tests for production profile failure, short secret failure, custom secret success, and development fallback behavior if retained.

**Acceptance Criteria:**

- Production cannot start with a framework-provided JWT signing secret.
- Error messages tell users exactly which property or environment variable to set.
- No secret value is logged.

### 1.3 Restore deterministic test execution across supported JDKs

**Affected:** all test modules using Mockito, parent `pom.xml`, `.github/workflows/build.yml`

**Problem:** The local Java 25 runtime fails Mockito inline mock-maker initialization before assertions. CI tests Java 21 and 23 only, while `CONTRIBUTING.md` says Java 21+.

**Action:**

- Pin and document the supported build JDK range, or make tests work on current JDKs.
- Prefer Java 21 as the release JDK and add Maven toolchain/enforcer rules if only 21/23 are supported.
- If Java 25 support is intended, configure Mockito/Byte Buddy explicitly or avoid inline mocking where plain mocks and Spring mocks are enough.
- Add a small `docs/development.md` section or update `CONTRIBUTING.md` with the expected JDK and known local setup.

**Acceptance Criteria:**

- `./mvnw test` passes on the documented local JDK.
- CI matrix matches the documented support promise.
- Mockito failures do not block non-Mockito modules unnecessarily.

### 1.4 Remove release workflow test bypass

**Affected:** `.github/workflows/release.yml`

**Problem:** Release builds run `verify -DskipTests` and deploy with `-DskipTests`.

**Action:**

- Run the full test suite before publishing release artifacts.
- Keep a separate packaging step only after tests pass.
- Remove `continue-on-error: true` from Maven Central publish unless there is a documented fallback release policy.

**Acceptance Criteria:**

- A tagged release cannot publish artifacts if tests fail.
- Release workflow output clearly separates verify, sign, publish, and GitHub release steps.

---

## Phase 2 - P1 Runtime And API Hardening

### 2.1 Clarify and simplify component template ownership

**Affected:** `clinvio-nucleus-core`, `clinvio-nucleus-components`, starter auto-configuration

**Problem:** Core contains `templates/components/*`, while components contains `templates/cv/components/*`. Component defaults point to `components/*`. The components auto-registrar scans `templates/cv/components/*` but only logs the files and does not register instances or metadata.

**Action:**

- Define one canonical classpath template location.
- Align `CvComponentType.getDefaultTemplate()`, starter `templatePrefix`, docs, and all template files.
- Remove duplicated or dead templates after migration.
- Either make `ComponentAutoRegistrar` perform useful registration or remove it.
- Add renderer tests that prove every `CvComponentType` with an implementation resolves to an actual template.

**Acceptance Criteria:**

- Each component type has one canonical template path.
- Rendering all implemented components does not return `cv-render-error`.
- There are no template-only or implementation-only components except explicitly planned ones.

### 2.2 Complete component coverage and tests

**Affected:** `clinvio-nucleus-components`, `clinvio-nucleus-core`

**Problem:** Java implementations exist for checkbox, toggle, searchable select, toast, navbar, and sidebar, but test coverage currently omits several of them. `CvComponentType.WEEKLY_SCHEDULE` exists without a visible implementation/template in components.

**Action:**

- Add unit tests for `CvCheckbox`, `CvToggle`, `CvSearchableSelect`, `CvToast`, `CvNavbar`, and `CvSidebar`.
- Decide whether `WEEKLY_SCHEDULE` belongs in Community 2.4.0.
- If not shipping, remove it from `CvComponentType` until implemented.
- If shipping, add Java implementation, template, CSS, and tests.

**Acceptance Criteria:**

- Every enum value corresponds to a tested implementation and template, or is explicitly excluded.
- README component count matches source reality.

### 2.3 Harden renderer error handling and escaping

**Affected:** `clinvio-nucleus-core`, component templates

**Problem:** `CvRenderer` returns exception messages inside HTML on render failure. That is useful in development but may disclose implementation details in production.

**Action:**

- Add a renderer property controlling detailed errors.
- Default production behavior to a generic render-error marker.
- Ensure any included error detail is HTML-escaped.
- Add tests for development and production behavior.

**Acceptance Criteria:**

- Production render failures do not expose stack details, template paths, or exception text to users.
- Logs retain enough detail for operators.

### 2.4 Strengthen persistence crypto behavior

**Affected:** `clinvio-nucleus-persistence`, `docs/key-management.md`

**Problem:** AES-GCM implementation uses a fixed default salt unless configured. This is documented as PBKDF2-based encryption, but key-management guidance is referenced by `SECURITY.md` and is not present in `docs/`.

**Action:**

- Add `docs/key-management.md`.
- Document passphrase, salt, rotation, backup, environment variable, and disaster recovery expectations.
- Consider requiring `clinvio.persistence.encryption-salt` in production.
- Add tests for short/tampered ciphertext, empty plaintext, wrong key, custom salt, and production missing salt behavior if enforced.

**Acceptance Criteria:**

- `SECURITY.md` links to an existing key-management guide.
- Production encryption configuration has explicit, test-covered failure modes.

### 2.5 Review security defaults for CORS and CSRF

**Affected:** `clinvio-nucleus-security`, starter metadata, docs

**Problem:** CORS defaults to `allowedOrigins = {"*"}`. CSRF is enabled, but behavior should be validated for HTMX and JWT combinations.

**Action:**

- Document the default CORS posture as development-friendly or change it to a safer deny-by-default production profile.
- Add tests for CORS with credentials on/off and wildcard origins.
- Add HTMX CSRF token propagation examples and tests around `CvCsrfController`.
- Ensure permit paths do not accidentally bypass sensitive endpoints.

**Acceptance Criteria:**

- Production CORS and CSRF behavior is explicit and tested.
- Docs explain the required HTTPS and cookie/header assumptions.

---

## Phase 3 - P2 Test Quality And Maintainability

### 3.1 Add integration tests at module boundaries

**Affected:** persistence, business, security, starter

**Action:**

- Add starter `ApplicationContextRunner` tests covering combinations of enabled/disabled modules.
- Add persistence JPA tests with an in-memory or temp SQLite database.
- Add security filter-chain tests for login, protected routes, refresh, logout, blacklist, and role checks.
- Add controller tests following the `CvIntegrationTest` convention described in `CONTRIBUTING.md`, or update the convention if no such base class exists.

**Acceptance Criteria:**

- New public behavior has unit or integration coverage.
- The documented test base classes either exist or the docs are corrected.

### 3.2 Introduce quality gates without heavy style churn

**Affected:** parent `pom.xml`, CI

**Action:**

- Add Maven Enforcer for Java/Maven versions and dependency convergence.
- Add reproducible build settings where possible.
- Add JaCoCo report generation first, then decide coverage thresholds after observing baseline.
- Add forbidden-log/secret checks for obvious secret literals outside tests and docs.
- Avoid broad formatting changes; keep style aligned with current code.

**Acceptance Criteria:**

- CI fails early for wrong Maven/JDK versions.
- Coverage is visible in artifacts.
- Quality gates do not cause unrelated formatting churn.

### 3.3 Clean stale and misleading repository artifacts

**Affected:** root docs and misc files

**Action:**

- Update `ROADMAP.md` to reflect work already completed in 2.3.0.
- Reconcile README claims such as test count and component count with actual source.
- Remove or archive `trash/SecurityAutoConfiguration.java` if it is not intentionally kept.
- Decide whether root `CSS/clinvio-theme.css`, `docs/css/clinvio-theme.css`, and `clinvio-nucleus-themes/src/main/resources/static/cv/css/clinvio-theme.css` should all exist.

**Acceptance Criteria:**

- Root docs do not contradict source.
- No stale implementation files remain in release artifacts or confuse contributors.

### 3.4 Document public extension points

**Affected:** `docs/`, README, starter metadata

**Action:**

- Document component creation patterns using existing `AbstractCvComponent` conventions.
- Document template naming, properties, and renderer expectations.
- Document HTMX response usage and request annotations.
- Document persistence encryption limitations, especially search/filter behavior on encrypted fields.

**Acceptance Criteria:**

- A contributor can add a component in one module without reverse-engineering unrelated modules.
- Public API changes include docs in the same PR, matching `CONTRIBUTING.md`.

---

## Phase 4 - P3 Product And Developer Experience

### 4.1 Add a minimal runnable sample application

**Affected:** new sample module or `docs/examples`

**Action:**

- Add a small Spring Boot app that uses the starter, one encrypted entity, one HTMX interaction, and one protected route.
- Keep it outside published Community artifacts unless intentionally shipped.

**Acceptance Criteria:**

- New users can run one command and see the core stack working.
- Sample uses secure placeholder configuration and does not include real secrets.

### 4.2 Improve UI component accessibility

**Affected:** component templates and CSS

**Action:**

- Add ARIA attributes for modal, tabs, toast, dropdown, and form fields.
- Add keyboard behavior expectations for tabs, dropdown, modal close, and searchable select.
- Add template-level tests where practical.

**Acceptance Criteria:**

- Core interactive components have documented accessibility behavior.
- Templates expose labels, roles, and states consistently.

### 4.3 Prepare native-image support intentionally

**Affected:** persistence native-image metadata, roadmap, CI

**Action:**

- Audit existing native-image metadata.
- Add a native-image smoke test only if the feature is in current Community scope.
- Otherwise mark current metadata as best-effort and avoid claiming full GraalVM support.

**Acceptance Criteria:**

- Docs and roadmap accurately state native-image status.
- Native metadata is either tested or clearly experimental.

---

## Cross-Cutting Standards For All Work

- Use `./mvnw`, not system `mvn`, in contributor-facing commands.
- Keep modules independently testable.
- Add tests with each behavior change.
- Update docs when public APIs, properties, examples, or security behavior change.
- Do not open public issues for security vulnerabilities; follow `SECURITY.md`.
- Do not log encryption keys, JWT secrets, plaintext sensitive values, or raw tokens.
- Keep examples free of production-looking secrets.
- Preserve existing code style and avoid unrelated refactors.

---

## Proposed Execution Order

1. P0 encryption annotation contract and tests.
2. P0 JWT production-secret validation.
3. P0 test-toolchain fix and CI/release workflow updates.
4. P1 template ownership and component coverage.
5. P1 renderer/security hardening.
6. P2 integration tests and docs reconciliation.
7. P3 sample app and accessibility improvements.

---

## Release Readiness Checklist

- [ ] `./mvnw test` passes on the documented local JDK.
- [ ] CI build matrix matches documented Java support.
- [ ] Release workflow runs tests before publishing.
- [ ] No production default JWT secret is accepted.
- [ ] Encryption annotation contract is consistent across code, docs, and tests.
- [ ] `docs/key-management.md` exists and is linked from `SECURITY.md`.
- [ ] Every shipped component has Java implementation, template, CSS, and tests.
- [ ] README, roadmap, docs site, and generated metadata agree on module/component/property names.
- [ ] CodeQL remains enabled for `main` and pull requests.
- [ ] Public PR template continues to require tests and docs for API changes.

