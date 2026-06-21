# Run Steps

## 1. Install Required Tools

Install these tools first:

- Java 17 or later
- Gradle 8 or later

Check installation:

```powershell
java -version
gradle --version
```

## 2. Configure SMTP

Example for Gmail SMTP:

```powershell
$env:SMTP_HOST="smtp.gmail.com"
$env:SMTP_PORT="587"
$env:SMTP_USERNAME="your-email@gmail.com"
$env:SMTP_PASSWORD="your-gmail-app-password"
$env:MAIL_FROM="your-email@gmail.com"
```

Important: for Gmail, create an App Password from your Google account security settings. Do not use your normal Gmail password.

## 3. Start Application

```powershell
cd D:\git\gradle\gradle-demo
gradle bootRun
```

## 4. Open Swagger UI

Open this URL in your browser:

```text
http://localhost:8080/swagger-ui.html
```

## 5. Send Email From Swagger

Use endpoint:

```text
POST /api/emails/send
```

Sample request body:

```json
{
  "to": "receiver@example.com",
  "cc": "",
  "subject": "Test email from Swagger UI",
  "body": "Hello, this email was sent from Spring Boot Swagger UI.",
  "html": false
}
```

## 6. Health Check

```text
GET /api/emails/health
```