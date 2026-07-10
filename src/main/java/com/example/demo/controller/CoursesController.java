package com.example.demo.controller;

import com.example.demo.entity.Courses;
import com.example.demo.service.CoursesService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/courses")
@CrossOrigin("*")
public class CoursesController {

    private final CoursesService coursesService;

    public CoursesController(CoursesService coursesService) {
        this.coursesService = coursesService;
    }

    @GetMapping
    public List<Courses> getAllCourses() {
        return coursesService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Courses> getCourseById(@PathVariable Integer id) {
        return coursesService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/career/{careerId}")
    public List<Courses> getCoursesByCareer(@PathVariable Integer careerId) {
        return coursesService.findByCareer(careerId);
    }

    @PostMapping
    public Courses createCourse(@RequestBody Courses course) {
        return coursesService.save(course);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Courses> updateCourse(@PathVariable Integer id, @RequestBody Courses courseDetails) {
        return ResponseEntity.ok(coursesService.update(id, courseDetails));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCourse(@PathVariable Integer id) {
        coursesService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/restore")
    public ResponseEntity<Void> restoreCourse(@PathVariable Integer id) {
        coursesService.restore(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Void> changeCourseStatus(@PathVariable Integer id, @RequestParam Boolean status) {
        coursesService.changeStatus(id, status);
        return ResponseEntity.ok().build();
    }
}
