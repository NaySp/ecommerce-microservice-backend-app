# Order Service

## Descripción
Administra la creación, consulta y procesamiento de órdenes de compra. Orquesta la interacción con user-service, product-service y otros microservicios.

## Pruebas Implementadas
- **Unitarias**: Lógica de creación y validación de órdenes (CheckoutServiceUnitTest, OrderServiceUnitTest).
- **Integración**: Comunicación real con servicios de usuario/producto (OrderUserIntegrationTest, OrderProductIntegrationTest).
- **End-to-End**: Flujos completos de pedido desde cliente (OrderEndToEndTest).
- **Performance**: Prueba de concurrencia y carga en creación de órdenes (`perf/locust_checkout_flow.py`).

## Ejecución de pruebas automatizadas
```bash
./mvnw test
# Pruebas de rendimiento
locust -f perf/locust_checkout_flow.py --headless -u 50 -r 10 -t 3m
```

## Evidencia y reportes
- JUnit XML: `target/surefire-reports`
- Locust: csv generado en directorio raíz
