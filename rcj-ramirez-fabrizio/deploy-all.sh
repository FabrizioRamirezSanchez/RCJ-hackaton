#!/bin/bash

echo "=== Desplegando aplicación RCJ Hackaton ==="

# 1. Crear namespaces
echo "Creando namespaces..."
kubectl apply -f manifest-namespace/hct-vehiculo-ramirez-fabrizio-namespace.yml
kubectl apply -f manifest-namespace/hct-cliente-ramirez-fabrizio-namespace.yml
kubectl apply -f manifest-namespace/hct-alquiler-ramirez-fabrizio-namespace.yml
kubectl apply -f manifest-namespace/hct-frontend-ramirez-fabrizio-namespace.yml

# 2. Crear secrets
echo "Creando secrets..."
kubectl apply -f manifest-secret/hct-vehiculo-ramirez-fabrizio-secret.yml
kubectl apply -f manifest-secret/hct-cliente-ramirez-fabrizio-secret.yml
kubectl apply -f manifest-secret/hct-alquiler-ramirez-fabrizio-secret.yml

# 3. Desplegar MongoDB
echo "Desplegando MongoDB..."
kubectl apply -f manifest-mongodb/hct-mongodb-ramirez-fabrizio-deployment.yml
kubectl apply -f manifest-mongodb/hct-mongodb-ramirez-fabrizio-service.yml

# 4. Desplegar microservicios
echo "Desplegando microservicios..."
kubectl apply -f manifest-vehiculo/hct-vehiculo-ramirez-fabrizio-deployment.yml
kubectl apply -f manifest-vehiculo/hct-vehiculo-ramirez-fabrizio-service.yml

kubectl apply -f manifest-cliente/hct-cliente-ramirez-fabrizio-deployment.yml
kubectl apply -f manifest-cliente/hct-cliente-ramirez-fabrizio-service.yml

kubectl apply -f manifest-alquiler/hct-alquiler-ramirez-fabrizio-deployment.yml
kubectl apply -f manifest-alquiler/hct-alquiler-ramirez-fabrizio-service.yml

# 5. Desplegar frontend
echo "Desplegando frontend..."
kubectl apply -f manifest-frontend/hct-frontend-ramirez-fabrizio-deployment.yml
kubectl apply -f manifest-frontend/hct-frontend-ramirez-fabrizio-service.yml

# 6. Esperar a que los pods estén listos
echo "Esperando a que los pods estén listos..."
kubectl wait --for=condition=ready pod -l app=hct-vehiculo-ramirez-fabrizio -n hct-vehiculo-ramirez-fabrizio --timeout=300s
kubectl wait --for=condition=ready pod -l app=hct-cliente-ramirez-fabrizio -n hct-cliente-ramirez-fabrizio --timeout=300s
kubectl wait --for=condition=ready pod -l app=hct-alquiler-ramirez-fabrizio -n hct-alquiler-ramirez-fabrizio --timeout=300s
kubectl wait --for=condition=ready pod -l app=hct-frontend-ramirez-fabrizio -n hct-frontend-ramirez-fabrizio --timeout=300s

echo "=== Despliegue completado ==="
echo "=== Iniciando port-forwards ==="

echo "Ejecuta estos comandos en terminales separadas:"
echo "kubectl port-forward svc/hct-frontend-ramirez-fabrizio-service 8080:80 -n hct-frontend-ramirez-fabrizio"
echo ""
echo "Luego accede a http://localhost:8080"
