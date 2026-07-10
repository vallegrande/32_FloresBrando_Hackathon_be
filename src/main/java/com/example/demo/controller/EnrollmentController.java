package com.example.demo.controller;

import com.example.demo.entity.Enrollment;
import com.example.demo.service.EnrollmentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Tabla transaccional (cabecera): enrollment.
 * CRUD completo + baja/restauración lógica (campo status).
 */
@RestController
@RequestMapping("/api/enrollments")
@CrossOrigin("*")
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    public EnrollmentController(EnrollmentService enrollmentService) {
        this.enrollmentService = enrollmentService;
    }

    @GetMapping
    public List<Enrollment> getAllEnrollments() {
        return enrollmentService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Enrollment> getEnrollmentById(@PathVariable Integer id) {
        return enrollmentService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/student/{studentId}")
    public List<Enrollment> getEnrollmentsByStudent(@PathVariable Integer studentId) {
        return enrollmentService.findByStudent(studentId);
    }

    // Registrar
    @PostMapping
    public ResponseEntity<Enrollment> createEnrollment(@Valid @RequestBody Enrollment enrollment) {
        Enrollment saved = enrollmentService.save(enrollment);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    // Editar
    @PutMapping("/{id}")
    public ResponseEntity<Enrollment> updateEnrollment(@PathVariable Integer id, @RequestBody Enrollment enrollmentDetails) {
        return ResponseEntity.ok(enrollmentService.update(id, enrollmentDetails));
    }

    // Eliminar (baja lógica: status = false)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEnrollment(@PathVariable Integer id) {
        enrollmentService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // Restaurar (status = true)
    @PutMapping("/{id}/restore")
    public ResponseEntity<Void> restoreEnrollment(@PathVariable Integer id) {
        enrollmentService.restore(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Void> changeEnrollmentStatus(@PathVariable Integer id, @RequestParam Boolean status) {
        enrollmentService.changeStatus(id, status);
        return ResponseEntity.ok().build();
    }
}
