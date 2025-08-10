# Security Configuration

## API Key Management

This application uses the Groq API and requires proper security configuration for the API key.

### Environment Variables

The application now uses environment variables for sensitive configuration:

- `GROQ_API_KEY`: Your Groq API key for form generation services

### Local Development Setup

1. Copy `.env.example` to `.env`:
   ```bash
   cp .env.example .env
   ```

2. Edit `.env` and add your actual Groq API key:
   ```
   GROQ_API_KEY=your_actual_api_key_here
   ```

3. The `.env` file is automatically ignored by Git for security.

### Production Deployment

For production environments, set the environment variable directly:

**Docker:**
```bash
docker run -e GROQ_API_KEY=your_api_key your-image
```

**Docker Compose:**
```yaml
environment:
  - GROQ_API_KEY=${GROQ_API_KEY}
```

**System Environment:**
```bash
export GROQ_API_KEY=your_api_key
```

**Kubernetes:**
```yaml
apiVersion: v1
kind: Secret
metadata:
  name: groq-secret
type: Opaque
data:
  groq-api-key: <base64-encoded-key>
```

### Security Best Practices

1. **Never commit API keys** to version control
2. **Use different keys** for development, staging, and production
3. **Rotate keys regularly** 
4. **Monitor API usage** for unusual activity
5. **Use least-privilege access** when possible

### Files to Keep Secure

- `.env` - Contains actual API keys (git ignored)
- Any backup or config files with sensitive data

### Files Safe to Commit

- `.env.example` - Template file without actual keys
- `application.properties` - Uses environment variable placeholders
- `SECURITY.md` - This documentation file