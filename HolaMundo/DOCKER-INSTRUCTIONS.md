# 🐳 Instrucciones para crear imagen Docker con GitHub Actions

## 📋 PASO A PASO

### **Paso 1: Preparar tu repositorio en GitHub**

1. Asegúrate de que tu código esté subido a GitHub
2. Los archivos importantes ya están en tu proyecto:
   - ✅ `Dockerfile`
   - ✅ `.github/workflows/docker-build.yml` (workflow de GitHub Actions)
   - ✅ `.dockerignore`

### **Paso 2: Subir el código a GitHub**

```powershell
# Navega a tu proyecto
cd "C:\Fuentes\Problemas e incidentes\Problemas e incidentes\ProyectosIntellijIDEA\HolaMundoJava\HolaMundo"

# Inicializa git si no lo has hecho
git init

# Agrega los archivos
git add .

# Commit
git commit -m "Add Docker support and GitHub Actions workflow"

# Conecta con tu repositorio en GitHub (reemplaza con tu URL)
git remote add origin https://github.com/TU-USUARIO/TU-REPOSITORIO.git

# Sube a GitHub
git push -u origin main
```

**Nota:** Si tu rama principal se llama `master` en lugar de `main`, usa `master`.

### **Paso 3: GitHub Actions construirá automáticamente la imagen**

Una vez que hagas push a GitHub:

1. Ve a tu repositorio en GitHub
2. Haz clic en la pestaña **"Actions"**
3. Verás el workflow "Build and Push Docker Image" ejecutándose
4. Espera a que termine (toma unos 3-5 minutos)

### **Paso 4: Verificar tu imagen Docker**

Tu imagen estará disponible en GitHub Container Registry:

1. Ve a tu repositorio en GitHub
2. En la barra lateral derecha, verás **"Packages"**
3. Ahí encontrarás tu imagen Docker

La URL será algo como:
```
ghcr.io/TU-USUARIO/holamundo:latest
```

### **Paso 5: Usar tu imagen Docker**

Cualquier persona (o servidor) puede descargar y ejecutar tu imagen:

```bash
# Hacer login en GitHub Container Registry (primera vez)
docker login ghcr.io -u TU-USUARIO

# Descargar y ejecutar la imagen
docker pull ghcr.io/TU-USUARIO/holamundo:latest
docker run -p 8080:8080 ghcr.io/TU-USUARIO/holamundo:latest
```

---

## 🚀 CUÁNDO SE CONSTRUYE LA IMAGEN

El workflow de GitHub Actions construirá tu imagen automáticamente cuando:

- ✅ Hagas `push` a la rama `main` o `master`
- ✅ Crees un Pull Request
- ✅ Crees un tag de versión (ej: `v1.0.0`)
- ✅ Lo ejecutes manualmente desde la pestaña Actions

---

## 🏷️ TAGS GENERADOS AUTOMÁTICAMENTE

El workflow crea múltiples tags para tu imagen:

- `latest` - Última versión de la rama principal
- `main` o `master` - Por nombre de rama
- `v1.0.0` - Si creas un tag de versión
- `main-sha123456` - Por commit SHA

---

## 🔒 PERMISOS

El workflow usa `GITHUB_TOKEN` automáticamente, no necesitas configurar nada adicional.

---

## 📝 CONFIGURACIÓN ADICIONAL (Opcional)

### Hacer pública tu imagen

Por defecto, las imágenes en GitHub Container Registry son privadas:

1. Ve a tu paquete en GitHub
2. Click en "Package settings"
3. Scroll hasta "Danger Zone"
4. Click en "Change visibility" → "Public"

### Crear una versión específica

```powershell
git tag v1.0.0
git push origin v1.0.0
```

Esto creará una imagen con el tag `v1.0.0` además de `latest`.

---

## ✅ VENTAJAS DE ESTE MÉTODO

- ✅ No necesitas Docker Desktop en tu máquina
- ✅ La construcción es automática al hacer push
- ✅ Se ejecuta en servidores de GitHub (gratis para repos públicos)
- ✅ Genera múltiples tags automáticamente
- ✅ Incluye cache para builds más rápidos
- ✅ Genera attestations de seguridad

---

## 🆘 SOLUCIÓN DE PROBLEMAS

### El workflow falla

1. Ve a Actions en GitHub
2. Click en el workflow fallido
3. Revisa los logs para ver el error

### La imagen no aparece en Packages

- Verifica que el workflow haya terminado exitosamente
- Puede tomar unos minutos en aparecer
- Asegúrate de que tienes permisos de escritura en el repositorio

### Error de permisos

El repositorio debe tener habilitados los permisos de escritura:
1. Settings → Actions → General
2. En "Workflow permissions" selecciona "Read and write permissions"

