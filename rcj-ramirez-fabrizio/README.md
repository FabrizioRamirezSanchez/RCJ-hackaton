# RCJ Hackaton - Sistema de Alquiler de Vehículos

## Requisitos
- Docker Desktop con Kubernetes habilitado
- kubectl instalado
- PowerShell (Windows) o Bash (Linux/Mac)

## Despliegue Automático

### Windows (PowerShell)
```powershell
cd rcj-ramirez-fabrizio
.\deploy-all.ps1
```

### Linux/Mac (Bash)
```bash
cd rcj-ramirez-fabrizio
chmod +x deploy-all.sh
./deploy-all.sh
```

## Acceso a la Aplicación

Después del despliegue, ejecuta el port-forward del frontend:

```bash
kubectl port-forward svc/hct-frontend-ramirez-fabrizio-service 8080:80 -n hct-frontend-ramirez-fabrizio
```

Luego accede a: http://localhost:8080

## Arquitectura

- **Frontend**: Angular con Nginx como proxy
- **Backend**: 3 microservicios Spring Boot (vehículo, cliente, alquiler)
- **Base de datos**: MongoDB

## Notas Importantes

- El frontend usa Nginx como proxy interno para redirigir las peticiones API a los microservicios
- Solo necesitas el port-forward del frontend (8080), no de los servicios backend
- Los datos se almacenan en MongoDB dentro de Kubernetes
- Cada PC tiene su propia instancia de la aplicación con sus propios datos

## Solución de Problemas

Si la página no carga:
```bash
# Verificar pods
kubectl get pods --all-namespaces

# Verificar logs del frontend
kubectl logs -n hct-frontend-ramirez-fabrizio -l app=hct-frontend-ramirez-fabrizio

# Verificar deployment del frontend
kubectl describe deployment hct-frontend-ramirez-fabrizio-deployment -n hct-frontend-ramirez-fabrizio
```

## Limpieza

Para eliminar todo:
```bash
kubectl delete namespace hct-vehiculo-ramirez-fabrizio
kubectl delete namespace hct-cliente-ramirez-fabrizio
kubectl delete namespace hct-alquiler-ramirez-fabrizio
kubectl delete namespace hct-frontend-ramirez-fabrizio
```
