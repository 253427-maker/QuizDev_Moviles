# Documento de Requisitos y Especificaciones - QuizDev

## A. Descripción del proyecto

- **Nombre:** QuizDev
- **Objetivo general:** Desarrollar una aplicación móvil nativa en Android para evaluar y reforzar los conocimientos de estudiantes y profesionales en desarrollo de software sobre **Kotlin**, **Android** y **Jetpack Compose** mediante un sistema de trivias interactivo, permitiendo además la gestión de preguntas personalizadas y el seguimiento del historial de partidas.
- **Descripción de su funcionamiento:** 
  La aplicación inicia con una pantalla de bienvenida (Inicio) que permite acceder al juego, ver el historial o crear nuevas preguntas. El usuario selecciona una categoría de estudio (Kotlin, Android, Jetpack Compose) y se enfrenta a un cuestionario de opción múltiple cargado desde un repositorio local. Al finalizar la partida, se muestra un resumen con la puntuación obtenida. Asimismo, los usuarios pueden añadir nuevas preguntas mediante un formulario dedicado que persiste localmente, y consultar estadísticas previas en el historial.
- **Alcance de la aplicación:** 
  La aplicación funciona de manera completamente local y offline. Incluye un conjunto inicial de preguntas semilla (en formato JSON de solo lectura), persistencia local mediante DataStore para el historial y preguntas personalizadas, gestión de partidas con temporizador o límite de preguntas, y una interfaz moderna basada en Material 3 con navegación fluida entre cuatro pantallas principales. No requiere backend remoto, APIs externas, bases de datos relacionales pesadas (SQLite/Room) ni conexión a internet.
- **Usuarios a quienes está dirigida:** 
  Estudiantes universitarios de ingeniería de software/sistemas, entusiastas de la programación móvil y desarrolladores que deseen autoevaluar sus conocimientos técnicos en Kotlin, Android y Jetpack Compose de forma rápida y didáctica.

---

## B. Requisitos funcionales (RF1 - RF12)

| ID | Nombre | Descripción | Criterio Verificable de Aceptación |
|---|---|---|---|
| **RF1** | Pantalla de Inicio | La aplicación debe mostrar una pantalla principal con el título de la aplicación, el logotipo y opciones de navegación accesibles para iniciar juego, ver historial y crear preguntas. | Al abrir la app se visualiza la pantalla de inicio con botones funcionales hacia las vistas correspondientes. |
| **RF2** | Selección de Categoría | El usuario debe poder seleccionar una categoría temática antes de iniciar la partida (Ej. Kotlin, Android, Jetpack Compose, o Mixto). | Al pulsar sobre una categoría, la partida se inicializa cargando exclusivamente preguntas asociadas a dicha temática. |
| **RF3** | Carga de Preguntas Semilla | El sistema debe cargar un conjunto inicial de preguntas predefinidas desde un archivo JSON estático local integrado en los assets o recursos de la app. | Al iniciar la app por primera vez, el sistema lee correctamente el JSON semilla y lo disponibiliza en memoria/repositorio. |
| **RF4** | Visualización de Preguntas | Durante la partida, la pantalla de preguntas debe mostrar claramente el enunciado de la pregunta, las 4 opciones de respuesta y el indicador de progreso actual (ej. Pregunta 3 de 10). | Cada pregunta muestra de forma legible su texto y botones interactivos para cada una de las opciones. |
| **RF5** | Selección y Validación de Respuesta | El usuario debe seleccionar una única opción por pregunta y confirmar o pasar a la siguiente. El sistema validará si es correcta o incorrecta. | Al seleccionar una opción, el sistema registra la respuesta y avanza a la siguiente pregunta tras un feedback visual opcional. |
| **RF6** | Cálculo y Cómputo de Puntuación | El sistema debe calcular el puntaje total de la partida basado en las respuestas correctas acumuladas y el tiempo o dificultad si aplica. | Al responder todas las preguntas, el puntaje final se calcula correctamente y se transfiere a la pantalla de resultados. |
| **RF7** | Pantalla de Partida Terminada | Al finalizar el cuestionario, se debe presentar una pantalla de resultados mostrando la puntuación obtenida, aciertos, errores y opciones para reintentar o volver al inicio. | La pantalla muestra el resumen exacto de la partida recién concluida y botones de navegación de retorno. |
| **RF8** | Persistencia de Partida en Historial | Cada vez que finaliza una partida, el resultado debe guardarse automáticamente en el almacenamiento local para su consulta histórica. | Al finalizar una partida, el registro aparece listado en la sección de historial de partidas. |
| **RF9** | Creación de Preguntas Personalizadas | La aplicación debe proveer un formulario para que el usuario ingrese nuevas preguntas (enunciado, 4 opciones, índice correcto, categoría y dificultad). | El formulario valida que los campos no estén vacíos y que se seleccione una opción correcta antes de guardar. |
| **RF10** | Persistencia de Preguntas Personalizadas | Las preguntas creadas por el usuario deben persistir en el dispositivo mediante DataStore tras cerrar y reabrir la aplicación. | Tras crear una pregunta personalizada, reiniciar la app y jugar, la nueva pregunta aparece disponible en su categoría. |
| **RF11** | Consulta del Historial de Partidas | El usuario debe poder acceder a una vista de historial que liste las partidas anteriores con su fecha, puntaje y categoría. | La pantalla de historial lista cronológicamente las partidas guardadas con sus detalles principales. |
| **RF12** | Navegación entre Pantallas | La aplicación debe permitir navegar de forma intuitiva entre las cuatro pantallas principales (Inicio, Pregunta, Partida Terminada, Nueva Pregunta e Historial) sin perder el estado de navegación. | Los botones de acción y retroceso permiten fluidez y consistencia en la navegación en todo momento. |

---

## C. Requisitos no funcionales

### Requisitos Obligatorios del Docente
1. **Lenguaje:** Desarrollado íntegramente en **Kotlin** idiomático.
2. **Interfaz de Usuario:** Construido con **Jetpack Compose** y diseño **Material 3**.
3. **Arquitectura:** Aplicación estructurada bajo el patrón **MVVM (Model-View-ViewModel)** y **Screaming Architecture** (organización de carpetas por dominios/funcionalidades).
4. **Reactividad:** Uso de **StateFlow** para la gestión reactiva del estado entre ViewModel y Composes.
5. **Persistencia Local:** Almacenamiento local mediante **DataStore** (o alternativas permitidas, excluyendo estrictamente SQLite y Room).
6. **Restricciones de Conectividad y APIs:** Prohibido el uso de APIs externas, servicios remotos, SQLite y Room. Operación 100% offline.

### Decisiones de Implementación Propuestas
7. **Arquitectura de Datos (Repository Pattern):** Abstracción de fuentes de datos (JSON semilla de solo lectura + DataStore para datos mutables) a través de repositorios unificados.
8. **Mantenibilidad y Limpieza:** Código modular, comentado adecuadamente, sin clases vacías innecesarias ni dependencias externas redundantes.
9. **Accesibilidad Básica:** Tamaños de fuente escalables, contrastes adecuados acordes a Material 3 y componentes interactivos con tamaños táctiles óptimos.
10. **Pruebas:** Preparación de pruebas unitarias básicas para los ViewModels y pruebas instrumentadas de UI según el avance del proyecto.

---

## D. Modelo de datos

### 1. `Question` (Entidad de Pregunta)
Representa una pregunta de la trivia.

| Campo | Tipo | Finalidad | Validaciones Básicas |
|---|---|---|---|
| `id` | `String` (o `Long`) | Identificador único de la pregunta. | No nulo, único. |
| `text` | `String` | Enunciado de la pregunta. | No vacío, longitud mínima de 10 caracteres. |
| `options` | `List<String>` | Lista de 4 opciones de respuesta posibles. | Exactamente 4 elementos, ninguno vacío. |
| `correctIndex` | `Int` | Índice (0 a 3) de la respuesta correcta dentro de `options`. | Rango válido entre `0` y `3`. |
| `category` | `String` | Categoría temática (ej. `"Kotlin"`, `"Android"`, `"Jetpack Compose"`). | No vacía, pertenecer a categorías permitidas. |
| `difficulty` | `String` | Nivel de dificultad (`"Fácil"`, `"Medio"`, `"Difícil"`). | Valores predefinidos válidos. |

### 2. `GameResult` (Entidad de Resultado de Partida)
Registra el resultado de una partida completada por el usuario.

| Campo | Tipo | Finalidad | Validaciones Básicas |
|---|---|---|---|
| `id` | `String` | Identificador único del resultado. | No nulo. |
| `date` | `Long` (Timestamp) | Fecha y hora en que se completó la partida. | Mayor a 0. |
| `score` | `Int` | Puntuación obtenida (ej. cantidad de aciertos o puntos totales). | `>= 0`. |
| `total` | `Int` | Número total de preguntas respondidas en la partida. | `> 0`, `score <= total`. |
| `category` | `String` | Categoría jugada. | No vacía. |
| `correctAnswers`| `Int` | Cantidad de aciertos logrados. | `>= 0`, `<= total`. |

---

## E. Estrategia de almacenamiento

La estrategia de persistencia combina dos enfoques locales optimizados para las restricciones del proyecto:

1. **Archivo JSON Local (Semilla de solo lectura):**
   - Se incluirá un archivo estático en los recursos (`res/raw` o `assets`) que contendrá el banco inicial de preguntas sobre Kotlin, Android y Jetpack Compose.
   - Este archivo es de **solo lectura** y sirve para garantizar que el usuario tenga contenido disponible inmediatamente al instalar y abrir la aplicación por primera vez.

2. **DataStore (Preferencias o Proto DataStore):**
   - Utilizado para almacenar de forma persistente y local:
     - Las **preguntas personalizadas** creadas por el usuario en la aplicación.
     - El **historial de partidas** completadas (`List<GameResult>`).
     - Configuraciones o récords locales (ej. mejor puntuación).
   - Los datos creados por el usuario se serializan (por ejemplo, usando Kotlinx Serialization a JSON) y se guardan en DataStore, garantizando que persistan al cerrar y reabrir la aplicación.

3. **Patrón Repositorio (`Repository`):**
   - Se implementará una capa de repositorio (`QuestionRepository`, `GameResultRepository`) que abstrae el origen de los datos. El ViewModel consultará al repositorio sin importar si los datos provienen del JSON semilla inicial o del almacenamiento local en DataStore, cumpliendo con la separación de incumbencias de MVVM.

---

## F. Arquitectura preliminar

### 1. Responsabilidades de las Capas

- **Presentation:** 
  - Contiene las pantallas construidas con Jetpack Compose (UI), componentes visuales reutilizables, temas de Material 3 y los `ViewModel`.
  - El `ViewModel` gestiona el estado de la UI mediante `StateFlow`, procesa los eventos de usuario y expone estados inmutables (`StateFlow<UiState>`).
- **Domain / Business Rules:** 
  - Contiene la lógica de negocio pura (validación de respuestas, cálculo de puntuación, filtrado de categorías) independiente de la plataforma Android.
- **Data (Repository & Storage):** 
  - Administra las fuentes de datos (JSON estático y DataStore). Convierte los datos brutos en modelos de dominio y viceversa.

### 2. Flujo de Datos Reactivo

```text
[ Acción del Usuario ] 
        │ (Click / Input)
        ▼
[ Interfaz (Jetpack Compose) ] 
        │ (Llama a función del ViewModel)
        ▼
[ ViewModel ] 
        │ (Actualiza / Solicita datos)
        ▼
[ Repository ] 
        │ (Lee/Escribe DataStore o JSON)
        ▼
[ Almacenamiento Local (DataStore / JSON) ]
        │ (Retorna datos)
        ▼
[ Repository ] 
        │ (Retorna resultado)
        ▼
[ ViewModel ] 
        │ (Emite nuevo estado vía StateFlow)
        ▼
[ Interfaz (Jetpack Compose) ] 
        │ (Observa StateFlow & Recompone UI)
```

### 3. Organización mediante Screaming Architecture
El paquete raíz `com.example.quizdev_moviles` se organizará por dominios funcionales en lugar de capas técnicas horizontales:

```text
com.example.quizdev_moviles/
├── core/                  # Utilidades globales, temas Material 3, extensiones
├── game/                  # Dominio de Partida y Preguntas (Pantalla 2 y 3)
│   ├── data/              # Repositorios, fuentes de datos (DataStore, JSON)
│   ├── domain/            # Modelos (Question, GameResult), casos de uso
│   └── ui/                # Pantalla de Pregunta, Partida Terminada y ViewModel
├── home/                  # Dominio de Inicio (Pantalla 1)
│   └── ui/                # Pantalla de Inicio y ViewModel
├── history/               # Dominio de Historial de Partidas
│   ├── data/              # Repositorios de historial
│   ├── domain/            # Modelos de historial
│   └── ui/                # Pantalla de Historial y ViewModel
└── questioncreator/       # Dominio de Creación de Preguntas (Pantalla 4)
    ├── data/              # Repositorio de preguntas creadas
    ├── domain/            # Lógica de validación
    └── ui/                # Pantalla de Nueva Pregunta y ViewModel
```

---

## G. Alcance de las siguientes fases

- **Fase 2 (Preparación de Mockups, Estructura y Arquitectura Preliminar):**
  - Configuración de la estructura de paquetes (Screaming Architecture).
  - Implementación de los modelos de datos base (`Question`, `GameResult`).
  - Definición de navegación preliminar entre las cuatro pantallas basadas en los mockups existentes (Inicio, Pregunta, Partida Terminada, Nueva Pregunta e Historial).
  - Creación de componentes base de interfaz con Material 3.

- **Fase 3 (Implementación Funcional, Persistencia, Pruebas y Defensa):**
  - Implementación de la carga de preguntas desde el JSON semilla y DataStore.
  - Conexión completa de los `ViewModel` con `StateFlow`.
  - Lógica de juego, selección de respuestas y cálculo de puntuación.
  - Persistencia de preguntas personalizadas y registro de partidas en historial.
  - Pruebas unitarias e instrumentadas básicas.
  - Elaboración del reporte técnico final y defensa del proyecto.
