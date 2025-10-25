# User Service

## Descripción
Servicio encargado de la gestión de usuarios (registro, autenticación, perfil, etc.) para la plataforma e-commerce.

## Pruebas Implementadas
- **Unitarias**: Sobre lógica de creación y validación de usuarios.
- **Integración**: Pruebas REST entre endpoints internos y dependencias.
- **E2E**: Simulación de registro y login desde la perspectiva de un usuario real.
- **Perfil de pruebas** usando H2 en memoria (`src/test/resources/application-test.yml`).

## Instrucciones para correr las pruebas
```bash
./mvnw test
```

## Evidencia de pruebas
> Los reportes generados se encuentran en `target/surefire-reports` (JUnit XML)
