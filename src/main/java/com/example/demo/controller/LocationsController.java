package com.example.demo.controller;

import com.example.demo.entity.Locations;
import com.example.demo.service.LocationsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/locations")
@CrossOrigin("*")
public class LocationsController {

    private final LocationsService locationsService;

    public LocationsController(LocationsService locationsService) {
        this.locationsService = locationsService;
    }

    @GetMapping
    public List<Locations> getAllLocations() {
        return locationsService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Locations> getLocationById(@PathVariable Integer id) {
        return locationsService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Locations createLocation(@RequestBody Locations locations) {
        return locationsService.save(locations);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Locations> updateLocation(@PathVariable Integer id, @RequestBody Locations locationsDetails) {
        return ResponseEntity.ok(locationsService.update(id, locationsDetails));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLocation(@PathVariable Integer id) {
        locationsService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/restore")
    public ResponseEntity<Void> restoreLocation(@PathVariable Integer id) {
        locationsService.restore(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Void> changeLocationStatus(@PathVariable Integer id, @RequestParam Boolean status) {
        locationsService.changeStatus(id, status);
        return ResponseEntity.ok().build();
    }
}
