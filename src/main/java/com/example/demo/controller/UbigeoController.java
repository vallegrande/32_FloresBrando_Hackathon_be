package com.example.demo.controller;

import com.example.demo.entity.Ubigeo;
import com.example.demo.service.UbigeoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ubigeo")
@CrossOrigin("*")
public class UbigeoController {

    private final UbigeoService ubigeoService;

    public UbigeoController(UbigeoService ubigeoService) {
        this.ubigeoService = ubigeoService;
    }

    @GetMapping
    public List<Ubigeo> getAllUbigeo() {
        return ubigeoService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Ubigeo> getUbigeoById(@PathVariable String id) {
        return ubigeoService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Ubigeo createUbigeo(@RequestBody Ubigeo ubigeo) {
        return ubigeoService.save(ubigeo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Ubigeo> updateUbigeo(@PathVariable String id, @RequestBody Ubigeo ubigeoDetails) {
        return ResponseEntity.ok(ubigeoService.update(id, ubigeoDetails));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUbigeo(@PathVariable String id) {
        ubigeoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
