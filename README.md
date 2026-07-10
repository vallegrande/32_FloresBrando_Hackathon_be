# Matrícula Cloud 360 - Backend

Backend completo para el sistema de matrículas del Instituto Superior EduFuturo, desarrollado con Spring Boot 3.5.16 y SQL Server 2022.

## Características Implementadas

### ✅ Requisitos de la Hackathon Cumplidos

1. **CRUD Funcional** - Controladores completos para todas las entidades:
   - Students (Estudiantes)
   - Career (Carreras/Programas)
   - Courses (Cursos)
   - Teachers (Docentes)
   - Promoter (Promotores)
   - Locations (Sedes/Campus)
   - Enrollment (Matrículas)
   - Ubigeo (Ubicaciones geográficas)

2. **Campos de Auditoría** - 4 campos de fecha/hora en todas las entidades:
   - `created_at` - Fecha de creación
   - `updated_at` - Fecha de última actualización
   - `deleted_at` - Fecha de eliminación lógica (soft delete)

3. **Soft Delete (Eliminación Lógica)** - Implementado en todas las entidades:
   - Los registros no se eliminan físicamente
   - Se marca con `deleted_at` cuando se elimina
   - Endpoint `PUT /api/{entidad}/{id}/restore` para restaurar
   - Las consultas filtran automáticamente registros eliminados

4. **Validación de Negocio** - Reglas del caso implementadas:
   - Un estudiante no puede matricularse dos veces en el mismo periodo
   - Validación de email y DNI únicos por entidad
   - Toda matrícula requiere promotor y sede obligatorios

5. **Importación/Exportación de Datos**:
   - Importación de estudiantes desde Excel
   - Exportación de estudiantes a Excel
   - Exportación de estudiantes a PDF

6. **Procedimientos Almacenados** (en script SQL):
   - `sp_RegisterEnrollment` - Registra matrícula con validaciones
   - `sp_SoftDelete` - Eliminación lógica genérica
   - `sp_Restore` - Restauración de registros

7. **CORS Configurado** - Todos los controladores configurados para Angular:
   - Origen: `http://localhost:4200`
   - Habilitado para todas las rutas

## Tecnologías

- Java 25
- Spring Boot 3.5.16
- Spring Data JPA
- Spring Security (configurado para permitir todas las peticiones)
- SQL Server 2022
- Lombok
- Apache POI (Excel)
- iText (PDF)
- OpenAPI/Swagger
- Docker

## Estructura del Proyecto

```
src/main/java/com/example/demo/
├── config/
│   └── SecurityConfig.java
├── controller/
│   ├── CareerController.java
│   ├── CoursesController.java
│   ├── EnrollmentController.java
│   ├── LocationsController.java
│   ├── PromoterController.java
│   ├── StudentsController.java
│   ├── TeachersController.java
│   └── UbigeoController.java
├── entity/
│   ├── Auditable.java
│   ├── Career.java
│   ├── Courses.java
│   ├── Enrollment.java
│   ├── Locations.java
│   ├── Promoter.java
│   ├── Students.java
│   ├── Teachers.java
│   └── Ubigeo.java
├── repository/
│   ├── CareerRepository.java
│   ├── CoursesRepository.java
│   ├── EnrollmentRepository.java
│   ├── LocationsRepository.java
│   ├── PromoterRepository.java
│   ├── StudentsRepository.java
│   ├── TeachersRepository.java
│   └── UbigeoRepository.java
└── service/
    ├── CareerService.java
    ├── CoursesService.java
    ├── EnrollmentService.java
    ├── LocationsService.java
    ├── PromoterService.java
    ├── StudentsService.java
    ├── TeachersService.java
    └── UbigeoService.java
```

## Configuración de Base de Datos

### Ejecutar el Script SQL

El script `database-script.sql` crea automáticamente:
- Base de datos `EduFuturo`
- Todas las tablas con restricciones
- Procedimientos almacenados
- Datos de ejemplo

**En SQL Server Management Studio:**
1. Abrir el archivo `database-script.sql`
2. Seleccionar todo el contenido (Ctrl+A)
3. Ejecutar (F5)

**O desde línea de comandos:**
```bash
sqlcmd -S localhost -U sa -P TuPassword -i database-script.sql
```

### Configurar Variables de Entorno

```bash
export DATABASE_URL=jdbc:sqlserver://tu-host:1433;databaseName=EduFuturo;encrypt=true;trustServerCertificate=true
export DATABASE_USERNAME=sa
export DATABASE_PASSWORD=TuPassword
```

## Ejecución Local

### Prerrequisitos
- Java 25
- Maven 3.6+
- SQL Server 2022

### Ejecutar con Maven
```bash
./mvnw spring-boot:run
```

### Ejecutar el JAR compilado
```bash
./mvnw clean package
java -jar target/demo-0.0.1-SNAPSHOT.jar
```

## Despliegue con Docker

### Construir la Imagen
```bash
docker build -t matricula-cloud-360:latest .
```

### Ejecutar el Contenedor Localmente
```bash
docker run -d \
  -p 8080:8080 \
  -e DATABASE_URL=jdbc:sqlserver://host.docker.internal:1433;databaseName=EduFuturo;encrypt=true;trustServerCertificate=true \
  -e DATABASE_USERNAME=sa \
  -e DATABASE_PASSWORD=TuPassword \
  matricula-cloud-360:latest
```

### Subir a DockerHub
```bash
docker tag matricula-cloud-360:latest tu-usuario/matricula-cloud-360:latest
docker push tu-usuario/matricula-cloud-360:latest
```

## Despliegue en AWS

### 1. Preparar la Instancia EC2
- Crear instancia EC2 en tu VPC
- Instalar Docker: `sudo yum install docker -y`
- Iniciar Docker: `sudo service docker start`

### 2. Ejecutar en AWS
```bash
docker run -d \
  -p 8080:8080 \
  -e DATABASE_URL=jdbc:sqlserver://tu-rds-endpoint:1433;databaseName=EduFuturo;encrypt=true;trustServerCertificate=true \
  -e DATABASE_USERNAME=sa \
  -e DATABASE_PASSWORD=TuPassword \
  tu-usuario/matricula-cloud-360:latest
```

### 3. Configurar Security Group
- Permitir tráfico entrante en puerto 8080 desde tu IP o VPC

## API Endpoints

### Estudiantes
- `GET /api/students` - Listar todos
- `GET /api/students/{id}` - Obtener por ID
- `POST /api/students` - Crear
- `PUT /api/students/{id}` - Actualizar
- `DELETE /api/students/{id}` - Eliminar (soft delete)
- `PUT /api/students/{id}/restore` - Restaurar
- `POST /api/students/import` - Importar desde Excel
- `GET /api/students/export/excel` - Exportar a Excel
- `GET /api/students/export/pdf` - Exportar a PDF

### Carreras
- `GET /api/careers` - Listar todos
- `GET /api/careers/{id}` - Obtener por ID
- `POST /api/careers` - Crear
- `PUT /api/careers/{id}` - Actualizar
- `DELETE /api/careers/{id}` - Eliminar (soft delete)
- `PUT /api/careers/{id}/restore` - Restaurar

### Cursos
- `GET /api/courses` - Listar todos
- `GET /api/courses/{id}` - Obtener por ID
- `GET /api/courses/career/{careerId}` - Listar por carrera
- `POST /api/courses` - Crear
- `PUT /api/courses/{id}` - Actualizar
- `DELETE /api/courses/{id}` - Eliminar (soft delete)
- `PUT /api/courses/{id}/restore` - Restaurar

### Docentes
- `GET /api/teachers` - Listar todos
- `GET /api/teachers/{id}` - Obtener por ID
- `POST /api/teachers` - Crear
- `PUT /api/teachers/{id}` - Actualizar
- `DELETE /api/teachers/{id}` - Eliminar (soft delete)
- `PUT /api/teachers/{id}/restore` - Restaurar

### Promotores
- `GET /api/promoters` - Listar todos
- `GET /api/promoters/{id}` - Obtener por ID
- `POST /api/promoters` - Crear
- `PUT /api/promoters/{id}` - Actualizar
- `DELETE /api/promoters/{id}` - Eliminar (soft delete)
- `PUT /api/promoters/{id}/restore` - Restaurar

### Sedes
- `GET /api/locations` - Listar todos
- `GET /api/locations/{id}` - Obtener por ID
- `POST /api/locations` - Crear
- `PUT /api/locations/{id}` - Actualizar
- `DELETE /api/locations/{id}` - Eliminar (soft delete)
- `PUT /api/locations/{id}/restore` - Restaurar

### Matrículas
- `GET /api/enrollments` - Listar todos
- `GET /api/enrollments/{id}` - Obtener por ID
- `GET /api/enrollments/student/{studentId}` - Listar por estudiante
- `POST /api/enrollments` - Crear (valida matrícula única)
- `PUT /api/enrollments/{id}` - Actualizar
- `DELETE /api/enrollments/{id}` - Eliminar (soft delete)
- `PUT /api/enrollments/{id}/restore` - Restaurar

### Ubigeo
- `GET /api/ubigeo` - Listar todos
- `GET /api/ubigeo/{id}` - Obtener por ID
- `POST /api/ubigeo` - Crear
- `PUT /api/ubigeo/{id}` - Actualizar
- `DELETE /api/ubigeo/{id}` - Eliminar (soft delete)
- `PUT /api/ubigeo/{id}/restore` - Restaurar

## Health Check

El backend expone endpoints de monitoreo:
- `GET /actuator/health` - Estado de salud de la aplicación
- `GET /actuator/info` - Información de la aplicación
- `GET /actuator/metrics` - Métricas de la aplicación

## Documentación Swagger

Accede a la documentación de la API en:
```
http://localhost:8080/swagger-ui.html
```

## Reglas de Negocio Implementadas

1. **Matrícula Única**: Un estudiante no puede tener más de una matrícula activa simultáneamente
2. **Promotor y Sede Obligatorios**: Toda matrícula debe tener un promotor y una sede asignados
3. **Email y DNI Únicos**: Validación de unicidad para estudiantes, docentes y promotores
4. **Soft Delete**: Ningún registro se elimina físicamente, solo se marca como inactivo

## Datos de Ejemplo

El script SQL incluye:
- 5 ubigeos (Lima, Arequipa, Cusco)
- 5 carreras
- 5 sedes
- 5 docentes
- 3 promotores
- 5 estudiantes
- 20 cursos (relacionados con carreras y docentes según el ejemplo)
- 3 matrículas de ejemplo
