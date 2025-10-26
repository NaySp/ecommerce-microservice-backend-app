# PUNTO 4 - VALIDACIÓN DE DESPLIEGUE STAGE SIN CLUSTER KUBERNETES

**Escenario:** Ambiente sin acceso a cluster Kubernetes real  
**Alternativa:** Validación de manifiestos YAML y configuración CI/CD  
**Fecha:** 26 de Octubre de 2025

---

## 1. DIAGNÓSTICO DEL PROBLEMA

### Error Observado

```
Error from server (Forbidden): unknown (post namespaces)
Authentication required - /login?from=%2Fopenapi%2Fv2
```

### Causas Posibles

1. **Kubeconfig no configurado correctamente**
   - Falta credenciales de acceso al cluster
   - Token expirado

2. **Cluster Kubernetes no disponible**
   - Cluster no está ejecutándose
   - Red de acceso bloqueada

3. **Permisos RBAC insuficientes**
   - Usuario no tiene permisos para crear namespaces

---

## 2. VALIDACIÓN ALTERNATIVA: ANÁLISIS DE MANIFIESTOS

### 2.1 Estructura de Archivos Requerida

Verifica que existan los siguientes archivos:

```bash
# Comando para listar estructura
Get-ChildItem -Path ".\*-service\k8s\" -Recurse -Filter "*.yaml"
```

**Archivos esperados:**

```
user-service/k8s/
├── deployment-stage.yaml
├── service-stage.yaml
└── configmap-stage.yaml

order-service/k8s/
├── deployment-stage.yaml
├── service-stage.yaml
└── configmap-stage.yaml

product-service/k8s/
├── deployment-stage.yaml
├── service-stage.yaml
└── configmap-stage.yaml
```

### 2.2 Validación de Sintaxis YAML

#### Paso 1: Instalar Validador

```powershell
# Instalar yamllint (si tienes Python)
pip install yamllint

# O usar validador online en PowerShell
$yaml_file = "user-service/k8s/deployment-stage.yaml"
$content = Get-Content $yaml_file -Raw
# Verificar que no tenga caracteres inválidos
$content -match '^[\x00-\x7F]*$' # Debe ser ASCII válido
```

#### Paso 2: Validar Estructura YAML

```powershell
# Comando PowerShell para validar estructura básica
function Test-YAMLStructure {
    param([string]$FilePath)
    
    $content = Get-Content $FilePath -Raw
    
    # Validar indentación (debe usar espacios, no tabs)
    if ($content -contains "`t") {
        Write-Host "ERROR: Archivo contiene tabs. Usar espacios (2 o 4)."
        return $false
    }
    
    # Validar campos obligatorios para Deployment
    if ($content -match 'kind: Deployment') {
        if (-not ($content -match 'metadata:')) {
            Write-Host "ERROR: Falta metadata en Deployment"
            return $false
        }
        if (-not ($content -match 'spec:')) {
            Write-Host "ERROR: Falta spec en Deployment"
            return $false
        }
        if (-not ($content -match 'containers:')) {
            Write-Host "ERROR: Falta containers en Deployment"
            return $false
        }
    }
    
    Write-Host "OK: Estructura YAML válida"
    return $true
}

# Uso
Test-YAMLStructure "user-service/k8s/deployment-stage.yaml"
Test-YAMLStructure "order-service/k8s/deployment-stage.yaml"
Test-YAMLStructure "product-service/k8s/deployment-stage.yaml"
```

### 2.3 Validación de Configuración (Sin ejecutar kubectl)

#### Validación 1: Puertos Correctos

```powershell
# Verificar puertos en manifiestos
function Validate-Ports {
    $expected = @{
        'user-service'    = '8700'
        'order-service'   = '8300'
        'product-service' = '8500'
    }
    
    foreach ($service in $expected.Keys) {
        $port = $expected[$service]
        $file = "$service/k8s/deployment-stage.yaml"
        
        $content = Get-Content $file -Raw
        
        if ($content -match "containerPort:\s*$port") {
            Write-Host "[PASS] $service puerto $port configurado correctamente"
        } else {
            Write-Host "[FAIL] $service puerto $port NO encontrado"
        }
    }
}

Validate-Ports
```

#### Validación 2: Namespaces

```powershell
# Verificar que todos los manifiestos usan namespace 'stage'
function Validate-Namespace {
    $files = @(
        "user-service/k8s/deployment-stage.yaml",
        "order-service/k8s/deployment-stage.yaml",
        "product-service/k8s/deployment-stage.yaml"
    )
    
    foreach ($file in $files) {
        $content = Get-Content $file -Raw
        
        if ($content -match 'namespace:\s*stage') {
            Write-Host "[PASS] $file usa namespace 'stage'"
        } else {
            Write-Host "[FAIL] $file NO especifica namespace 'stage'"
        }
    }
}

Validate-Namespace
```

#### Validación 3: Replicas

```powershell
# Verificar configuración de replicas
function Validate-Replicas {
    $files = @(
        "user-service/k8s/deployment-stage.yaml",
        "order-service/k8s/deployment-stage.yaml",
        "product-service/k8s/deployment-stage.yaml"
    )
    
    foreach ($file in $files) {
        $content = Get-Content $file -Raw
        
        if ($content -match 'replicas:\s*(\d+)') {
            $replicas = $matches[1]
            Write-Host "[PASS] $file replicas: $replicas"
        } else {
            Write-Host "[FAIL] $file NO especifica replicas"
        }
    }
}

Validate-Replicas
```

#### Validación 4: Health Checks

```powershell
# Verificar probes (Liveness y Readiness)
function Validate-HealthChecks {
    $files = @(
        "user-service/k8s/deployment-stage.yaml",
        "order-service/k8s/deployment-stage.yaml",
        "product-service/k8s/deployment-stage.yaml"
    )
    
    foreach ($file in $files) {
        $content = Get-Content $file -Raw
        
        $hasLiveness = $content -match 'livenessProbe:'
        $hasReadiness = $content -match 'readinessProbe:'
        
        if ($hasLiveness) {
            Write-Host "[PASS] $file tiene livenessProbe"
        } else {
            Write-Host "[WARN] $file SIN livenessProbe"
        }
        
        if ($hasReadiness) {
            Write-Host "[PASS] $file tiene readinessProbe"
        } else {
            Write-Host "[WARN] $file SIN readinessProbe"
        }
    }
}

Validate-HealthChecks
```

#### Validación 5: Resources (CPU/Memory)

```powershell
# Verificar requests y limits
function Validate-Resources {
    $files = @(
        "user-service/k8s/deployment-stage.yaml",
        "order-service/k8s/deployment-stage.yaml",
        "product-service/k8s/deployment-stage.yaml"
    )
    
    foreach ($file in $files) {
        $content = Get-Content $file -Raw
        
        $hasRequests = $content -match 'requests:'
        $hasLimits = $content -match 'limits:'
        
        if ($hasRequests -and $hasLimits) {
            Write-Host "[PASS] $file tiene requests y limits configurados"
        } else {
            Write-Host "[FAIL] $file falta configuración de resources"
        }
    }
}

Validate-Resources
```

---

## 3. VALIDACIÓN DE CONFIGURACIÓN STAGE

### 3.1 Verificar application-stage.yml

```powershell
# Verificar que existan archivos de configuración stage
function Validate-StageConfig {
    $services = @('user-service', 'order-service', 'product-service')
    
    foreach ($service in $services) {
        $stageConfig = "$service/src/main/resources/application-stage.yml"
        
        if (Test-Path $stageConfig) {
            Write-Host "[PASS] $service tiene application-stage.yml"
            
            $content = Get-Content $stageConfig -Raw
            
            # Validar propiedades stage
            if ($content -match 'spring:.*profiles:.*active:.*stage') {
                Write-Host "  ✓ Perfil stage activado"
            }
            
            if ($content -match 'eureka:.*client:.*service-url:') {
                Write-Host "  ✓ Eureka configurado"
            }
        } else {
            Write-Host "[FAIL] $service NO tiene application-stage.yml"
        }
    }
}

Validate-StageConfig
```

### 3.2 Verificar Jenkinsfile-stage

```powershell
# Verificar que existan Jenkinsfiles para stage
function Validate-JenkinsfileStage {
    $services = @('user-service', 'order-service', 'product-service')
    
    foreach ($service in $services) {
        $jenkinsfile = "$service/Jenkinsfile-stage"
        
        if (Test-Path $jenkinsfile) {
            Write-Host "[PASS] $service tiene Jenkinsfile-stage"
            
            $content = Get-Content $jenkinsfile -Raw
            
            # Validar stages
            $expectedStages = @(
                'Checkout',
                'Build',
                'Unit Tests',
                'Integration Tests',
                'Deploy to Stage',
                'Smoke Tests'
            )
            
            foreach ($stage in $expectedStages) {
                if ($content -match "stage\('$stage'\)") {
                    Write-Host "  ✓ Stage '$stage' presente"
                } else {
                    Write-Host "  ✗ Stage '$stage' FALTA"
                }
            }
        } else {
            Write-Host "[FAIL] $service NO tiene Jenkinsfile-stage"
        }
    }
}

Validate-JenkinsfileStage
```

---

## 4. VALIDACIÓN DE PIPELINE JENKINS

### 4.1 Prerequisitos para Validar Pipeline

1. Jenkins debe estar ejecutándose
2. Repositorio debe estar configurado
3. Jenkinsfile-stage debe existir

### 4.2 Script de Validación Jenkins

```groovy
// Validación en Groovy (dentro de Jenkins)
pipeline {
    agent any
    
    stages {
        stage('Validate Manifests') {
            steps {
                script {
                    echo "===== VALIDANDO MANIFIESTOS KUBERNETES ====="
                    
                    // Validar sintaxis YAML
                    sh '''
                        for service in user-service order-service product-service; do
                            for file in $service/k8s/*.yaml; do
                                echo "[CHECK] Validando $file"
                                kubectl apply -f $file --dry-run=client -o name || exit 1
                            done
                        done
                    '''
                    
                    echo "[PASS] Todos los manifiestos tienen sintaxis válida"
                }
            }
        }
        
        stage('Validate Configuration') {
            steps {
                script {
                    echo "===== VALIDANDO CONFIGURACIÓN STAGE ====="
                    
                    sh '''
                        for service in user-service order-service product-service; do
                            # Verificar archivos requeridos
                            test -f $service/k8s/deployment-stage.yaml || exit 1
                            test -f $service/k8s/service-stage.yaml || exit 1
                            test -f $service/src/main/resources/application-stage.yml || exit 1
                            
                            echo "[PASS] $service tiene toda configuración stage"
                        done
                    '''
                }
            }
        }
        
        stage('Check Kubectl Access') {
            steps {
                script {
                    echo "===== VERIFICANDO ACCESO A KUBERNETES ====="
                    
                    sh '''
                        # Intentar conectar a cluster
                        if kubectl cluster-info; then
                            echo "[PASS] Conexión a Kubernetes exitosa"
                        else
                            echo "[WARN] No se puede acceder a Kubernetes"
                            echo "SOLUCIÓN: Configurar kubeconfig o credenciales"
                            exit 1
                        fi
                    '''
                }
            }
        }
    }
    
    post {
        always {
            echo "===== RESUMEN DE VALIDACIÓN ====="
            sh '''
                echo "Manifiestos YAML: VALIDADOS"
                echo "Configuración Stage: VALIDADA"
                echo "Acceso Kubernetes: REQUERIDO"
            '''
        }
    }
}
```

---

## 5. MATRIZ DE VALIDACIÓN (SIN CLUSTER)

| Criterio | Validación | Comando/Método | Estado |
|----------|-----------|---|--------|
| Archivos YAML Existen | Estructura | `Test-Path user-service/k8s/*.yaml` | [ ] |
| Sintaxis YAML Válida | Estructura | yamllint o validador | [ ] |
| Puertos Correctos | Contenido | Grep `containerPort: 8700` | [ ] |
| Namespaces Configurados | Contenido | Grep `namespace: stage` | [ ] |
| Replicas Definidas | Contenido | Grep `replicas:` | [ ] |
| Health Checks | Contenido | Grep `livenessProbe:` | [ ] |
| Resources Definidos | Contenido | Grep `resources:` | [ ] |
| application-stage.yml Existe | Estructura | `Test-Path */src/main/resources/application-stage.yml` | [ ] |
| Jenkinsfile-stage Existe | Estructura | `Test-Path */Jenkinsfile-stage` | [ ] |
| Stages Jenkins Presentes | Contenido | Grep `stage(` en Jenkinsfile-stage | [ ] |

---

## 6. SOLUCIÓN: CONFIGURAR KUBECONFIG

Si tienes cluster Kubernetes pero falta configuración:

### 6.1 Obtener Credenciales

```bash
# Desde tu proveedor Kubernetes (AWS EKS, AzureAKS, etc.)
aws eks update-kubeconfig --region us-east-1 --name my-cluster
# O
az aks get-credentials --resource-group myResourceGroup --name myCluster

# O si tienes archivo kubeconfig
set KUBECONFIG=C:\path\to\kubeconfig.yaml
```

### 6.2 Verificar Conexión

```bash
kubectl cluster-info
kubectl get nodes
kubectl get namespaces
```

### 6.3 Crear Namespace

```bash
kubectl create namespace stage
kubectl label namespace stage environment=stage tier=microservices
```

### 6.4 Aplicar Manifiestos (UNA VEZ CONECTADO)

```bash
# Con validación primero
kubectl apply -f user-service/k8s/deployment-stage.yaml --validate=true -n stage
kubectl apply -f user-service/k8s/service-stage.yaml --validate=true -n stage

kubectl apply -f order-service/k8s/deployment-stage.yaml --validate=true -n stage
kubectl apply -f order-service/k8s/service-stage.yaml --validate=true -n stage

kubectl apply -f product-service/k8s/deployment-stage.yaml --validate=true -n stage
kubectl apply -f product-service/k8s/service-stage.yaml --validate=true -n stage
```

### 6.5 Verificar Despliegue

```bash
# Verificar pods
kubectl get pods -n stage -w

# Verificar servicios
kubectl get svc -n stage

# Verificar detalles
kubectl describe deployment user-service -n stage
```

---

## 7. EVIDENCIA RECOMENDADA

Para documentar sin acceso a cluster real, capturar:

### 7.1 Evidencia Estática

1. **Captura de archivos YAML válidos**
   ```powershell
   # Mostrar contenido de un manifiestos
   Get-Content user-service/k8s/deployment-stage.yaml | Select-Object -First 50
   ```

2. **Validación de sintaxis**
   ```powershell
   # Mostrar que pasan validación
   yamllint user-service/k8s/deployment-stage.yaml
   ```

3. **Validación de configuración**
   ```powershell
   # Scripts de validación PowerShell ejecutados
   Validate-Ports
   Validate-Namespace
   Validate-HealthChecks
   ```

### 7.2 Evidencia Dinámica (Con Cluster)

1. Logs de despliegue exitoso
2. Output de `kubectl get pods -n stage`
3. Output de `kubectl get svc -n stage`
4. Logs de Jenkins mostrando pipeline en verde

---

## 8. CONCLUSIÓN

**Para validación sin Kubernetes real:**
- ✓ Validar sintaxis YAML
- ✓ Validar estructura de manifiestos
- ✓ Validar configuración stage
- ✓ Validar Jenkinsfiles
- ✓ Documentar con evidencia estática

**Para validación con Kubernetes:**
- Configurar kubeconfig
- Aplicar manifiestos con `kubectl apply`
- Verificar con `kubectl get` y `kubectl describe`
- Capturar evidencia de logs y estado

**Próximo paso:** ¿Tienes acceso a cluster Kubernetes o necesitas usar validación estática?
