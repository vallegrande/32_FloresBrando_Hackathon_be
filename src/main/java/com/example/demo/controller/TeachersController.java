package com.example.demo.controller;

import com.example.demo.entity.Teachers;
import com.example.demo.service.TeachersService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/teachers")
@CrossOrigin("*")
public class TeachersController {

    private final TeachersService teachersService;

    public TeachersController(TeachersService teachersService) {
        this.teachersService = teachersService;
    }

    @GetMapping
    public List<Teachers> getAllTeachers() {
        return teachersService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Teachers> getTeacherById(@PathVariable Integer id) {
        return teachersService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Teachers createTeacher(@RequestBody Teachers teacher) {
        return teachersService.save(teacher);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Teachers> updateTeacher(@PathVariable Integer id, @RequestBody Teachers teacherDetails) {
        return ResponseEntity.ok(teachersService.update(id, teacherDetails));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTeacher(@PathVariable Integer id) {
        teachersService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/restore")
    public ResponseEntity<Void> restoreTeacher(@PathVariable Integer id) {
        teachersService.restore(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Void> changeTeacherStatus(@PathVariable Integer id, @RequestParam Boolean status) {
        teachersService.changeStatus(id, status);
        return ResponseEntity.ok().build();
    }
}
