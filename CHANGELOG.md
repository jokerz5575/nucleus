# Changelog

## [2.0.0-SNAPSHOT] - 2026-06-18

### Added
- 34 modules: core(3), infrastructure(16), domain(9), tools(4), starter(1), parent(1)
- PostgreSQL module (`clinvio-nucleus-postgresql`)
- Redis module (`clinvio-nucleus-redis`)
- S3 storage module (`clinvio-nucleus-s3`)
- Search module (`clinvio-nucleus-search`)
- CLI tool (`clinvio-cli`) for project scaffolding
- Maven plugin (`clinvio-maven-plugin`) for code generation
- Test utilities (`clinvio-nucleus-test`) with `CvBaseServiceTest`, `CvIntegrationTest`
- Spring Security integration with JWT authentication
- Tiered package distribution: Community (MIT), Professional, Enterprise
- Stripe payment integration with automated account provisioning
- Email templates for Community (green), Professional (teal), Enterprise (dark blue)
- HTMX-native UI components with partial page updates
- Field-level AES-256-GCM encryption via `@SensitiveData` + `@Convert`

### Fixed
- RateLimitFilter bean conflict (added `@Qualifier`/`@ConditionalOnMissingBean`)
- `CvNotificationService` name collision
- Conditional bean wiring for email, S3, and mail services
- `PersistenceAutoConfiguration` added `@EntityScan`/`@EnableJpaRepositories`/`@ComponentScan`

## [1.1.0] - 2026-06-15

### Added
- Initial public release with 23 modules
- SQLite persistence with field-level encryption
- HTMX integration with Clinvio UI components
- Workflow engine, scheduler, email, WebSocket modules
- Compliance/GDPR module with consent tracking
- Domain modules: medical, logistics, cloud, project management
- AI module for LLM integration
- Multi-tenancy support with tenant header isolation
- OpenAPI/Swagger documentation generation
- Observability with health checks and request logging

### Known Limitations
- SQLite: ~50 concurrent write ceiling
- No native image support
- No React/Vue SDK
