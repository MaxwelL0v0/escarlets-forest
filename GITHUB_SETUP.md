# 🚀 Guía para Subir a GitHub y Configurar Firebase Studio

## ✅ Pasos Completados

1. ✅ Creado `.gitignore` para excluir archivos sensibles
2. ✅ Creado `firebase.json` para configuración de Firebase
3. ✅ Creado `firestore.rules` para reglas de seguridad
4. ✅ Creado `storage.rules` para reglas de almacenamiento
5. ✅ Creado `firestore.indexes.json` para optimización
6. ✅ Creado `.github/workflows/android.yml` para CI/CD
7. ✅ Creado `package.json` para herramientas de desarrollo
8. ✅ Creado `.firebaserc` para configuración de proyecto
9. ✅ Inicializado repositorio Git con commit inicial

## 🔄 Próximos Pasos

### 1. Crear Repositorio en GitHub

1. Ve a [GitHub.com](https://github.com) y inicia sesión
2. Haz clic en el botón verde "New" o "+" → "New repository"
3. Configura el repositorio:
   - **Repository name**: `escarlets-forest`
   - **Description**: `Aplicación Android para reducir CO2 mediante plantación de árboles`
   - **Visibility**: Public (recomendado) o Private
   - **NO** marques "Add a README file" (ya tenemos uno)
   - **NO** marques "Add .gitignore" (ya tenemos uno)
   - **NO** marques "Choose a license" (ya está en README)

4. Haz clic en "Create repository"

### 2. Conectar Repositorio Local con GitHub

```bash
# Agregar el repositorio remoto (reemplaza TU_USUARIO con tu nombre de usuario)
git remote add origin https://github.com/TU_USUARIO/escarlets-forest.git

# Cambiar la rama principal a 'main' (estándar actual)
git branch -M main

# Subir el código a GitHub
git push -u origin main
```

### 3. Configurar Firebase Studio

#### Opción A: Usar Firebase CLI (Recomendado)

1. **Instalar Firebase CLI**:
   ```bash
   npm install -g firebase-tools
   ```

2. **Iniciar sesión en Firebase**:
   ```bash
   firebase login
   ```

3. **Inicializar Firebase en el proyecto**:
   ```bash
   firebase init
   ```
   
   Selecciona:
   - ✅ Firestore
   - ✅ Storage
   - ✅ Emulators
   - Proyecto: `escarletsforestv10`

4. **Desplegar reglas y configuración**:
   ```bash
   firebase deploy --only firestore:rules,storage
   ```

#### Opción B: Usar Firebase Console Web

1. Ve a [Firebase Console](https://console.firebase.google.com)
2. Selecciona tu proyecto `escarletsforestv10`
3. Ve a **Firestore Database** → **Rules**
4. Copia y pega el contenido de `firestore.rules`
5. Ve a **Storage** → **Rules**
6. Copia y pega el contenido de `storage.rules`

### 4. Configurar GitHub Actions (Opcional)

1. Ve a tu repositorio en GitHub
2. Ve a **Settings** → **Secrets and variables** → **Actions**
3. Agrega los siguientes secrets:
   - `FIREBASE_PROJECT_ID`: `escarletsforestv10`
   - `FIREBASE_SERVICE_ACCOUNT`: (JSON de cuenta de servicio de Firebase)

### 5. Configurar Firebase Studio

1. Ve a [Firebase Console](https://console.firebase.google.com)
2. Selecciona tu proyecto
3. Ve a **Project Settings** → **General**
4. En la sección "Your apps", verifica que tu app Android esté configurada
5. Descarga el archivo `google-services.json` actualizado si es necesario

### 6. Verificar Configuración

1. **Clonar el repositorio en otra máquina**:
   ```bash
   git clone https://github.com/TU_USUARIO/escarlets-forest.git
   cd escarlets-forest
   ```

2. **Instalar dependencias**:
   ```bash
   npm install
   ```

3. **Ejecutar emuladores de Firebase**:
   ```bash
   npm run firebase:emulators
   ```

4. **Compilar la app Android**:
   ```bash
   npm run build:android
   ```

## 🔧 Configuración Adicional

### Variables de Entorno (Opcional)

Crea un archivo `.env` en la raíz del proyecto:

```env
FIREBASE_PROJECT_ID=escarletsforestv10
GOOGLE_MAPS_API_KEY=tu_api_key_aqui
```

### Configuración de Desarrollo

1. **Habilitar emuladores de Firebase**:
   ```bash
   firebase emulators:start
   ```

2. **Configurar Android Studio**:
   - Abrir el proyecto en Android Studio
   - Sincronizar con Gradle
   - Configurar el emulador de Firebase en `google-services.json`

## 🚨 Notas Importantes

### Seguridad
- ✅ `google-services.json` está en `.gitignore` (no se subirá a GitHub)
- ✅ `local.properties` está en `.gitignore` (configuración local)
- ✅ Reglas de Firestore configuradas para seguridad

### Compatibilidad
- ✅ Firebase Studio compatible
- ✅ GitHub Actions configurado
- ✅ Emuladores de Firebase configurados
- ✅ Reglas de seguridad implementadas

### Próximas Mejoras
- [ ] Configurar Google Maps API Key
- [ ] Implementar notificaciones push
- [ ] Agregar analytics de Firebase
- [ ] Configurar Crashlytics

## 📞 Soporte

Si tienes problemas:
1. Revisa los logs de Firebase Console
2. Verifica la configuración en `firebase.json`
3. Asegúrate de que las reglas de Firestore estén desplegadas
4. Consulta la documentación de Firebase

¡Tu proyecto está listo para ser compartido y desarrollado colaborativamente! 🌳 