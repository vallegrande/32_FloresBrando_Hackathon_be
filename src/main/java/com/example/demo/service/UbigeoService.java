package com.example.demo.service;

import com.example.demo.entity.Ubigeo;
import com.example.demo.exception.BusinessException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.UbigeoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * ubigeo no tiene baja lógica (no posee columna status en la BD), por eso
 * delete() aquí es un borrado físico real.
 */
@Service
public class UbigeoService {

    private final UbigeoRepository ubigeoRepository;

    public UbigeoService(UbigeoRepository ubigeoRepository) {
        this.ubigeoRepository = ubigeoRepository;
    }

    public List<Ubigeo> findAll() {
        return ubigeoRepository.findAll();
    }

    public Optional<Ubigeo> findById(String id) {
        return ubigeoRepository.findById(id);
    }

    public Ubigeo save(Ubigeo ubigeo) {
        if (ubigeoRepository.existsById(ubigeo.getIdUbigeo())) {
            throw new BusinessException("Ya existe un ubigeo con id: " + ubigeo.getIdUbigeo());
        }
        return ubigeoRepository.save(ubigeo);
    }

    public Ubigeo update(String id, Ubigeo ubigeoDetails) {
        Ubigeo ubigeo = ubigeoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No existe el ubigeo con id: " + id));
        ubigeo.setDepartment(ubigeoDetails.getDepartment());
        ubigeo.setProvince(ubigeoDetails.getProvince());
        ubigeo.setDistrict(ubigeoDetails.getDistrict());
        ubigeo.setZone(ubigeoDetails.getZone());
        return ubigeoRepository.save(ubigeo);
    }

    public void delete(String id) {
        if (!ubigeoRepository.existsById(id)) {
            throw new ResourceNotFoundException("No existe el ubigeo con id: " + id);
        }
        ubigeoRepository.deleteById(id);
    }
}
