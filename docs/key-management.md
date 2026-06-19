# Clinvio Nucleus Key Management

This guide covers operational handling for keys used by the Community persistence and security modules.

## Encryption Key

`clinvio-nucleus-persistence` derives an AES-256-GCM key from the configured passphrase using PBKDF2 with 600,000 iterations. Configure the passphrase outside source control:

```properties
clinvio.persistence.encryption-key=${CLINVIO_ENCRYPTION_KEY}
```

Production deployments should provide `CLINVIO_ENCRYPTION_KEY` through a secret manager, orchestrator secret, or protected environment injection. Do not commit real keys to application properties, Docker images, CI logs, shell history, sample apps, or documentation.

## Salt

`clinvio.persistence.encryption-salt` changes the derived AES key. Treat it as deployment configuration that must be backed up with the encrypted database. Changing the salt without re-encrypting existing data makes old ciphertext undecryptable.

Use a unique salt per production deployment. Keep the salt stable for the lifetime of data encrypted with the matching key.

## Rotation

Key rotation requires a migration window:

1. Back up the database and current key material.
2. Start a trusted maintenance job with both old and new key configuration available.
3. Read each encrypted field with the old key.
4. Re-write each encrypted field with the new key and salt.
5. Verify a sample of reloaded entities and raw ciphertext.
6. Remove old key material from runtime configuration after verification.

Do not rotate keys by changing configuration only. Existing data must be re-encrypted.

## Backups And Recovery

Encrypted database backups are only useful if the matching key and salt can be restored. Store key material separately from database backups and restrict access to both. Test restore procedures before production use.

## JWT Secrets

`clinvio-nucleus-security` signs JWTs with `clinvio.security.jwt.secret`. Use a high-entropy secret of at least 32 characters, stored in the same protected secret-management system as the encryption key.

Rotate JWT secrets regularly. Rotation invalidates existing tokens unless the application implements a multi-key validation window.

## Incident Handling

If encryption keys, salts, JWT secrets, or plaintext sensitive values may have been exposed, follow `SECURITY.md`. Do not open a public issue for suspected credential or cryptographic compromise.
