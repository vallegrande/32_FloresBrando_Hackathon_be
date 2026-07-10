package com.example.demo.service;

import com.example.demo.entity.Career;
import com.example.demo.repository.CareerRepository;
import com.example.demo.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CareerService {

    private final CareerRepository careerRepository;

    public CareerService(CareerRepository careerRepository) {
        this.careerRepository = careerRepository;
    }

    public List<Career> findAll() {
        return careerRepository.findAllActive();
    }

    public Optional<Career> findById(Integer id) {
        return careerRepository.findActiveById(id);
    }

    public Career save(Career career) {
        if (career.getStatus() == null) {
            career.setStatus(true);
        }
        return careerRepository.save(career);
    }

    public Career update(Integer id, Career careerDetails) {
        Career career = careerRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No existe career con id: " + id));
        career.setName(careerDetails.getName());
        career.setCycleDuration(careerDetails.getCycleDuration());
        career.setDescription(careerDetails.getDescription());
        return careerRepository.save(career);
    }

    public void delete(Integer id) {
        Career career = careerRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No existe career con id: " + id));
        career.setStatus(false);
        careerRepository.save(career);
    }

    public void restore(Integer id) {
        Career career = careerRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No existe career con id: " + id));
        career.setStatus(true);
        careerRepository.save(career);
    }

    public void changeStatus(Integer id, Boolean status) {
        Career career = careerRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No existe career con id: " + id));
        career.setStatus(status);
        careerRepository.save(career);
    }
}
