# Script de despliegue automático para RCJ Hackaton

Write-Host "=== Desplegando aplicación RCJ Hackaton ===" -ForegroundColor Green

# 0. Verificar que Kubernetes esté corriendo
Write-Host "Verificando que Kubernetes esté corriendo..." -ForegroundColor Yellow
try {
    kubectl cluster-info > $null 2>&1
    if ($LASTEXITCODE -ne 0) {
        throw "Kubernetes no está corriendo"
    }
} catch {
    Write-Host "ERROR: Kubernetes no está corriendo. Asegúrate de que Docker Desktop tenga Kubernetes habilitado." -ForegroundColor Red
    exit 1
}

# 1. Hacer pull de las imágenes Docker desde DockerHub
Write-Host "Descargando imágenes Docker desde DockerHub..." -ForegroundColor Yellow
docker pull fabrizioleonardoramirez/hct-vehiculo:latest
docker pull fabrizioleonardoramirez/hct-cliente:latest
docker pull fabrizioleonardoramirez/hct-alquiler:latest
docker pull fabrizioleonardoramirez/hct-frontend:latest

# 2. Crear namespaces
Write-Host "Creando namespaces..." -ForegroundColor Yellow
kubectl apply -f manifest-vehiculo/hct-vehiculo-ramirez-fabrizio-namespace.yml
kubectl apply -f manifest-cliente/hct-cliente-ramirez-fabrizio-namespace.yml
kubectl apply -f manifest-alquiler/hct-alquiler-ramirez-fabrizio-namespace.yml
kubectl apply -f manifest-frontend/hct-frontend-ramirez-fabrizio-namespace.yml

# 3. Crear secrets
Write-Host "Creando secrets..." -ForegroundColor Yellow
kubectl apply -f manifest-vehiculo/hct-vehiculo-ramirez-fabrizio-secret.yml
kubectl apply -f manifest-cliente/hct-cliente-ramirez-fabrizio-secret.yml
kubectl apply -f manifest-alquiler/hct-alquiler-ramirez-fabrizio-secret.yml

# 4. Desplegar microservicios
Write-Host "Desplegando microservicios..." -ForegroundColor Yellow
kubectl apply -f manifest-vehiculo/hct-vehiculo-ramirez-fabrizio-deployment.yml
kubectl apply -f manifest-vehiculo/hct-vehiculo-ramirez-fabrizio-service.yml

kubectl apply -f manifest-cliente/hct-cliente-ramirez-fabrizio-deployment.yml
kubectl apply -f manifest-cliente/hct-cliente-ramirez-fabrizio-service.yml

kubectl apply -f manifest-alquiler/hct-alquiler-ramirez-fabrizio-deployment.yml
kubectl apply -f manifest-alquiler/hct-alquiler-ramirez-fabrizio-service.yml

# 5. Desplegar frontend
Write-Host "Desplegando frontend..." -ForegroundColor Yellow
kubectl apply -f manifest-frontend/hct-frontend-ramirez-fabrizio-deployment.yml
kubectl apply -f manifest-frontend/hct-frontend-ramirez-fabrizio-service.yml

# 6. Eliminar variables de entorno del frontend (CRÍTICO)
Write-Host "Eliminando variables de entorno del frontend..." -ForegroundColor Yellow
kubectl patch deployment hct-frontend-ramirez-fabrizio-deployment -n hct-frontend-ramirez-fabrizio --type json -p='[{"op": "remove", "path": "/spec/template/spec/containers/0/env"}]' 2>$null

# 7. Reiniciar deployment del frontend
Write-Host "Reiniciando deployment del frontend..." -ForegroundColor Yellow
kubectl rollout restart deployment hct-frontend-ramirez-fabrizio-deployment -n hct-frontend-ramirez-fabrizio

# 8. Esperar a que los pods estén listos
Write-Host "Esperando a que los pods estén listos..." -ForegroundColor Yellow
Start-Sleep -Seconds 10

Write-Host "=== Despliegue completado ===" -ForegroundColor Green
Write-Host "=== Verificando pods ===" -ForegroundColor Yellow
kubectl get pods --all-namespaces

Write-Host "=== Verificando deployment de frontend ===" -ForegroundColor Yellow
kubectl describe deployment hct-frontend-ramirez-fabrizio-deployment -n hct-frontend-ramirez-fabrizio

Write-Host "=== Iniciando port-forward del frontend ===" -ForegroundColor Green
Write-Host "Ejecuta este comando en una terminal:" -ForegroundColor Cyan
Write-Host "kubectl port-forward svc/hct-frontend-ramirez-fabrizio-service 8080:80 -n hct-frontend-ramirez-fabrizio" -ForegroundColor Cyan
Write-Host ""
Write-Host "Luego accede a http://localhost:8080" -ForegroundColor Cyan
