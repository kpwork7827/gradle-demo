# Jenkins CI/CD Pipeline

This project is ready for CI/CD with Jenkins, Docker, and Kubernetes.

## Files Added

```text
Jenkinsfile
Dockerfile
.dockerignore
k8s/namespace.yaml
k8s/configmap.yaml
k8s/secret.example.yaml
k8s/deployment.yaml
k8s/service.yaml
```

## Jenkins Server Requirements

Install these tools on the Jenkins agent:

- Java 17
- Docker CLI and Docker daemon access
- kubectl
- Git

Install these Jenkins plugins:

- Pipeline
- Git
- Credentials Binding
- JUnit
- Docker Pipeline, optional but useful

## Jenkins Credentials

Create these credentials in Jenkins:

### 1. Docker Registry Credentials

- Kind: Username with password
- ID: `docker-registry-creds`
- Username: your Docker registry username
- Password: your Docker registry password or token

### 2. Kubernetes Config

- Kind: Secret file
- ID: `kubeconfig-creds`
- File: kubeconfig file for your Kubernetes cluster

## Before Running Pipeline

Update these placeholders in `Jenkinsfile` and `k8s/deployment.yaml`:

```text
docker.io/YOUR_DOCKERHUB_USERNAME
```

Example:

```text
docker.io/mydockeruser
```

## Kubernetes SMTP Secret

Do not commit real SMTP credentials. Create the secret manually in Kubernetes.

```powershell
kubectl apply -f k8s/namespace.yaml
kubectl create secret generic gradle-demo-email-secret `
  --namespace gradle-demo `
  --from-literal=SMTP_USERNAME="your-email@gmail.com" `
  --from-literal=SMTP_PASSWORD="your-app-password" `
  --from-literal=MAIL_FROM="your-email@gmail.com"
```

For Gmail, use a Gmail App Password, not your normal Gmail password.

## Jenkins Job Setup

1. Open Jenkins.
2. Click `New Item`.
3. Select `Pipeline`.
4. Under `Pipeline`, choose `Pipeline script from SCM`.
5. SCM: Git.
6. Repository URL: your Git repository URL.
7. Script Path: `Jenkinsfile`.
8. Save and click `Build Now`.

## Pipeline Stages

1. Checkout source code.
2. Run tests and package the Spring Boot JAR.
3. Build Docker image.
4. Push Docker image to registry.
5. Deploy to Kubernetes.
6. Wait for Kubernetes rollout to finish.

## Local Docker Test

```powershell
cd D:\git\gradle\gradle-demo
docker build -t gradle-demo-email:local .
docker run --rm -p 8080:8080 `
  -e SMTP_HOST="smtp.gmail.com" `
  -e SMTP_PORT="587" `
  -e SMTP_USERNAME="your-email@gmail.com" `
  -e SMTP_PASSWORD="your-app-password" `
  -e MAIL_FROM="your-email@gmail.com" `
  gradle-demo-email:local
```

Swagger UI:

```text
http://localhost:8080/swagger-ui.html
```

Health check:

```text
http://localhost:8080/api/emails/health
```

## Kubernetes Verification

```powershell
kubectl get pods -n gradle-demo
kubectl get svc -n gradle-demo
kubectl rollout status deployment/gradle-demo-email -n gradle-demo
```

## Rollback

```powershell
kubectl rollout undo deployment/gradle-demo-email -n gradle-demo
```