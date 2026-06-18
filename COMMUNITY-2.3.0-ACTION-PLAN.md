# Clinvio Nucleus Community 2.3.0 — Action Plan

**Date:** 2026-06-18
**Status:** Approved
**Target:** v2.3.0 release

---

## Priority Legend

| Tag | Meaning |
|-----|---------|
| P0 | Bug — breaks correctness or blocks usage |
| P1 | Stability — runtime failure risk or hard gap |
| P2 | Quality — technical debt, hardening |
| P3 | Enhancement — new capability |

---

## Audit Overview (v2.2.0 baseline)

| Metric | Count |
|--------|-------|
| Java source files | 87 |
| Modules | 8 |
| CvComponentType enum values | 23 |
| Proper component implementations | 15 |
| Missing component implementations | 7 (SEARCHABLE_SELECT, CHECKBOX, TOGGLE, TOAST, NAVBAR, SIDEBAR, WEEKLY_SCHEDULE) |
| Utility-only "components" | 1 (CvBadge — static helper, not full component) |
| Test source files | **0** |
| Modules with test directory | **0** |
| Modules with test dependency | **1** (core — `spring-boot-starter-test` declared but unused) |
| Template files (Thymeleaf fragments) | **0** |
| CSS files | 1 (`CSS/clinvio-theme.css`, 82 KB) |
| README status | Minimal — no usage/docs |
| CONTRIBUTING status | Empty shell |
| Security endpoints (logout, refresh) | **0** |
| CORS configuration | **0** |
| CSRF support for HTMX | **0** |

---

## Phase 1: Test Coverage (P0)

### 1.1 Add test dependencies to all modules

**Affected:** `clinvio-nucleus-components`, `clinvio-nucleus-persistence`, `clinvio-nucleus-business`, `clinvio-nucleus-security`, `clinvio-nucleus-htmx`, `clinvio-nucleus-themes`, `clinvio-nucleus-spring-boot-starter`

**Action:** Add `spring-boot-starter-test` to each module's `pom.xml`. For persistence, also add H2 (in-memory DB for JPA tests).

### 1.2 Core module tests

**Files to create:** `clinvio-nucleus-core/src/test/java/hu/clinvio/ui/core/`

| Test class | What to test |
|---|---|
| `dialect/ClinvioDialectTest` | All 4 processors registered with correct precedences; dialect name |
| `component/CvComponentRegistryTest` | Registration, lookup by type, lookup by string, duplicate registration |
| `component/CvEventBusTest` | Publish/subscribe, unsubscribe, event delivery, exception isolation |
| `renderer/CvRendererTest` | Template resolution with/without prefix, context creation |
| `component/CvComponentTypeTest` | `fromType()` valid/throws, `values()` coverage, template paths |

### 1.3 Components module tests

**Files to create:** `clinvio-nucleus-components/src/test/java/hu/clinvio/ui/components/`

| Test class | What to test |
|---|---|
| `basic/CvButtonTest` | Builder/fluent API, render output, `getComponentType()`, null/edge cases |
| `basic/CvCardTest` | Builder, sections (header, body, footer), actions |
| `basic/CvAlertTest` | Builder, all severity levels, dismissible, icon customization |
| `basic/CvAvatarTest` | Initials extraction (normal, whitespace, single char, Unicode), `getComponentType()` |
| `basic/CvBadgeTest` | Static helper methods, all variants (pill, dot, icon) |
| `basic/CvBreadcrumbTest` | Builder, items, active state, `getComponentType()` |
| `basic/CvDropdownTest` | Builder, items, selection, `getComponentType()` |
| `basic/CvProgressTest` | Value, max, label, variant, `getComponentType()` |
| `basic/CvStatCardTest` | Builder, value/label/trend, `getComponentType()` |
| `basic/CvStepperTest` | Builder, steps, active/completed states, `getComponentType()` |
| `basic/CvTabsTest` | Builder, tabs, active tab, `getComponentType()` |
| `basic/CvTimelineTest` | Builder, items, alignment, `getComponentType()` |
| `form/CvTextFieldTest` | Builder, label, placeholder, error state, `getComponentType()` |
| `form/CvSelectTest` | Builder, options, multiple, `getComponentType()` |
| `data/CvDataTableTest` | Builder, columns, rows, sorting, `getComponentType()` |
| `overlay/CvModalTest` | Builder, open/close, size, `getComponentType()` |

### 1.4 Persistence module tests

**Files to create:** `clinvio-nucleus-persistence/src/test/java/hu/clinvio/ui/persistence/`

| Test class | What to test |
|---|---|
| `crypto/AesCryptoServiceTest` | Encrypt/decrypt roundtrip, null input, empty input, tampered ciphertext |
| `crypto/EncryptedStringConverterTest` | Convert to/from DB column, null handling, roundtrip |
| `entity/BaseEntityTest` | `@PrePersist`/`@PreUpdate` lifecycle callbacks, `equals()`/`hashCode()` contract |
| `config/SQLiteDialectTest` | Dialect registration, FK constraint dropping |

### 1.5 Business module tests

**Files to create:** `clinvio-nucleus-business/src/test/java/hu/clinvio/ui/business/`

| Test class | What to test |
|---|---|
| `service/CvBaseServiceTest` | CRUD with mocked repo, `findRecent()`, `countCreatedAfter()`, pagination |
| `dto/CvFormResultTest` | Success/error builders, `ok(T)` data storage, message formatting |
| `dto/CvPageResultTest` | Page/size/total calculation, empty page |
| `dto/CvSearchRequestTest` | Builder, default values |
| `util/CsvExportTest` | Builder, header extractors, write output, empty data, special characters |
| `util/PaginationTest` | Configured defaults, page size limits, edge cases |
| `notification/CvSimpleNotificationServiceTest` | CRUD notifications, eviction (if added), TTL |
| `workflow/WorkflowEngineTest` | State transitions (if `StatusAware` interface is implemented), invalid transitions |
| `document/CvDocumentStorageServiceTest` | Store/retrieve/delete with temp files, null InputStream |

### 1.6 Security module tests

**Files to create:** `clinvio-nucleus-security/src/test/java/hu/clinvio/ui/security/`

| Test class | What to test |
|---|---|
| `jwt/CvJwtServiceTest` | Token generation, validation, expiration, signature tampering |
| `config/CvPasswordEncoderTest` | Password encoding/verification, upgrade check |
| `filter/CvJwtAuthenticationFilterTest` | Valid/invalid/missing token, filter chain continuation |
| `filter/CvSecuredInterceptorTest` | PreHandle with/without annotation, role matching |

### 1.7 HTMX module tests

**Files to create:** `clinvio-nucleus-htmx/src/test/java/hu/clinvio/ui/htmx/`

| Test class | What to test |
|---|---|
| `response/HxResponseTest` | Builder, headers, triggers, trigger-detail, push URL, redirect |
| `filter/CvHxRequestInterceptorTest` | Allowed paths, HX-Request header enforcement |
| `filter/HxRequestFilterTest` | Servlet → HTTP cast, filter chain, mock request |

### 1.8 Starter module tests

**Files to create:** `clinvio-nucleus-spring-boot-starter/src/test/java/hu/clinvio/ui/autoconfigure/`

| Test class | What to test |
|---|---|
| `ClinvioAutoConfigurationTest` | Beans created, conditional properties, template prefix applied |
| `ClinvioPropertiesTest` | Default values, custom values via `@TestPropertySource` |

---

## Phase 2: Templates — Component Rendering (P0)

### 2.1 Create Thymeleaf fragment templates

**Affected:** `clinvio-nucleus-core/src/main/resources/templates/cv/components/`

The `CvRenderer` uses `templatePrefix + "/" + componentType.getDefaultTemplate()` to resolve templates. Currently `templatePrefix` defaults to `cv/components` but no template files exist. This means all component rendering silently produces empty output or Thymeleaf errors.

**Action:** Create Thymeleaf fragment templates for all 16 implemented component types:

| Template file | Component |
|---|---|
| `button.html` | CvButton |
| `text-field.html` | CvTextField |
| `select.html` | CvSelect |
| `card.html` | CvCard |
| `stat-card.html` | CvStatCard |
| `data-table.html` | CvDataTable |
| `alert.html` | CvAlert |
| `avatar.html` | CvAvatar |
| `modal.html` | CvModal |
| `tabs.html` | CvTabs |
| `breadcrumb.html` | CvBreadcrumb |
| `stepper.html` | CvStepper |
| `timeline.html` | CvTimeline |
| `dropdown.html` | CvDropdown |
| `progress-bar.html` | CvProgress |
| `badge.html` | CvBadge |

### 2.2 Template design convention

Each template is a Thymeleaf fragment using `cv:*` attribute processor namespace. Components pass a `CvComponent` context object with:
- `componentType` — enum value for CSS class/directive
- `properties` — `Map<String, Object>` of component attributes
- `content` — inner HTML/component tree

Template structure:
```html
<cv:component th:fragment="render(component)" th:attr="data-cv-component=${component.componentType.type}">
  <!-- component markup here -->
</cv:component>
```

### 2.3 CSS class mapping

Ensure CSS classes in Thymeleaf templates match the theme CSS (`CSS/clinvio-theme.css`). The theme uses `[data-cv-component="..."]` selectors and `.cv-{type}` classes. Audit the CSS file to verify all component types have corresponding styles. Add any missing CSS classes.

---

## Phase 3: Missing Component Implementations (P1)

### 3.1 CvCheckbox

**Module:** `clinvio-nucleus-components` → `form/`

A form checkbox component:
- Label + checkbox input
- Support checked/unchecked state
- Indeterminate state support
- Error state (matching CvTextField pattern)
- Disabled state
- Fluent builder API
- `getComponentType()` returns `CvComponentType.CHECKBOX.getType()`

### 3.2 CvToggle

**Module:** `clinvio-nucleus-components` → `form/`

A toggle/switch component (visual variant of checkbox):
- On/off state
- Label (left or right positioning)
- Disabled state
- Colored variants (primary, success, danger)
- Fluent builder API
- `getComponentType()` returns `CvComponentType.TOGGLE.getType()`

### 3.3 CvSearchableSelect

**Module:** `clinvio-nucleus-components` → `form/`

A select with search/filter capability:
- Extends or mirrors CvSelect API
- Search input filtering options
- Keyboard navigation support
- Fluent builder API
- `getComponentType()` returns `CvComponentType.SEARCHABLE_SELECT.getType()`

### 3.4 CvToast

**Module:** `clinvio-nucleus-components` → `overlay/`

A toast notification component:
- Severity levels (info, success, warning, error)
- Auto-dismiss with configurable duration
- Position (top-right, top-left, bottom-right, bottom-left)
- Dismiss button
- Stackable (multiple toasts)
- Fluent builder API
- `getComponentType()` returns `CvComponentType.TOAST.getType()`

### 3.5 CvBadge — Upgrade to full component

**Module:** `clinvio-nucleus-components` → `basic/`

Current `CvBadge` is a static utility class. Upgrade to a full component:
- Implement `CvComponent` interface
- Add `getComponentType()` returning `CvComponentType.BADGE.getType()`
- Keep existing static helper methods as convenience API
- Add fluent builder for programmatic creation

### 3.6 CvNavbar (lower priority)

**Module:** `clinvio-nucleus-components` → `basic/`

Simple navigation bar component:
- Brand/logo area
- Navigation links
- Optional user menu slot
- Responsive (mobile hamburger)
- Fluent builder API
- `getComponentType()` returns `CvComponentType.NAVBAR.getType()`

### 3.7 CvSidebar (lower priority)

**Module:** `clinvio-nucleus-components` → `basic/`

Side navigation panel:
- Navigation items with icons
- Collapsible
- Active state tracking
- Nested sub-menus
- Fluent builder API
- `getComponentType()` returns `CvComponentType.SIDEBAR.getType()`

### 3.8 CvWeeklySchedule (nice-to-have)

**Module:** `clinvio-nucleus-components` → `data/`

Weekly schedule/time grid component (domain-specific for scheduling apps):
- Days of week × time slots grid
- Appointment/event rendering
- Configurable time range
- Fluent builder API
- `getComponentType()` returns `CvComponentType.WEEKLY_SCHEDULE.getType()`

---

## Phase 4: Code Quality & Hardening (P1)

### 4.1 `CvComponentType.fromType()` → return `Optional`

**File:** `core/src/main/java/hu/clinvio/ui/core/component/CvComponentType.java`

Change `fromType(String)` to return `Optional<CvComponentType>` instead of throwing `IllegalArgumentException`. Update all callers:
- `CvComponentRegistry` — handle empty Optional gracefully
- `CvComponentAttributesProcessor` — handle empty Optional
- Any other direct callers

### 4.2 Add missing `@Override` annotations

**Scope:** All 87 Java source files

Search all classes implementing interfaces (`CvComponent`, `CvEventListener`, etc.) and add `@Override` where missing. Use compiler `-Xlint:all` to identify all locations.

### 4.3 Fix raw type usage

**Scope:** All 87 Java source files

Eliminate raw `List`, `Comparable`, `Map` usage:
- Replace `List` → `List<T>` (or specific type)
- Replace `Comparable` → `Comparable<T>`
- Replace `Map` → `Map<K, V>`
- Replace raw `Class` → `Class<T>`

Highest impact files:
- `CvComponentRegistry` — `register(Class)` should be `register(Class<? extends CvComponent>)`
- `CvEventBus` — `subscribe(Class, CvEventListener)` should be typed
- `WorkflowEngine` — entity state comparison via reflection should use generics

### 4.4 Break down long methods

**Scope:** Methods > 30 lines identified by exploration

Check and refactor:
- `CvRenderer.render()` — 80+ lines combining attribute processing, context building, and template resolution
- `CvDataTable` builder — likely > 40 lines
- `CvModal` — verify builder length
- `PersistenceAutoConfiguration` — multiple bean definitions in single method
- `WorkflowEngine.getCurrentStatus()` — fragile reflection, extract to typed interface (`StatusAware`)

### 4.5 Add `CvComponent` interface default methods

**File:** `core/src/main/java/hu/clinvio/ui/core/component/CvComponent.java`

Add default implementations for `getComponentType()` to reduce boilerplate in component classes. Consider:
```java
default String getComponentType() {
    return getClass().getSimpleName().toUpperCase();
}
```

Or define a `@ComponentType("name")` annotation and use reflection in the default method.

### 4.6 `BaseEntity` contract compliance

**File:** `persistence/src/main/java/hu/clinvio/ui/persistence/entity/BaseEntity.java`

- Change `setCreatedAt()` / `setUpdatedAt()` to `protected`
- Verify `@PrePersist` / `@PreUpdate` work correctly with auditing
- Add `@MappedSuperclass` documentation

### 4.7 CSS deduplication

**File:** `CSS/clinvio-theme.css` (82 KB)

Run a CSS linter to:
- Merge duplicate selectors
- Remove unused CSS variables
- Group related styles
- Add CSS custom properties for all colors (move from hardcoded hex values)

---

## Phase 5: Security Depth (P2)

### 5.1 Token refresh endpoint

**Module:** `clinvio-nucleus-security`

Add `POST /api/auth/refresh` endpoint:
- Accept refresh token (short-lived or one-time-use)
- Return new access token
- Invalidate old refresh token
- Configure token expiry via `clinvio.security.jwt.*` properties

### 5.2 Logout / token invalidation

**Module:** `clinvio-nucleus-security`

Add `POST /api/auth/logout` endpoint:
- Add token blacklist (in-memory `Set<String>` or Redis-backed)
- `CvJwtAuthenticationFilter` checks blacklist before validating token
- Optional: add `@ConditionalOnProperty` for blacklist backend selection

### 5.3 CORS configuration

**Module:** `clinvio-nucleus-spring-boot-starter` or `clinvio-nucleus-security`

Add `CorsConfigurationSource` bean in `ClinvioAutoConfiguration` (or dedicated config):
- Read allowed origins from `clinvio.ui.cors.allowed-origins` property
- Default to `*` or `http://localhost:8080` in development
- Configure allowed methods, headers, credentials
- `@ConditionalOnMissingBean` for user override

### 5.4 CSRF token support for HTMX

**Module:** `clinvio-nucleus-htmx`

- Add CSRF token to `HxResponse` headers
- Add `CvCsrfTokenProvider` interface with default implementation
- Add interceptor that validates CSRF for mutating requests (POST, PUT, DELETE, PATCH)
- Skip CSRF for `@HxRequest`-annotated endpoints (configurable)

### 5.5 Rate limiting (optional)

**Module:** `clinvio-nucleus-security`

- Add `CvRateLimitingFilter` with configurable:
  - Requests per minute per IP/user
  - Burst allowance
  - Response headers (`X-RateLimit-*`)
- `@ConditionalOnProperty("clinvio.security.rate-limiting.enabled")`

### 5.6 AccessDeniedException handler

**Module:** `clinvio-nucleus-business`

- In `CvBaseApiController` or global `@ControllerAdvice`:
  - Handle `AccessDeniedException` with 403 JSON response
  - Return HTMX-friendly error (triggers toast via `HX-Trigger`)
  - Include `X-Cv-Error` header for frontend handling

---

## Phase 6: Developer Experience (P2)

### 6.1 README.md — comprehensive rewrite

**File:** `README.md`

Current state: minimal. Rewrite to include:
- Project description and goals
- Quick start (add dependency, configure, first component)
- Module overview table
- Key features (configurable architecture, security, HTMX)
- Build instructions
- Links to: CONTRIBUTING, CHANGELOG, ROADMAP, API docs
- Badges: build status, Maven Central (when available), license

### 6.2 CONTRIBUTING.md — fill in

**File:** `CONTRIBUTING.md`

Current state: empty shell. Add:
- How to set up a dev environment
- Build and test commands
- Pull request process
- Coding conventions
- Commit message format
- Issue reporting guidelines

### 6.3 Sample application

**New:** `clinvio-nucleus-samples/` module (optional, not part of release)

A minimal Spring Boot app demonstrating:
- Component usage (Button, Card, DataTable, Modal, Form)
- HTMX integration
- Security (login, JWT)
- Thymeleaf template fragments
- Custom configuration

### 6.4 Add `@ConditionalOnMissingBean` guards

**Module:** `clinvio-nucleus-spring-boot-starter`

Files: `ClinvioAutoConfiguration.java`

Add `@ConditionalOnMissingBean` on all bean definitions so users can override:
- `HxRequestFilter`
- `CvComponentRegistry`
- `CvEventBus`
- `CvRenderer`
- `ClinvioDialect`
- `CvJwtService`
- `PasswordEncoder`

### 6.5 Configuration metadata

**Module:** `clinvio-nucleus-spring-boot-starter`

Create `src/main/resources/META-INF/additional-spring-configuration-metadata.json` with descriptions for all custom properties:
- `clinvio.ui.enabled`
- `clinvio.ui.components.template-prefix`
- `clinvio.ui.components.pagination.default-page-size`
- `clinvio.ui.components.pagination.max-page-size`
- `clinvio.ui.components.processors.button-precedence`
- `clinvio.ui.components.processors.event-precedence`
- `clinvio.ui.components.processors.attribute-precedence`
- `clinvio.ui.components.processors.component-precedence`
- `clinvio.security.jwt.secret`
- `clinvio.security.jwt.expiration-ms`

### 6.6 Spec compliance audit

**File:** `SPECIFICATION.md`

Cross-reference the specification against implemented features. Add `[x]` (done) / `[ ]` (todo) markers. Identify any unimplemented specification items.

---

## Phase 7: Templates Directory Restructuring (P2)

### 7.1 Move templates from classpath to filesystem option

**File:** `clinvio-nucleus-core/src/main/java/hu/clinvio/ui/core/renderer/CvRenderer.java`

Currently templates are resolved relative to classpath via Thymeleaf template resolver. Add support for filesystem-based templates (for developers who want to customize templates without rebuilding):
- If `clinvio.ui.components.template-location` starts with `file:`, use `FileTemplateResolver`
- Otherwise use default `ClassLoaderTemplateResolver`
- Document in README

### 7.2 Add template cache control

Add `clinvio.ui.components.template-cache` property (default `true` in production, `false` in dev):
- Set Thymeleaf template resolver cache TTL
- Add dev-mode profile support

---

## Phase 8: Build & DevOps (P3)

### 8.1 Add test execution to CI

**File:** `.github/workflows/`

- Update `release.yml` or create `ci.yml` to run `mvn test` on PR and push to main
- Add JaCoCo for code coverage reporting
- Add Checkstyle or SpotBugs for static analysis
- Fail build on test failures

### 8.2 Add test to Maven lifecycle

**File:** `community/pom.xml`

Ensure test phase is bound to `mvn verify` (default is correct if `maven-surefire-plugin` is configured). Add:
- `maven-surefire-plugin` configuration (fork count, memory settings)
- `maven-failsafe-plugin` for integration tests (if any)

### 8.3 JaCoCo coverage configuration

Add JaCoCo plugin to parent POM with:
- Minimum coverage rules (optional, informational for v2.3.0)
- HTML/XML report generation
- Exclude generated code (if any)

---

## Execution Order

```
Phase 1 (Tests)       → P0 — Highest priority, foundational
Phase 2 (Templates)   → P0 — Components are broken without templates
Phase 3 (Components)  → P1 — Missing basic form controls
Phase 4 (Hardening)   → P1 — Type safety, @Override, method length
Phase 5 (Security)    → P2 — Depth features, not blocking
Phase 6 (DevX)        → P2 — Docs, samples, metadata
Phase 7 (Templates)   → P2 — Advanced template features
Phase 8 (DevOps)      → P3 — CI improvements
```

### Release criteria

All Phase 1 + Phase 2 items must be complete before `v2.3.0` tag:
- [ ] Tests exist for all 8 modules with minimum 60% line coverage
- [ ] All component templates exist and produce valid HTML
- [ ] Template renderer tests pass
- [ ] CI runs tests automatically
- [ ] No regressions in existing functionality

### Stretch goals (if time permits)
- [ ] Phase 3 (Checkbox, Toggle, Toast)
- [ ] Phase 4 (fromType → Optional, @Override audit)
- [ ] Phase 5 (logout, CORS)
- [ ] Phase 6 (README rewrite, sample app)
