package com.example.demo.controller;

import com.example.demo.entity.Promoter;
import com.example.demo.service.PromoterService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/promoters")
@CrossOrigin("*")
public class PromoterController {

    private final PromoterService promoterService;

    public PromoterController(PromoterService promoterService) {
        this.promoterService = promoterService;
    }

    @GetMapping
    public List<Promoter> getAllPromoters() {
        return promoterService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Promoter> getPromoterById(@PathVariable Integer id) {
        return promoterService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Promoter createPromoter(@RequestBody Promoter promoter) {
        return promoterService.save(promoter);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Promoter> updatePromoter(@PathVariable Integer id, @RequestBody Promoter promoterDetails) {
        return ResponseEntity.ok(promoterService.update(id, promoterDetails));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePromoter(@PathVariable Integer id) {
        promoterService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/restore")
    public ResponseEntity<Void> restorePromoter(@PathVariable Integer id) {
        promoterService.restore(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Void> changePromoterStatus(@PathVariable Integer id, @RequestParam Boolean status) {
        promoterService.changeStatus(id, status);
        return ResponseEntity.ok().build();
    }
}
