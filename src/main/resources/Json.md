## 📋 Endpoints para Postman
Aquí tienes todos los endpoints para testear, con ejemplos de JSON y URLs:

### 📌 Students (Tu Tabla Maestra) Base URL: http://localhost:8080/api/students
1. GET - Listar todos los estudiantes activos :
   
   - Método: GET
   - URL: http://localhost:8080/api/students
2. GET - Obtener estudiante por ID :
   
   - Método: GET
   - URL: http://localhost:8080/api/students/1
3. POST - Crear estudiante :
   
   - Método: POST
   - URL: http://localhost:8080/api/students
   - Body (JSON):

   {
  "firstName": "Pedro",
  "lastName": "García",
  "career": { "idCareer": 1 },
  "documentType": "DNI",
  "numerDoc": "12345678",
  "email": "pedro.garcia@email.com",
  "phone": "987654321",
  "adress": "Av. Ejemplo 456",
  "ubigeo": { "idUbigeo": "150101" }
}

4. PUT - Editar estudiante :
   
   - Método: PUT
   - URL: http://localhost:8080/api/students/1
   - Body (JSON):

   {
  "firstName": "Juan",
  "lastName": "Pérez López",
  "career": { "idCareer": 1 },
  "documentType": "DNI",
  "numerDoc": "77665544",
  "email": "juan.perez.lopez@email.com",
  "phone": "999888777",
  "adress": "Av. Las Flores 123",
  "ubigeo": { "idUbigeo": "150142" }
}
5. DELETE - Eliminar (desactivar) estudiante :
   
   - Método: DELETE
   - URL: http://localhost:8080/api/students/1
6. PUT - Restaurar (activar) estudiante :
   
   - Método: PUT
   - URL: http://localhost:8080/api/students/1/restore
7. PUT - Cambiar estado de estudiante :
   
   - Método: PUT
   - URL: http://localhost:8080/api/students/1/status?status=false (desactivar)
   - URL: http://localhost:8080/api/students/1/status?status=true (activar)
### 📌 Enrollment (Tabla Transaccional - Cabecera) Base URL: http://localhost:8080/api/enrollments
1. GET - Listar todas las matrículas activas :
   
   - Método: GET
   - URL: http://localhost:8080/api/enrollments
2. GET - Obtener matrícula por ID :
   
   - Método: GET
   - URL: http://localhost:8080/api/enrollments/1
3. GET - Obtener matrículas por estudiante :
   
   - Método: GET
   - URL: http://localhost:8080/api/enrollments/student/1
4. POST - Crear matrícula :
   
   - Método: POST
   - URL: http://localhost:8080/api/enrollments
   - Body (JSON):

{
  "student": { "idStudents": 2 },
  "locations": { "idLocations": 1 },
  "promoter": { "idPromoter": 1 },
  "amount": 400.00
}

5. PUT - Editar matrícula :
   
   - Método: PUT
   - URL: http://localhost:8080/api/enrollments/1
   - Body (JSON):

   {
  "student": { "idStudents": 1 },
  "locations": { "idLocations": 1 },
  "promoter": { "idPromoter": 1 },
  "amount": 380.00
}
6. DELETE - Eliminar (desactivar) matrícula :
   
   - Método: DELETE
   - URL: http://localhost:8080/api/enrollments/1
7. PUT - Restaurar (activar) matrícula :
   
   - Método: PUT
   - URL: http://localhost:8080/api/enrollments/1/restore
8. PUT - Cambiar estado de matrícula :
   
   - Método: PUT
   - URL: http://localhost:8080/api/enrollments/1/status?status=false
### 📌 EnrollmentDetail (Tabla Transaccional - Detalle) Base URL: http://localhost:8080/api/enrollment-detail
1. GET - Listar todos los detalles activos :
   
   - Método: GET
   - URL: http://localhost:8080/api/enrollment-detail
2. GET - Obtener detalle por ID :
   
   - Método: GET
   - URL: http://localhost:8080/api/enrollment-detail/1
3. GET - Obtener detalles por matrícula :
   
   - Método: GET
   - URL: http://localhost:8080/api/enrollment-detail/enrollment/1
4. POST - Crear detalle de matrícula :
   
   - Método: POST
   - URL: http://localhost:8080/api/enrollment-detail
   - Body (JSON):

   {
  "enrollment": { "idEnrollment": 1 },
  "courses": { "idCourses": 3 }
}
5. PUT - Editar detalle :
   
   - Método: PUT
   - URL: http://localhost:8080/api/enrollment-detail/1
   - Body (JSON):

   {
  "enrollment": { "idEnrollment": 1 },
  "courses": { "idCourses": 2 }
}
6. DELETE - Eliminar (desactivar) detalle :
   
   - Método: DELETE
   - URL: http://localhost:8080/api/enrollment-detail/1
7. PUT - Restaurar (activar) detalle :
   
   - Método: PUT
   - URL: http://localhost:8080/api/enrollment-detail/1/restore
8. PUT - Cambiar estado de detalle :
   
   - Método: PUT
   - URL: http://localhost:8080/api/enrollment-detail/1/status?status=false