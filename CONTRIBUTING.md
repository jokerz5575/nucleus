# Contributing to Clinvio Nucleus

## Code of Conduct

This project adheres to the [Contributor Covenant](CODE_OF_CONDUCT.md). By participating, you agree to uphold it.

## How to Contribute

### 1. Report Bugs

Open a [GitHub Issue](https://github.com/jokerz5575/nucleus/issues/new?template=bug_report.md) with:
- Framework version, Java version, OS
- Minimal reproduction steps
- Expected vs actual behavior
- Logs or stack traces (redacted of any sensitive data)

### 2. Suggest Features

Open a [Feature Request](https://github.com/jokerz5575/nucleus/issues/new?template=feature_request.md) describing:
- The problem you're solving
- Proposed API or behavior
- Alternative solutions considered

### 3. Submit Code Changes

#### Prerequisites
- Java 21+ (CI currently tests 21, 23, and 25)
- Maven 3.9+ (use `./mvnw`)

#### Setup
```bash
git clone https://github.com/jokerz5575/nucleus.git
cd nucleus
./mvnw install -N -DskipTests
./mvnw install -pl clinvio-nucleus-core,clinvio-nucleus-persistence -DskipTests
```

#### Make Changes
- Follow existing code style (no comments, match surrounding patterns)
- Add tests for new functionality
- Update documentation if changing public API

#### Run Tests
```bash
./mvnw test -pl <your-module>
```

If Mockito-based tests fail on newer JDKs, verify that the module includes
`src/test/resources/mockito-extensions/org.mockito.plugins.MockMaker` with
`mock-maker-subclass`. This avoids requiring runtime agent attachment for the
current test suite.

#### Submit a Pull Request
1. Fork the repository
2. Create a feature branch: `git checkout -b feat/my-change`
3. Commit with clear message: `feat(core): add support for X`
4. Push and open a PR against `main`
5. Ensure CI passes

### 4. Improve Documentation

Docs live in `docs/`. Submit PRs with doc improvements.

## Development Conventions

- **Annotations**: `@SensitiveData` + `@Convert(converter = ...)` — both required on encrypted fields
- **Modules**: One module per concern, independently testable
- **Testing**: Extend `CvBaseServiceTest` for service tests, `CvIntegrationTest` for controller tests
- **Commit style**: `type(scope): description` — types: feat, fix, docs, test, refactor, chore

## Questions?

Open a [Discussion](https://github.com/jokerz5575/nucleus/discussions).
