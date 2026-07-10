package com.example.demo.service;

import com.example.demo.entity.Locations;
import com.example.demo.repository.LocationsRepository;
import com.example.demo.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LocationsService {

    private final LocationsRepository locationsRepository;

    public LocationsService(LocationsRepository locationsRepository) {
        this.locationsRepository = locationsRepository;
    }

    public List<Locations> findAll() {
        return locationsRepository.findAllActive();
    }

    public Optional<Locations> findById(Integer id) {
        return locationsRepository.findActiveById(id);
    }

    public Locations save(Locations locations) {
        if (locations.getStatus() == null) {
            locations.setStatus(true);
        }
        return locationsRepository.save(locations);
    }

    public Locations update(Integer id, Locations locationsDetails) {
        Locations locations = locationsRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No existe locations con id: " + id));
        locations.setNameLocations(locationsDetails.getNameLocations());
        locations.setCity(locationsDetails.getCity());
        locations.setUbigeo(locationsDetails.getUbigeo());
        return locationsRepository.save(locations);
    }

    public void delete(Integer id) {
        Locations locations = locationsRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No existe locations con id: " + id));
        locations.setStatus(false);
        locationsRepository.save(locations);
    }

    public void restore(Integer id) {
        Locations locations = locationsRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No existe locations con id: " + id));
        locations.setStatus(true);
        locationsRepository.save(locations);
    }

    public void changeStatus(Integer id, Boolean status) {
        Locations locations = locationsRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No existe locations con id: " + id));
        locations.setStatus(status);
        locationsRepository.save(locations);
    }
}
