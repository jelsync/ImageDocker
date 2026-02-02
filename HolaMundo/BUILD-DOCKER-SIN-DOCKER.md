# Guía: Construir Imagen Docker SIN tener Docker instalado

## Resumen
Esta guía te permite crear y subir imágenes Docker usando **Jib** (herramienta de Google) sin necesidad de tener Docker Desktop instalado.

**✅ MÉTODO QUE FUNCIONÓ: Construcción Local (Paso 3)**

---

## 1. Crear Personal Access Token de GitHub

1. Ve a GitHub → Click en tu foto de perfil → **Settings**
2. Scroll hasta el final → **Developer settings**
3. **Personal access tokens** → **Tokens (classic)** → **Generate new token (classic)**
4. Configurar:
   - **Note**: `Jib Docker Build`
   - **Expiration**: 90 días o más
   - **Permisos**:
     - ✅ `write:packages`
     - ✅ `read:packages` (se selecciona automáticamente)
     - ✅ `repo`
5. Click **Generate token**
6. **COPIAR EL TOKEN** (empieza con `ghp_...`) - ¡No podrás verlo de nuevo!

---

## 2. Configurar pom.xml

Agregar el plugin Jib en la sección `<build><plugins>`:

```xml
<plugin>
    <groupId>com.google.cloud.tools</groupId>
    <artifactId>jib-maven-plugin</artifactId>
    <version>3.4.0</version>
    <configuration>
        <from>
            <!-- Usar imagen pública de Amazon Corretto (no requiere login) -->
            <image>public.ecr.aws/amazoncorretto/amazoncorretto:17</image>
        </from>
        <to>
            <!-- Cambiar 'jelsync/imagedocker' por tu usuario/repo -->
            <image>ghcr.io/jelsync/imagedocker</image>
            <tags>
                <tag>latest</tag>
                <tag>${project.version}</tag>
            </tags>
        </to>
        <container>
            <ports>
                <port>8080</port>
            </ports>
            <format>OCI</format>
            <creationTime>USE_CURRENT_TIMESTAMP</creationTime>
        </container>
    </configuration>
</plugin>
```

---

## 3. Comando para Construir y Subir (Localmente)

Abrir PowerShell y ejecutar:

```powershell
cd "c:ruta/HolaMundo"

& "C:\Program Files\JetBrains\IntelliJ IDEA 2025.2.1\plugins\maven\lib\maven3\bin\mvn.cmd" clean package jib:build "-Djib.to.auth.username=jelsync" "-Djib.to.auth.password=TU_TOKEN_AQUI"
```

**IMPORTANTE**: 
- Reemplazar `TU_TOKEN_AQUI` con tu token de GitHub
- Los parámetros `-D` deben estar entre comillas en PowerShell

---

## 4. Configurar GitHub Actions (Automático)

Archivo: `.github/workflows/docker-build.yml`

```yaml
name: Build and Push Docker Image with Jib

on:
  push:
    branches:
      - main
      - master
  workflow_dispatch:

env:
  REGISTRY: ghcr.io
  GITHUB_REPOSITORY: ${{ github.repository }}

jobs:
  build-and-push:
    runs-on: ubuntu-latest
    permissions:
      contents: read
      packages: write

    steps:
      - name: Checkout repository
        uses: actions/checkout@v4

      - name: Set up JDK 17
        uses: actions/setup-java@v4
        with:
          java-version: '17'
          distribution: 'temurin'
          cache: 'maven'

      - name: Log in to GitHub Container Registry
        run: echo "${{ secrets.GITHUB_TOKEN }}" | docker login ghcr.io -u ${{ github.actor }} --password-stdin

      - name: Build and push image with Jib
        working-directory: ./HolaMundo
        run: |
          mvn clean package jib:build \
            -Djib.to.auth.username=${{ github.actor }} \
            -Djib.to.auth.password=${{ secrets.GITHUB_TOKEN }} \
            -Djib.to.image=ghcr.io/${{ github.repository }}:latest
```

---

## 5. Verificar la Imagen

Después de que se cree la imagen:

1. Ve a tu repositorio en GitHub
2. Click en **Packages** (lado derecho)
3. Verás tu imagen: `imagedocker`
4. La URL será: `ghcr.io/jelsync/imagedocker:latest`

---

## 6. Usar la Imagen

Para ejecutar tu imagen Docker:

```bash
docker pull ghcr.io/jelsync/imagedocker:latest
docker run -p 8080:8080 ghcr.io/jelsync/imagedocker:latest
```

---

## Ventajas de Jib

✅ **No requiere Docker instalado**  
✅ Más rápido que Docker tradicional  
✅ Imágenes optimizadas por capas automáticamente  
✅ Menor tamaño de imagen  
✅ Integrado con Maven/Gradle  
✅ Construcción reproducible  

---

## Troubleshooting

### Error: "Unauthorized for registry-1.docker.io"
- **Solución**: Usar imagen base pública como `public.ecr.aws/amazoncorretto/amazoncorretto:17`

### Error: "Unknown lifecycle phase .to.auth.username"
- **Solución**: Poner comillas alrededor de los parámetros `-D` en PowerShell

### Error: "Invalid image reference ${env.GITHUB_REPOSITORY}"
- **Solución**: Usar la ruta completa en local: `ghcr.io/jelsync/imagedocker`

---

## Recursos

- [Documentación Jib](https://github.com/GoogleContainerTools/jib)
- [GitHub Packages](https://docs.github.com/en/packages)
- [Amazon Corretto](https://aws.amazon.com/corretto/)
