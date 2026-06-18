# Security Policy

## Supported Versions

| Version | Supported          |
|---------|--------------------|
| 2.0.x   | ✅ Active development |
| < 2.0   | ❌ Not supported    |

## Reporting a Vulnerability

We take security seriously. If you discover a vulnerability in Clinvio Nucleus:

1. **Do not** open a public GitHub issue
2. Email **security@clinvio.hu** with:
   - Description of the vulnerability
   - Steps to reproduce
   - Affected modules and versions
   - Any proof-of-concept code (if available)

You should receive a response within 48 hours. If you don't, follow up.

## Disclosure Timeline

- **48h**: Acknowledgment of receipt
- **7 days**: Initial assessment and mitigation plan
- **30 days**: Patch released (or extension communicated)
- **90 days**: Public disclosure (coordinated)

We follow coordinated disclosure: we will not release details until a fix is
available and users have had reasonable time to update.

## Encryption Disclosure

Nucleus uses AES-256-GCM (NIST SP 800-38D) with PBKDF2 key derivation
(600K iterations per OWASP 2023). Key management is the responsibility of
the deploying organization. See `docs/key-management.md` for guidance.

## Known Security Considerations

- Encryption keys are configured via environment variable or config — protect access
- JWT secrets should be rotated regularly
- Session tokens are transmitted via cookies or auth headers — use HTTPS in production
- SQLite is not recommended for multi-process deployments
