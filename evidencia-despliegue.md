# Evidencia de despliegue y problemas con microservicios

## 1. Comandos utilizados para el despliegue

```powershell
kubectl apply -f .\cloud-config\k8s\deployment-stage.yaml -n stage
kubectl apply -f .\cloud-config\k8s\service-stage.yaml -n stage
kubectl apply -f .\service-discovery\k8s\deployment-stage.yaml -n stage
kubectl apply -f .\service-discovery\k8s\service-stage.yaml -n stage
kubectl apply -f .\user-service\k8s\deployment-stage.yaml -n stage
kubectl apply -f .\user-service\k8s\service-stage.yaml -n stage
```

## 2. Estado de los pods tras el despliegue

```shell
kubectl get pods -n stage
```

Ejemplo de salida:
```
NAME                                 READY   STATUS             RESTARTS        AGE
cloud-config-6797475bb9-kzfrj        1/1     Running            5 (3m48s ago)   37m
service-discovery-867479fd44-m48j2   1/1     Running            0               37m
user-service-85d8ff4788-gqwvg        0/1     CrashLoopBackOff   8 (74s ago)     37m
```

## 3. Logs de user-service

```shell
kubectl logs -n stage user-service-85d8ff4788-gqwvg --tail=100
```

Fragmento relevante:
```
org.springframework.cloud.config.client.ConfigClientFailFastException: Could not locate PropertySource and the resource is not optional, failing
...
Caused by: org.springframework.web.client.ResourceAccessException: I/O error on GET request for "http://cloud-config.stage.svc.cluster.local:9296/USER-SERVICE/stage": Connection refused (Connection refused)
```

## 4. Interpretación y evidencia

- Los pods de `cloud-config` y `service-discovery` están en estado `Running` y `Ready`, lo que demuestra que el pipeline y los manifiestos funcionan correctamente para estos servicios.
- El pod de `user-service` entra en estado `CrashLoopBackOff` debido a que no puede conectarse al servidor de configuración global (`cloud-config`).
- Aunque el pod de `user-service` puede llegar a estar en `Ready` temporalmente, no responde correctamente a peticiones externas (por ejemplo, usando `curl`), ya que falla en la obtención de configuración y no termina de arrancar el servicio.

## 5. Decisión y consecuencias

- Se evidencia que el pipeline y la infraestructura funcionan para `cloud-config` y `service-discovery`.
- El fallo de `user-service` se documenta con logs y estado del pod.
- Para continuar con las pruebas y despliegue, se recomienda probar con otro microservicio (por ejemplo, `proxy-client`) si las pruebas no dependen estrictamente de `user-service`.

## 6. Pantallazos sugeridos para el reporte

- Comandos de despliegue ejecutados en PowerShell.
- Estado de los pods tras el despliegue (`kubectl get pods -n stage`).
- Logs de error de `user-service`.
- Fragmentos de configuración YAML relevantes (Cloud Config, Eureka).

---

> Este documento resume el proceso, los comandos utilizados, los resultados observados y la interpretación de los problemas encontrados con `user-service`, cumpliendo los requisitos del taller para evidenciar el despliegue y análisis de los microservicios.
