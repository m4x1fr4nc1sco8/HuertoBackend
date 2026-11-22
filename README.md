# Huerto Escolar - API (microservicio)

Microservicio REST en **Spring Boot** que gestiona los datos del huerto escolar (sectores, cultivos con su tarea y usuarios) y los entrega a la aplicación Android **HuertoEscolar**. Corresponde a la Evaluación Parcial 4 de DSY1105 Desarrollo de Aplicaciones Móviles (Duoc UC).

- Aplicación Android: https://github.com/m4x1fr4nc1sco8/HuertoEscolar
- Autor: Maximiliano

## Tecnologías

- Java 21 y Spring Boot (Web, Validation, Data JPA)
- MySQL / MariaDB (XAMPP)
- Maven Wrapper (`mvnw`)
- Swagger UI (springdoc-openapi) para documentar y probar la API
- Lombok

## Requisitos

- JDK 21
- XAMPP con MySQL (puerto 3306, usuario `root` sin contraseña)
- Puerto 8080 libre

## Cómo ejecutarlo

1. Abre XAMPP e inicia **MySQL** (y Apache si vas a usar phpMyAdmin).
2. Crea la base de datos importando el script `database/huerto_escolar.sql`:
   - Entra a `http://localhost/phpmyadmin`.
   - Pestaña **Importar**, elige `database/huerto_escolar.sql` y pulsa **Importar** (o pega el contenido en la pestaña **SQL**).
   - El script ejecuta `DROP DATABASE IF EXISTS` y vuelve a crear `huerto_escolar`: **cada ejecución borra los datos existentes**.
3. Inicia el microservicio desde la raíz del proyecto:

   ```powershell
   .\mvnw spring-boot:run
   ```

   También puedes ejecutar la clase `HuertoApiApplication` desde IntelliJ IDEA.
4. Abre Swagger UI: http://localhost:8080/swagger-ui.html

## Base de datos

- Nombre: `huerto_escolar`
- Modelo normalizado en 3FN: los estados y roles viven en tablas de catálogo (`rol`, `estado_sector`, `estado_cultivo`, entre otras) y las demás tablas se relacionan por clave foránea.
- Las tablas las crea el script SQL. Hibernate no las modifica (`spring.jpa.hibernate.ddl-auto=none`).
- Configuración en `src/main/resources/application.properties`.

## Endpoints

Todos los endpoints están documentados y se pueden probar en Swagger UI.

### Sectores (`/sectores`)

| Método | Ruta | Descripción | Respuestas |
|--------|------|-------------|------------|
| GET | `/sectores` | Listar todos los sectores | 200 |
| GET | `/sectores/{id}` | Obtener un sector por id | 200, 404 si no existe |
| POST | `/sectores` | Crear un sector | 201, 409 si el código ya existe |
| PUT | `/sectores/{id}` | Actualizar un sector | 200, 404 si no existe, 409 si el código es de otro sector |
| DELETE | `/sectores/{id}` | Eliminar un sector | 204, 409 si tiene cultivos asociados |

### Cultivos (`/cultivos`)

| Método | Ruta | Descripción | Respuestas |
|--------|------|-------------|------------|
| GET | `/cultivos` | Listar cultivos. Filtros opcionales: `estudianteId`, `sectorId` | 200 |
| GET | `/cultivos/{id}` | Obtener un cultivo por id | 200, 404 si no existe |
| POST | `/cultivos` | Crear un cultivo (la tarea parte en `PENDIENTE`) | 201 |
| PUT | `/cultivos/{id}` | Actualizar los datos de un cultivo | 200 |
| DELETE | `/cultivos/{id}` | Eliminar un cultivo | 204 |
| PATCH | `/cultivos/{id}/estado` | Cambiar el estado de desarrollo del cultivo (administrador) | 200 |
| POST | `/cultivos/{id}/tarea/enviar` | Estudiante: enviar la tarea con foto y comentario (queda `PENDIENTE_DE_VALIDACION`) | 200 |
| POST | `/cultivos/{id}/tarea/validar` | Administrador: validar la tarea enviada (`VALIDADA`) | 200 |
| POST | `/cultivos/{id}/tarea/rechazar` | Administrador: rechazar la tarea con un motivo (vuelve a `PENDIENTE`) | 200 |

### Usuarios (`/usuarios`)

| Método | Ruta | Descripción | Respuestas |
|--------|------|-------------|------------|
| GET | `/usuarios` | Listar usuarios. Filtro opcional: `rol=ESTUDIANTE` o `rol=ADMINISTRADOR` | 200 |
| GET | `/usuarios/{id}` | Obtener un usuario por id | 200, 404 si no existe |
| POST | `/usuarios` | Registrar un usuario | 201, 409 si el correo ya existe |

## Flujo de la tarea de un cultivo

```
PENDIENTE --enviar (estudiante)--> PENDIENTE_DE_VALIDACION --validar (administrador)--> VALIDADA
                  ^                                  |
                  +-------- rechazar (con motivo) ---+
```

## Estructura del proyecto

```
src/main/java/cl/duoc/huertoapi
├── config        Configuración de OpenAPI / Swagger
├── controller    Controladores REST (Sector, Cultivo, Usuario)
├── dto           Request y Response de cada recurso
├── exception     ApiException, ErrorResponse y GlobalExceptionHandler
├── model         Entidades JPA y catálogos
├── repository    Repositorios Spring Data
└── service       Lógica de negocio
database/
└── huerto_escolar.sql   Script de creación de la base de datos
```

## Conexión con la aplicación Android

Desde el emulador de Android Studio, la app accede al microservicio en `http://10.0.2.2:8080/`, que es la dirección del equipo anfitrión vista desde el emulador.
