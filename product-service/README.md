# Product Service

## Descripción
Gestiona información de productos, inventario y búsqueda para la plataforma e-commerce.

## Pruebas Implementadas
- **Unitarias**: Sobre lógica de productos, validación de stock, etc.
- **Integración**: Prueba REST de endpoints internos y dependencias.
- **Performance**: Medición del tiempo de respuesta ante navegación masiva de catálogo (ver `perf/locust_catalog_browsing.py`).

## Instrucciones para correr las pruebas
```bash
./mvnw test
# Medición de rendimiento
locust -f perf/locust_catalog_browsing.py --headless -u 30 -r 5 -t 2m
```

## Evidencia de pruebas
> Los reportes de JUnit se generan en `target/surefire-reports` y Locust en formato csv.
