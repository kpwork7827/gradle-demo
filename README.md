# Gradle Demo Email Service

Spring Boot + Gradle project for sending real emails through SMTP. Swagger UI is included so you can manage and test the email API from a browser.

## Requirements

- Java 17+
- Gradle 8+ or a generated Gradle wrapper
- SMTP account credentials

## Configure SMTP

Set these environment variables before running the app:

```powershell
$env:SMTP_HOST="smtp.gmail.com"
$env:SMTP_PORT="587"
$env:SMTP_USERNAME="your-email@gmail.com"
$env:SMTP_PASSWORD="your-app-password"
$env:MAIL_FROM="your-email@gmail.com"
```

For Gmail, use an App Password, not your normal Gmail password.

## Run

```powershell
cd D:\git\gradle\gradle-demo
gradle bootRun
```

If you add a Gradle wrapper later, use:

```powershell
.\gradlew bootRun
```

## Swagger UI

After the app starts, open:

```text
http://localhost:8080/swagger-ui.html
```

Use `POST /api/emails/send` with this sample body:

```json
{
  "to": "receiver@example.com",
  "cc": "",
  "subject": "Test email from Spring Boot",
  "body": "Hello, this email was sent from the Gradle demo project.",
  "html": false
}
```

## API Endpoints

- `GET /api/emails/health` - check service status
- `POST /api/emails/send` - send real email through SMTP
## CI/CD

This project includes a complete Jenkins, Docker, and Kubernetes pipeline.

Read the setup guide:

```text
docs/CICD-JENKINS.md
```

Pipeline/deployment files:

```text
Jenkinsfile
Dockerfile
.dockerignore
k8s/
```
