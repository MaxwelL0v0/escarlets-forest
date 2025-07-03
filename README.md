# 🌳 Escarlet's Forest - Aplicación Android

Una aplicación móvil diseñada para reducir los niveles de CO2 globales a través de la plantación de árboles y plantas, conectando a personas de todo el mundo en un bosque global virtual.

## 🎯 Propósito

Esta aplicación fue creada en honor a **Escarlet**, estudiante de ingeniería forestal, con el objetivo de:

- **Reducir los niveles de CO2 globales** mediante el registro de plantas plantadas
- **Crear un bosque global** que conecte a personas de todo el mundo
- **Fomentar la plantación de árboles** y plantas de interior
- **Generar un registro mundial de especies**, especialmente especies protegidas
- **Colaborar con universidades** que imparten ingeniería forestal
- **Conectar comunidades** interesadas en la conservación ambiental

## 🌟 Características Principales

### 📱 Funcionalidades del Usuario
- **Registro de plantas**: Cada usuario puede registrar árboles plantados al aire libre o plantas de interior
- **Nombres personalizados**: Las plantas pueden tener nombres propios, como mascotas
- **Fotos y ubicación**: Captura fotos y registra la ubicación exacta de cada planta
- **Seguimiento de crecimiento**: Monitorea el desarrollo y salud de las plantas
- **Estadísticas personales**: Visualiza tu contribución al medio ambiente

### 🗺️ Mapa Mundial Interactivo
- **Visualización global**: Ve todas las plantas registradas en el mundo
- **Filtros por especie**: Explora diferentes tipos de plantas y árboles
- **Información detallada**: Accede a datos sobre cada planta registrada
- **Especies protegidas**: Identifica y protege especies en peligro

### 📊 Estadísticas y Impacto
- **CO2 absorbido**: Calcula automáticamente el CO2 absorbido por tus plantas
- **Oxígeno producido**: Muestra el oxígeno generado por tu contribución
- **Impacto global**: Visualiza el impacto colectivo de toda la comunidad
- **Logros y reconocimientos**: Desbloquea logros por tus contribuciones

### 🤝 Comunidad y Colaboración
- **Base de datos de especies**: Catálogo colaborativo de especies vegetales
- **Socios universitarios**: Integración con universidades de ingeniería forestal
- **Proyectos de conservación**: Participa en iniciativas de protección ambiental
- **Compartir experiencias**: Conecta con otros amantes de las plantas

## 🛠️ Tecnologías Utilizadas

### Frontend
- **Kotlin**: Lenguaje principal de desarrollo
- **Android Jetpack**: Componentes modernos de Android
- **Material Design 3**: Diseño de interfaz de usuario
- **Navigation Component**: Navegación entre pantallas
- **ViewBinding**: Enlace de vistas
- **LiveData & Flow**: Manejo reactivo de datos

### Backend & Base de Datos
- **Room Database**: Base de datos local
- **Firebase**: Autenticación, almacenamiento y sincronización
- **Google Maps API**: Integración de mapas
- **Hilt**: Inyección de dependencias

### Características Adicionales
- **Cámara integrada**: Captura de fotos de plantas
- **GPS**: Ubicación precisa de plantaciones
- **Notificaciones**: Recordatorios de cuidado de plantas
- **Modo offline**: Funcionalidad sin conexión

## 📱 Pantallas Principales

### 🏠 Inicio
- Estadísticas globales y personales
- Acceso rápido a funciones principales
- Plantas recientes del usuario
- Botones para agregar nuevas plantas

### 🗺️ Mapa Mundial
- Visualización interactiva de todas las plantas
- Filtros por tipo de planta y ubicación
- Información detallada al tocar marcadores
- Búsqueda por región o especie

### 🌱 Mis Plantas
- Lista de todas las plantas del usuario
- Filtros y búsqueda
- Estadísticas personales
- Acceso a detalles de cada planta

### 👥 Comunidad
- Base de datos de especies
- Información sobre socios universitarios
- Proyectos de conservación
- Noticias y eventos ambientales

### 👤 Perfil
- Estadísticas personales
- Logros desbloqueados
- Configuración de la aplicación
- Información del usuario

## 🚀 Instalación y Configuración

### Requisitos Previos
- Android Studio Arctic Fox o superior
- Android SDK API 24+
- Google Maps API Key
- Firebase Project

### Pasos de Instalación

1. **Clonar el repositorio**
   ```bash
   git clone https://github.com/tu-usuario/escarlets-forest.git
   cd escarlets-forest
   ```

2. **Configurar Google Maps API**
   - Obtener una API Key de Google Cloud Console
   - Habilitar Maps SDK for Android
   - Reemplazar `YOUR_MAPS_API_KEY` en `AndroidManifest.xml`

3. **Configurar Firebase**
   - Crear un proyecto en Firebase Console
   - Descargar `google-services.json`
   - Colocarlo en la carpeta `app/`

4. **Sincronizar dependencias**
   ```bash
   ./gradlew build
   ```

5. **Ejecutar la aplicación**
   - Conectar dispositivo Android o usar emulador
   - Ejecutar desde Android Studio

### Configuración de Permisos

La aplicación requiere los siguientes permisos:
- **Cámara**: Para tomar fotos de las plantas
- **Ubicación**: Para registrar la posición de las plantas
- **Almacenamiento**: Para guardar fotos
- **Internet**: Para sincronización con Firebase

## 📊 Estructura del Proyecto

```
app/
├── src/main/
│   ├── java/com/escarletsforest/app/
│   │   ├── data/
│   │   │   ├── converter/          # Convertidores para Room
│   │   │   ├── dao/               # Data Access Objects
│   │   │   ├── database/          # Configuración de base de datos
│   │   │   ├── model/             # Modelos de datos
│   │   │   └── repository/        # Repositorios
│   │   ├── ui/
│   │   │   ├── home/              # Fragmento de inicio
│   │   │   ├── map/               # Mapa mundial
│   │   │   ├── plants/            # Gestión de plantas
│   │   │   ├── community/         # Comunidad
│   │   │   ├── profile/           # Perfil de usuario
│   │   │   └── viewmodel/         # ViewModels
│   │   └── service/               # Servicios en segundo plano
│   └── res/
│       ├── layout/                # Layouts XML
│       ├── values/                # Strings, colores, temas
│       ├── drawable/              # Iconos y recursos gráficos
│       └── navigation/            # Grafos de navegación
```

## 🌍 Impacto Ambiental

### Cálculos de CO2
- **Árbol promedio**: 22 kg CO2/año
- **Arbusto**: 5 kg CO2/año
- **Planta de interior**: 0.5 kg CO2/año
- **Hierba**: 0.1 kg CO2/año

### Metas de la Aplicación
- **Corto plazo**: 10,000 plantas registradas
- **Mediano plazo**: 100,000 plantas y 2,200 toneladas de CO2 absorbido
- **Largo plazo**: 1,000,000 plantas y 22,000 toneladas de CO2 absorbido

## 🤝 Contribución

### Para Desarrolladores
1. Fork el proyecto
2. Crear una rama para tu feature (`git checkout -b feature/AmazingFeature`)
3. Commit tus cambios (`git commit -m 'Add some AmazingFeature'`)
4. Push a la rama (`git push origin feature/AmazingFeature`)
5. Abrir un Pull Request

### Para Usuarios
- Reportar bugs y sugerir mejoras
- Compartir la aplicación con amigos y familia
- Participar en la comunidad de usuarios
- Contribuir con fotos y datos de especies

## 📞 Contacto y Soporte

- **Email**: contacto@escarletsforest.com
- **Website**: https://escarletsforest.com
- **GitHub**: https://github.com/tu-usuario/escarlets-forest
- **Documentación**: https://docs.escarletsforest.com

## 📄 Licencia

Este proyecto está bajo la Licencia MIT. Ver el archivo `LICENSE` para más detalles.

## 🙏 Agradecimientos

- **Escarlet**: Por la inspiración y dedicación a la ingeniería forestal
- **Comunidad Android**: Por las herramientas y recursos de desarrollo
- **Google Maps**: Por la plataforma de mapas
- **Firebase**: Por los servicios de backend
- **Contribuidores**: Todos los que ayudan a hacer crecer este bosque digital

---

**¡Juntos podemos crear un bosque global que conecte a personas de todo el mundo y ayude a salvar nuestro planeta! 🌍🌳** 