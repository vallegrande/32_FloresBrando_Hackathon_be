package com.example.demo.controller;

import com.example.demo.entity.Students;
import com.example.demo.service.StudentsService;
import jakarta.validation.Valid;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.util.List;

/**
 * Tabla maestra: students.
 * CRUD completo + baja/restauración lógica (campo status) + import/export.
 */
@RestController
@RequestMapping("/api/students")
@CrossOrigin("*")
public class StudentsController {

    private final StudentsService studentsService;

    public StudentsController(StudentsService studentsService) {
        this.studentsService = studentsService;
    }

    @GetMapping
    public List<Students> getAllStudents() {
        return studentsService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Students> getStudentById(@PathVariable Integer id) {
        return studentsService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Registrar
    @PostMapping
    public ResponseEntity<Students> createStudent(@Valid @RequestBody Students student) {
        Students saved = studentsService.save(student);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    // Editar
    @PutMapping("/{id}")
    public ResponseEntity<Students> updateStudent(@PathVariable Integer id, @Valid @RequestBody Students studentDetails) {
        return ResponseEntity.ok(studentsService.update(id, studentDetails));
    }

    // Eliminar (baja lógica: status = false, el registro NO se borra de la BD)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable Integer id) {
        studentsService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // Restaurar (baja lógica inversa: status = true)
    @PutMapping("/{id}/restore")
    public ResponseEntity<Void> restoreStudent(@PathVariable Integer id) {
        studentsService.restore(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Void> changeStudentStatus(@PathVariable Integer id, @RequestParam Boolean status) {
        studentsService.changeStatus(id, status);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/import")
    public ResponseEntity<String> importStudents(@RequestParam("file") MultipartFile file) {
        try {
            studentsService.importExcel(file);
            return ResponseEntity.ok("Import successful!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Import failed: " + e.getMessage());
        }
    }

    @GetMapping("/export/excel")
    public ResponseEntity<InputStreamResource> exportStudentsToExcel() {
        ByteArrayInputStream in = studentsService.exportExcel();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=students.xlsx");
        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(new InputStreamResource(in));
    }

    @GetMapping("/export/pdf")
    public ResponseEntity<InputStreamResource> exportStudentsToPdf() {
        ByteArrayInputStream in = studentsService.exportPdf();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=students.pdf");
        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(in));
    }
}
