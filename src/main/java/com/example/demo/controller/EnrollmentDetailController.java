package com.example.demo.controller;

import com.example.demo.entity.EnrollmentDetail;
import com.example.demo.service.EnrollmentDetailService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Tabla transaccional (detalle): enrollment_detail.
 * CRUD completo + baja/restauración lógica (campo status).
 */
@RestController
@RequestMapping("/api/enrollment-detail")
@CrossOrigin("*")
public class EnrollmentDetailController {

    private final EnrollmentDetailService enrollmentDetailService;

    public EnrollmentDetailController(EnrollmentDetailService enrollmentDetailService) {
        this.enrollmentDetailService = enrollmentDetailService;
    }

    @GetMapping
    public List<EnrollmentDetail> findAll() {
        return enrollmentDetailService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<EnrollmentDetail> findById(@PathVariable Integer id) {
        return enrollmentDetailService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/enrollment/{enrollmentId}")
    public List<EnrollmentDetail> findByEnrollment(@PathVariable Integer enrollmentId) {
        return enrollmentDetailService.findByEnrollment(enrollmentId);
    }

    // Registrar
    @PostMapping
    public ResponseEntity<EnrollmentDetail> save(@Valid @RequestBody EnrollmentDetail enrollmentDetail) {
        EnrollmentDetail saved = enrollmentDetailService.save(enrollmentDetail);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    // Editar
    @PutMapping("/{id}")
    public ResponseEntity<EnrollmentDetail> update(@PathVariable Integer id, @RequestBody EnrollmentDetail enrollmentDetail) {
        return ResponseEntity.ok(enrollmentDetailService.update(id, enrollmentDetail));
    }

    // Eliminar (baja lógica: status = false)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        enrollmentDetailService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // Restaurar (status = true)
    @PutMapping("/{id}/restore")
    public ResponseEntity<Void> restore(@PathVariable Integer id) {
        enrollmentDetailService.restore(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Void> changeStatus(@PathVariable Integer id, @RequestParam Boolean status) {
        enrollmentDetailService.changeStatus(id, status);
        return ResponseEntity.ok().build();
    }
}
