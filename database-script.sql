USE master;
GO
IF EXISTS (SELECT name FROM sys.databases WHERE name = 'EduFuturo')
BEGIN
   ALTER DATABASE EduFuturo SET SINGLE_USER WITH ROLLBACK IMMEDIATE;
   DROP DATABASE EduFuturo;
END;
GO
CREATE DATABASE EduFuturo;
GO
USE EduFuturo;
GO

-- =============================================
-- TABLA: ubigeo (No necesita estado, son datos geográficos fijos)
-- =============================================
CREATE TABLE ubigeo (
   id_ubigeo VARCHAR(6) NOT NULL,
   department VARCHAR(50) NOT NULL,
   province VARCHAR(50) NOT NULL,
   district VARCHAR(50) NOT NULL,
   zone VARCHAR(200),
   created_at DATETIME2 NOT NULL DEFAULT GETDATE(),
   updated_at DATETIME2,
   CONSTRAINT pk_ubigeo PRIMARY KEY (id_ubigeo)
);

-- =============================================
-- TABLA: career
-- =============================================
CREATE TABLE career (
   id_career INT IDENTITY(1,1) NOT NULL,
   name VARCHAR(100) NOT NULL,
   cycle_duration INT NOT NULL,
   description VARCHAR(200),
   created_at DATETIME2 NOT NULL DEFAULT GETDATE(),
   updated_at DATETIME2,
   status BIT NOT NULL DEFAULT 1, -- <-- Para eliminado lógico
   CONSTRAINT pk_career PRIMARY KEY (id_career),
   CONSTRAINT uq_career_name UNIQUE (name)
);

-- =============================================
-- TABLA: students
-- =============================================
CREATE TABLE students (
   id_students INT IDENTITY(1,1) NOT NULL,
   first_name VARCHAR(50) NOT NULL,
   last_name VARCHAR(50) NOT NULL,
   id_career INT NOT NULL,
   document_type VARCHAR(10) NOT NULL,
   numer_doc VARCHAR(20) NOT NULL,
   email VARCHAR(100) NOT NULL,
   phone VARCHAR(15),
   adress VARCHAR(200),
   id_ubigeo VARCHAR(6) NOT NULL,
   created_at DATETIME2 NOT NULL DEFAULT GETDATE(),
   updated_at DATETIME2,
   status BIT NOT NULL DEFAULT 1, -- <-- Para dar de baja al alumno
   CONSTRAINT pk_students PRIMARY KEY (id_students),
   CONSTRAINT uq_students_email UNIQUE (email),
   CONSTRAINT uq_students_doc UNIQUE (numer_doc),
   CONSTRAINT fk_students_career FOREIGN KEY (id_career) REFERENCES career(id_career),
   CONSTRAINT fk_students_ubigeo FOREIGN KEY (id_ubigeo) REFERENCES ubigeo(id_ubigeo)
);

-- =============================================
-- TABLA: locations
-- =============================================
CREATE TABLE locations (
   id_locations INT IDENTITY(1,1) NOT NULL,
   name_locations VARCHAR(100) NOT NULL,
   city VARCHAR(50) NOT NULL,
   id_ubigeo VARCHAR(6) NOT NULL,
   created_at DATETIME2 NOT NULL DEFAULT GETDATE(),
   updated_at DATETIME2,
   status BIT NOT NULL DEFAULT 1, -- <-- Para inactivar una sede
   CONSTRAINT pk_locations PRIMARY KEY (id_locations),
   CONSTRAINT fk_locations_ubigeo FOREIGN KEY (id_ubigeo) REFERENCES ubigeo(id_ubigeo)
);

-- =============================================
-- TABLA: teachers
-- =============================================
CREATE TABLE teachers (
   id_teacher INT IDENTITY(1,1) NOT NULL,
   first_name VARCHAR(50) NOT NULL,
   last_name VARCHAR(50) NOT NULL,
   specialty VARCHAR(100) NOT NULL,
   document_type VARCHAR(10) NOT NULL,
   numer_doc VARCHAR(20) NOT NULL,
   corporate_email VARCHAR(100) NOT NULL,
   phone VARCHAR(15),
   adress VARCHAR(200),
   id_ubigeo VARCHAR(6) NOT NULL,
   created_at DATETIME2 NOT NULL DEFAULT GETDATE(),
   updated_at DATETIME2,
   status BIT NOT NULL DEFAULT 1, -- <-- Para cesar a un profesor
   CONSTRAINT pk_teachers PRIMARY KEY (id_teacher),
   CONSTRAINT uq_teachers_email UNIQUE (corporate_email),
   CONSTRAINT uq_teachers_doc UNIQUE (numer_doc),
   CONSTRAINT fk_teachers_ubigeo FOREIGN KEY (id_ubigeo) REFERENCES ubigeo(id_ubigeo)
);

-- =============================================
-- TABLA: promoter
-- =============================================
CREATE TABLE promoter (
   id_promoter INT IDENTITY(1,1) NOT NULL,
   first_name VARCHAR(50) NOT NULL,
   last_name VARCHAR(50) NOT NULL,
   document_type VARCHAR(10) NOT NULL,
   numer_doc VARCHAR(20) NOT NULL,
   corporate_email VARCHAR(100) NOT NULL,
   phone VARCHAR(15),
   adress VARCHAR(200),
   id_locations INT NOT NULL,
   id_ubigeo VARCHAR(6) NOT NULL,
   created_at DATETIME2 NOT NULL DEFAULT GETDATE(),
   updated_at DATETIME2,
   status BIT NOT NULL DEFAULT 1, -- <-- Para inactivar al promotor
   CONSTRAINT pk_promoter PRIMARY KEY (id_promoter),
   CONSTRAINT fk_promoter_locations FOREIGN KEY (id_locations) REFERENCES locations(id_locations),
   CONSTRAINT fk_promoter_ubigeo FOREIGN KEY (id_ubigeo) REFERENCES ubigeo(id_ubigeo)
);

-- =============================================
-- TABLA: courses
-- =============================================
CREATE TABLE courses (
   id_courses INT IDENTITY(1,1) NOT NULL,
   name VARCHAR(100) NOT NULL,
   credits INT NOT NULL,
   id_teacher INT NOT NULL,
   id_career INT NOT NULL,
   created_at DATETIME2 NOT NULL DEFAULT GETDATE(),
   updated_at DATETIME2,
   status BIT NOT NULL DEFAULT 1, -- <-- Para deshabilitar un curso
   CONSTRAINT pk_courses PRIMARY KEY (id_courses),
   CONSTRAINT fk_courses_teacher FOREIGN KEY (id_teacher) REFERENCES teachers(id_teacher),
   CONSTRAINT fk_courses_career FOREIGN KEY (id_career) REFERENCES career(id_career)
);

-- =============================================
-- TABLA: enrollment (Cabecera)
-- =============================================
CREATE TABLE enrollment (
   id_enrollment INT IDENTITY(1,1) NOT NULL,
   id_student INT NOT NULL,
   id_locations INT NOT NULL,
   id_promoter INT NOT NULL,
   amount DECIMAL(10,2) NOT NULL,
   enrollment_date DATETIME2 NOT NULL DEFAULT GETDATE(),
   created_at DATETIME2 NOT NULL DEFAULT GETDATE(),
   updated_at DATETIME2,
   status BIT NOT NULL DEFAULT 1, -- <-- NUEVO: Para anular la matrícula completa
   CONSTRAINT pk_enrollment PRIMARY KEY (id_enrollment),
   CONSTRAINT fk_enrollment_student FOREIGN KEY (id_student) REFERENCES students(id_students),
   CONSTRAINT fk_enrollment_locations FOREIGN KEY (id_locations) REFERENCES locations(id_locations),
   CONSTRAINT fk_enrollment_promoter FOREIGN KEY (id_promoter) REFERENCES promoter(id_promoter)
);

-- =============================================
-- TABLA: enrollment_detail (Detalle)
-- =============================================
CREATE TABLE enrollment_detail (
   id_enrollment_detail INT IDENTITY(1,1) NOT NULL,
   id_enrollment INT NOT NULL,
   id_courses INT NOT NULL,
   created_at DATETIME2 NOT NULL DEFAULT GETDATE(),
   updated_at DATETIME2,
   status BIT NOT NULL DEFAULT 1, -- <-- NUEVO: Para retirar al alumno de un curso específico
   CONSTRAINT pk_enrollment_detail PRIMARY KEY (id_enrollment_detail),
   CONSTRAINT fk_detail_enrollment FOREIGN KEY (id_enrollment) REFERENCES enrollment(id_enrollment),
   CONSTRAINT fk_detail_courses FOREIGN KEY (id_courses) REFERENCES courses(id_courses),
   CONSTRAINT uq_enrollment_course UNIQUE (id_enrollment, id_courses)
);
GO

-- =============================================
-- DATOS DE PRUEBA (Exactamente los mismos)
-- =============================================
INSERT INTO ubigeo (id_ubigeo, department, province, district, zone) VALUES
('150101', 'Lima', 'Lima', 'Lima Cercado', 'Zona Centro'),
('150112', 'Lima', 'Lima', 'Independencia', 'Zona Norte'),
('150142', 'Lima', 'Lima', 'Los Olivos', 'Zona Norte');

INSERT INTO career (name, cycle_duration, description) VALUES
('Desarrollo de Software', 6, 'Carrera enfocada en programación y bases de datos.'),
('Diseño Gráfico', 6, 'Carrera enfocada en branding y entornos digitales.');

INSERT INTO students (first_name, last_name, id_career, document_type, numer_doc, email, phone, adress, id_ubigeo) VALUES
('Juan', 'Pérez', 1, 'DNI', '77665544', 'juan.perez@email.com', '999888777', 'Av. Las Flores 123', '150142'),
('Maria', 'Gomez', 2, 'DNI', '44556677', 'maria.gomez@email.com', '999111222', 'Jr. Los Pinos 456', '150112');

INSERT INTO locations (name_locations, city, id_ubigeo) VALUES
('Sede Lima Norte', 'Lima', '150112'),
('Sede Central', 'Lima', '150101');

INSERT INTO teachers (first_name, last_name, specialty, document_type, numer_doc, corporate_email, phone, adress, id_ubigeo) VALUES
('Carlos', 'Díaz', 'Bases de Datos y Backend', 'DNI', '11223344', 'cdiaz@edufuturo.edu.pe', '988777666', 'Av. Universitaria 789', '150142'),
('Ana', 'Torres', 'UI/UX y Frontend', 'DNI', '55667788', 'atorres@edufuturo.edu.pe', '977666555', 'Av. Mendiola 101', '150112');

INSERT INTO promoter (first_name, last_name, document_type, numer_doc, corporate_email, phone, adress, id_locations, id_ubigeo) VALUES
('Luis', 'Mendoza', 'DNI', '99887766', 'lmendoza@edufuturo.edu.pe', '955444333', 'Jr. Lima 202', 1, '150101');

INSERT INTO courses (name, credits, id_teacher, id_career) VALUES 
('Introducción a Base de Datos', 4, 1, 1),
('Programación Orientada a Objetos', 5, 1, 1),
('Fundamentos del Diseño Visual', 3, 2, 2);

INSERT INTO enrollment (id_student, id_locations, id_promoter, amount) VALUES
(1, 1, 1, 350.00);

INSERT INTO enrollment_detail (id_enrollment, id_courses) VALUES
(1, 1),
(1, 2);
GO
