package com.example.demo.controller;

import com.example.demo.entity.Career;
import com.example.demo.service.CareerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/careers")
@CrossOrigin("*")
public class CareerController {

    private final CareerService careerService;

    public CareerController(CareerService careerService) {
        this.careerService = careerService;
    }

    @GetMapping
    public List<Career> getAllCareers() {
        return careerService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Career> getCareerById(@PathVariable Integer id) {
        return careerService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Career createCareer(@RequestBody Career career) {
        return careerService.save(career);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Career> updateCareer(@PathVariable Integer id, @RequestBody Career careerDetails) {
        return ResponseEntity.ok(careerService.update(id, careerDetails));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCareer(@PathVariable Integer id) {
        careerService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/restore")
    public ResponseEntity<Void> restoreCareer(@PathVariable Integer id) {
        careerService.restore(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Void> changeCareerStatus(@PathVariable Integer id, @RequestParam Boolean status) {
        careerService.changeStatus(id, status);
        return ResponseEntity.ok().build();
    }
}
