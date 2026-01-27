# DevOps Final Project

**Repository:** https://github.com/AbdullohMaraimov/devops-final  
**Stack:** Java (Spring/Backend), Docker, Kubernetes, Helm, ArgoCD, GitHub Actions

---

## 🚀 Project Overview

This project demonstrates an end‑to‑end DevOps pipeline for a Java backend application.  
It includes:

- A **Java backend service** packaged with Maven (`pom.xml`) and built via Docker.
- **CI/CD workflows** using GitHub Actions.
- **Containerization** via Docker.
- **Kubernetes deployments** managed with **Helm charts** under `helm/demo/`.
- **ArgoCD manifests** for GitOps deployment in the `argocd/` directory.

---

## 📁 Repository Structure

```text
/
├─ .github/workflows/           # GitHub Actions CI/CD pipelines
├─ argocd/                      # ArgoCD manifests (Applications, Projects)
├─ helm/demo/                   # Helm chart for Kubernetes
├─ src/                         # Java source code
├─ Dockerfile                   # Docker image spec
├─ pom.xml                      # Maven build file for Java
├─ mvnw, mvnw.cmd               # Maven wrapper
├─ .gitignore
└─ README.md                    # This file
```

---

## 🧠 Architecture Overview

The system follows a simple but realistic DevOps architecture:

1. **Application Code** (`src/`)
   - Standard Maven project structure.
   - Exposes REST endpoints (Spring/Java typical approach).

2. **Build Process**
   - `mvn clean package` generates a runnable `.jar`.

3. **Containerization**
   - Docker builds a production‑ready container image.

4. **CI/CD Pipeline**
   - GitHub Actions triggers on push to `main`.
   - Runs build + tests.
   - Builds Docker image.
   - Pushes image to registry.

5. **Deployment (GitOps)**
   - Helm chart generates Kubernetes resources.
   - ArgoCD watches Git repo and syncs state into the cluster.

---

## 🛠 Local Development

### Prerequisites

Install the following:

- Java 11+ (or Java 17 recommended)
- Maven 3.6+
- Docker
- kubectl
- Helm
- Optional: minikube / k3d / kind (for local Kubernetes)

---

### Build & Run Locally (Maven)

```bash
./mvnw clean package
java -jar target/*.jar
```

---

### Build & Run Locally (Docker)

```bash
# Build image
docker build -t devops-final:local .

# Run container
docker run --rm -p 8080:8080 devops-final:local
```

Then open:

- http://localhost:8080

---

## 📦 Docker Image

### Build

```bash
docker build -t devops-final:latest .
```

### Push (Example: GitHub Container Registry)

```bash
REGISTRY=ghcr.io
IMAGE_NAME=$REGISTRY/<YOUR_USERNAME>/devops-final

docker tag devops-final:latest $IMAGE_NAME:latest
docker push $IMAGE_NAME:latest
```

Replace `<YOUR_USERNAME>` with your GitHub username.

---

## 🚀 Kubernetes Deployment with Helm

This repository contains a Helm chart in `helm/demo/`.

### Install (or Upgrade)

```bash
helm upgrade --install devops-final helm/demo   --namespace devops-final-system   --create-namespace   --set image.repository=ghcr.io/<YOUR_USERNAME>/devops-final   --set image.tag=latest
```

### Verify

```bash
kubectl get pods -n devops-final-system
kubectl get svc -n devops-final-system
```

---

## 📜 ArgoCD GitOps Deployment

The `argocd/` folder contains ArgoCD application manifests.

Typical flow:

1. Install ArgoCD into cluster
2. Apply project and application YAML
3. ArgoCD continuously syncs desired state from Git

### Apply Manifests

```bash
kubectl apply -f argocd/project.yaml
kubectl apply -f argocd/application.yaml
```

### Check ArgoCD Status

```bash
kubectl get applications -n argocd
```

---

## ⚙️ GitHub Actions CI/CD

Workflows live under `.github/workflows/`.

Typical pipeline responsibilities:

- Checkout repository
- Setup JDK
- Run tests
- Build JAR
- Build Docker image
- Push to registry

### Required Secrets (Example)

If using GitHub Container Registry:

| Secret Name | Purpose |
|------------|---------|
| `CR_PAT`   | Token with permissions to push images |

---

## 🧪 Testing

Run unit tests:

```bash
./mvnw test
```

---

## 🧩 Configuration & Environment Variables

You can configure the service using environment variables (example pattern):

| Variable | Purpose |
|---------|---------|
| `SPRING_PROFILES_ACTIVE` | Activate environment profile |
| `SERVER_PORT` | Change application port |
| `DATABASE_URL` | Database connection string |

> Actual variables depend on application code.

---

## 🛡 Security Notes

Good DevOps security practices used / recommended:

- Use minimal base images in Docker (slim or distroless).
- Run containers as non‑root where possible.
- Keep dependencies updated (`mvn versions:display-dependency-updates`).
- Scan images using Trivy or similar tools.
- Store credentials only in secrets (GitHub Secrets / Kubernetes Secrets).

---

## 📌 Summary

This repository demonstrates a complete DevOps lifecycle:

✔ Java backend (Maven)  
✔ Docker containerization  
✔ CI/CD automation (GitHub Actions)  
✔ Kubernetes deployment (Helm)  
✔ GitOps continuous delivery (ArgoCD)

---

