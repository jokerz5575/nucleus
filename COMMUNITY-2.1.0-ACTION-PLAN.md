# Clinvio Nucleus Community 2.1.0 — Action Plan

**Date:** 2026-06-18
**Status:** Draft
**Target:** v2.1.0 release to Maven Central

---

## Priority Legend

| Tag | Meaning |
|-----|---------|
| P0 | Bug — breaks correctness |
| P1 | Stability — can cause runtime failure |
| P2 | Quality — technical debt, hardening |
| P3 | Enhancement — new capability |

---

## Phase 1: Bug Fixes (P0)

### 1.1 CSV Export broken — `CsvExport.Builder.write()` ignores column extractors

**Module:** `clinvio-nucleus-business`
**File:** `util/CsvExport.java`
**Problem:** `Builder.write()` calls `item.toString()` instead of applying the `Function<T, String>` extractors registered via `addHeader()`. The method signature accepts `Function<T, String>` but never stores them. CSV output is garbage.
**Fix:** Store extractors in a `List<Function<T, String>>` and apply them in `write()`.

### 1.2 `CvBaseService.findRecent()` uses wrong `PageRequest` parameters

**Module:** `clinvio-nucleus-business`
**File:** `service/CvBaseService.java`
**Problem:** `PageRequest.of(limit, 1, Sort.by(...))` — first param is page index, second is page size. Creates page size 1 at page `limit`. Should be `PageRequest.of(0, limit, Sort.by(...))`.
**Fix:** Swap arguments.

### 1.3 `CvDropdown` references undefined `CvComponentType.DROPDOWN`

**Module:** `clinvio-nucleus-components`
**File:** `basic/CvDropdown.java`, `core/component/CvComponentType.java`
**Problem:** `CvDropdown.getComponentType()` returns `"DROPDOWN"` but `CvComponentType` enum has no `DROPDOWN` entry. Template path is hardcoded instead of using `CvComponentType.DROPDOWN.getDefaultTemplate()`.
**Fix:** Add `DROPDOWN` to `CvComponentType` enum; update `CvDropdown` to use the enum.

### 1.4 `CvAvatar.getInitials()` throws on whitespace-only name

**Module:** `clinvio-nucleus-components`
**File:** `basic/CvAvatar.java`
**Problem:** If `name` is whitespace-only (e.g., `"   "`), `trim().split("\\s+")` returns `[""]`, then `parts[0].substring(0, 1)` throws `StringIndexOutOfBoundsException`.
**Fix:** Guard with `name.isBlank()` check and return fallback (e.g., `"?"`).

### 1.5 `CvFormResult.ok(String, T)` silently ignores data parameter

**Module:** `clinvio-nucleus-business`
**File:** `dto/CvFormResult.java`
**Problem:** Method accepts `<T> T data` but never assigns it to any field. Callers believe data is included in the response.
**Fix:** Add a `data` field to `CvFormResult` and store it.

### 1.6 `PersistenceAutoConfiguration` references non-existent base package

**Module:** `clinvio-nucleus-persistence`
**File:** `autoconfigure/PersistenceAutoConfiguration.java`
**Problem:** `@EnableJpaRepositories`, `@ComponentScan`, `@EntityScan` include `"hu.clinvio.nucleus"` which does not exist. Only `"hu.clinvio.ui"` is valid.
**Fix:** Remove `"hu.clinvio.nucleus"`.

### 1.7 `CvTextField` inconsistent fluent vs setter API

**Module:** `clinvio-nucleus-components`
**File:** `form/CvTextField.java`
**Problem:** `errorMessage(String)` sets state to ERROR, but `setErrorMessage(String)` does not.
**Fix:** Make `setErrorMessage()` also set the error state, or add JavaDoc clarifying the behavioral difference.

---

## Phase 2: Stability & Reliability (P1)

### 2.1 Zero test coverage — all 8 modules

All modules lack `src/test/` directories entirely. This is the single biggest risk.
- **core** — Test `ClinvioDialect` processor registration, `CvEventBus` dispatch, `CvComponentRegistry` registration/lookup, `CvRenderer` rendering, `CvButtonAttrProcessor` attribute processing
- **components** — Test each component's `render()` output, fluent builder methods, template path resolution, null/edge inputs
- **persistence** — Test AES-GCM encrypt/decrypt roundtrip, `EncryptedStringConverter` with nulls, `BaseEntity` lifecycle callbacks, `SQLiteDialect` Hibernate registration
- **business** — Test `CvBaseService` CRUD (with mocked repo), `CvBaseApiController` endpoints, `WorkflowEngine` state transitions, `CsvExport` output, `TestDataFactory` utilities, `Pagination` edge cases
- **security** — Test `CvJwtService` token generation/validation/expiration, `PasswordEncoder` creation, `CvUserDetails` construction
- **starter** — Test auto-configuration creates all expected beans with `@SpringBootTest`, verify `HxRequestFilter` registration
- **htmx** — Test `HxResponse.Builder`, `HxRequestFilter` with mock HTTP request
- **themes** — (placeholder, no tests needed until content added)

### 2.2 `CvDocumentStorageService.store(InputStream, ...)` resource leak

**Module:** `clinvio-nucleus-business`
**File:** `document/CvDocumentStorageService.java`
**Problem:** `Files.copy(inputStream, targetFile, REPLACE_EXISTING)` without try-finally. If copy throws, the InputStream is leaked.
**Fix:** Wrap in try-with-resources.

### 2.3 `CvSimpleNotificationService` in-memory leak (no eviction)

**Module:** `clinvio-nucleus-business`
**File:** `notification/CvSimpleNotificationService.java`
**Problem:** Notifications accumulate in `ConcurrentHashMap` forever. Long-running apps will OOM.
**Fix:** Add eviction (size-based or TTL-based), or document as short-lived.

### 2.4 Two security auto-configuration classes with conflicting property prefixes

**Module:** `clinvio-nucleus-security`
**Files:** `config/CvSecurityAutoConfiguration.java`, `config/SecurityAutoConfiguration.java`
**Problem:** Two auto-config classes; one watches `clinvio.nucleus.security.*`, the other watches `clinvio.security.*`. `SecurityProperties` uses `clinvio.security.*` prefix. Only one is registered in `AutoConfiguration.imports`. Potential bean conflicts.
**Fix:** Consolidate into a single `SecurityAutoConfiguration`; remove `CvSecurityAutoConfiguration`. Ensure property prefix is consistent.

### 2.5 `EncryptedStringConverter` static volatile anti-pattern

**Module:** `clinvio-nucleus-persistence`
**File:** `crypto/EncryptedStringConverter.java`
**Problem:** `static volatile AesCryptoService cryptoService` — global mutable state. In multi-tenant or context-refresh scenarios, the service pointer can change mid-operation.
**Fix:** Use `@Configurable` + Spring bean injection, or thread-local, or redesign to avoid static state.

### 2.6 `HxRequestFilter` unchecked `ServletRequest` → `HttpServletRequest` cast

**Module:** `clinvio-nucleus-htmx`
**File:** `filter/HxRequestFilter.java`
**Problem:** Direct cast without `instanceof` check. Non-HTTP requests (unlikely in Spring Boot but possible in test/mock) cause `ClassCastException`.
**Fix:** Add `instanceof` guard.

### 2.7 `ConnectionPoolProperties` is dead code

**Module:** `clinvio-nucleus-persistence`
**File:** `autoconfigure/ConnectionPoolProperties.java`
**Problem:** `@ConfigurationProperties` class is defined but never activated via `@EnableConfigurationProperties` and never referenced by any bean.
**Fix:** Either wire it into `PersistenceAutoConfiguration` to configure HikariCP, or remove the class.

### 2.8 `PersistenceProperties.ddlAuto` stored but never applied

**Module:** `clinvio-nucleus-persistence`
**File:** `autoconfigure/PersistenceProperties.java`, `PersistenceAutoConfiguration.java`
**Problem:** `ddlAuto` property is read from config but never set on `spring.jpa.hibernate.ddl-auto`.
**Fix:** Apply the property to the `EntityManagerFactory` or document as reserved for future use.

---

## Phase 3: Code Quality & Hardening (P2)

### 3.1 Missing `equals()`/`hashCode()` on entity/DTO classes

**Affected:**
- `BaseEntity` — critical for JPA collections
- `CvDocument` (inherits from BaseEntity)
- `CvFormResult`, `CvSearchRequest`, `CvPageResult`, `CvReportField`, `CvValidationResult`
- All inner model classes: `CvCard.CardAction`, `CvTabs.Tab`, `CvBreadcrumb.BreadcrumbItem`, `CvDropdown.DropdownItem`, `CvStepper.Step`, `CvTimeline.TimelineItem`, `CvDataTable.Column`, `CvSelect.Option`

**Approach:** Add `equals()`/`hashCode()` based on business key (for entities) or all fields (for DTOs/value objects).

### 3.2 `BaseEntity.setCreatedAt()` / `setUpdatedAt()` should be protected

**Module:** `clinvio-nucleus-persistence`
**Problem:** Public setters allow callers to corrupt the audit trail.
**Fix:** Change to `protected` access.

### 3.3 Missing `@Valid` / `@Validated` on controller request bodies

**Module:** `clinvio-nucleus-business`
**Files:** `controller/CvBaseApiController.java`, `CvBaseCrudController.java`
**Problem:** No `@Valid` on `@RequestBody` parameters — Bean Validation annotations on entity fields are ignored.
**Fix:** Add `@Valid @RequestBody` in both controllers.

### 3.4 `@Secured` annotation has no processor

**Module:** `clinvio-nucleus-security`
**Problem:** `@Secured` is defined but never checked by any AOP advice, security interceptor, or method security. Purely decorative.
**Fix:** Either implement a MethodSecurityInterceptor/`SecurityAspect`, or add JavaDoc clarifying it's reserved for future use.

### 3.5 `@HxRequest` annotation has no processor

**Module:** `clinvio-nucleus-htmx`
**Problem:** Same as above — defined but unused. Meant to annotate controller methods that handle HTMX requests.
**Fix:** Either implement a `HandlerInterceptor` that checks for the annotation, or document as reserved.

### 3.6 `ClinvioAutoConfiguration` ignores `clinvio.ui.enabled` property

**Module:** `clinvio-nucleus-spring-boot-starter`
**Problem:** `ClinvioProperties.enabled` is stored but never checked. Setting `clinvio.ui.enabled=false` has no effect.
**Fix:** Add `@ConditionalOnProperty(prefix = "clinvio.ui", name = "enabled", matchIfMissing = true)`.

### 3.7 `HxRequestFilter` registration is unconditional

**Module:** `clinvio-nucleus-spring-boot-starter`
**File:** `ClinvioAutoConfiguration.java`
**Problem:** Filter registered on `/*` for all requests. No `@ConditionalOnProperty` to disable.
**Fix:** Add `@ConditionalOnProperty(prefix = "clinvio.ui.htmx", name = "enabled", matchIfMissing = true)`.

### 3.8 `HxResponse.Builder.triggerDetail()` prematurely calls `build()` pattern

**Module:** `clinvio-nucleus-htmx`
**File:** `response/HxResponse.java`
**Problem:** `trigger()` and `triggerDetail()` are overloaded; one variant auto-sets triggerDetail. No validation that `trigger()` + `triggerDetail()` calls don't conflict.
**Fix:** Add validation in `build()` that checks for conflicting trigger configuration.

### 3.9 Hardcoded AES salt reduces security

**Module:** `clinvio-nucleus-persistence`
**File:** `crypto/AesCryptoService.java`
**Problem:** Salt is a compile-time constant (`"clinvio-ui-persistence-salt-v1"`). Should be configurable or randomly generated per encryption.
**Fix:** Add `aes.salt` property; regenerate salt on key rotation.

### 3.10 `WorkflowEngine.getCurrentStatus()` uses fragile reflection

**Module:** `clinvio-nucleus-business`
**File:** `workflow/WorkflowEngine.java`
**Problem:** Assumes entity has `getStatus()` method via reflection. Runtime failure if not found.
**Fix:** Define a `StatusAware` interface with `getStatus()` and use that instead of reflection.

---

## Phase 4: Enhancements (P3)

### 4.1 `clinvio-nucleus-themes` is an empty shell

**Problem:** Module has zero source files, zero CSS, zero templates.
**Proposal:** Either populate with a CSS reset/baseline theme (e.g., `clinvio-base.css`), a `ThemeResolver`, and optional Bootstrap overrides — or deprecate and remove from reactor.

### 4.2 Missing `SecurityFilterChain` bean

**Module:** `clinvio-nucleus-security`
**Proposal:** Add a default `SecurityFilterChain` that:
- Permits `/login`, `/css/**`, `/js/**`, `/webjars/**`, `/api/public/**`
- Requires authentication for `/api/**` and `/app/**`
- Configures stateless JWT session
- Adds `CvJwtAuthenticationFilter` (new class)

### 4.3 Template paths not configurable

**Affects:** All components in `clinvio-nucleus-components`
**Proposal:** Add `clinvio.ui.components.template-prefix` property (default `"cv/components"`) used by all component classes.

### 4.4 `Pagination.DEFAULT_PAGE_SIZE` and `MAX_PAGE_SIZE` not configurable

**Module:** `clinvio-nucleus-business`
**Proposal:** Make these Spring `@Value` properties or read from `ClinvioProperties`.

### 4.5 Processor precedence values hardcoded

**Module:** `clinvio-nucleus-core`
**Files:** `dialect/processor/*.java`
**Proposal:** Add `clinvio.ui.dialect.*-precedence` properties for each processor.

### 4.6 Missing `@ConditionalOnMissingBean` on key beans

**Affects:** `ClinvioAutoConfiguration`, `PersistenceAutoConfiguration`, `SecurityAutoConfiguration`
**Proposal:** Add `@ConditionalOnMissingBean` on `HxRequestFilter`, `CvComponentRegistry`, `CvEventBus`, `CvRenderer`, `ClinvioDialect`, `AesCryptoService` so users can override.

### 4.7 `spring.factories` is legacy — migrate to `AutoConfiguration.imports` only

**Module:** `clinvio-nucleus-spring-boot-starter`
**Proposal:** Spring Boot 3.x prefers `AutoConfiguration.imports`. Remove `spring.factories`.

### 4.8 `CvBaseService.countCreatedAfter()` loads all entities in memory

**Module:** `clinvio-nucleus-business`
**Problem:** `findAll().stream().filter(...).count()` loads entire table.
**Proposal:** Add a `@Query("SELECT COUNT(e) FROM #{#entityName} e WHERE e.createdAt > :since")` method to `CvBaseRepository`.

---

## Phase 5: Maven Central Publishing Setup

### 5.1 POM additions
- Add `developers` section
- Configure `maven-source-plugin` (attach sources)
- Configure `maven-javadoc-plugin` (attach javadoc)
- Configure `maven-gpg-plugin` (sign artifacts)
- Configure `central-publishing-maven-plugin` (Sonatype Central Portal)
- Add `parent.relativePath` corrections for all child POMs

### 5.2 Release workflow update
- Update `.github/workflows/release.yml` to publish to Maven Central instead of (or in addition to) GitHub Packages
- Add GPG signing step with secrets
- Add Sonatype token secrets

---

## Execution Order

```
Phase 1 (Bugs)       → IMMEDIATE — ship as 2.0.1 hotfix
Phase 2 (Stability)  → v2.1.0 milestone 1
Phase 3 (Quality)    → v2.1.0 milestone 2
Phase 4 (Enhance)    → v2.2.0
Phase 5 (Publishing) → v2.1.0 final
```

All Phase 1 items should be fixed before any community release.
