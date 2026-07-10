package com.example.demo.service;

import com.example.demo.entity.Promoter;
import com.example.demo.repository.PromoterRepository;
import com.example.demo.exception.BusinessException;
import com.example.demo.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PromoterService {

    private final PromoterRepository promoterRepository;

    public PromoterService(PromoterRepository promoterRepository) {
        this.promoterRepository = promoterRepository;
    }

    public List<Promoter> findAll() {
        return promoterRepository.findAllActive();
    }

    public Optional<Promoter> findById(Integer id) {
        return promoterRepository.findActiveById(id);
    }

    public Promoter save(Promoter promoter) {
        if (promoterRepository.findActiveByEmail(promoter.getCorporateEmail()).isPresent()) {
            throw new BusinessException("Ya existe un promotor activo con este email");
        }
        if (promoterRepository.findActiveByNumerDoc(promoter.getNumerDoc()).isPresent()) {
            throw new BusinessException("Ya existe un promotor activo con este documento");
        }
        if (promoter.getStatus() == null) {
            promoter.setStatus(true);
        }
        return promoterRepository.save(promoter);
    }

    public Promoter update(Integer id, Promoter promoterDetails) {
        Promoter promoter = promoterRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No existe promoter con id: " + id));
        promoter.setFirstName(promoterDetails.getFirstName());
        promoter.setLastName(promoterDetails.getLastName());
        promoter.setDocumentType(promoterDetails.getDocumentType());
        promoter.setNumerDoc(promoterDetails.getNumerDoc());
        promoter.setCorporateEmail(promoterDetails.getCorporateEmail());
        promoter.setPhone(promoterDetails.getPhone());
        promoter.setAdress(promoterDetails.getAdress());
        promoter.setLocations(promoterDetails.getLocations());
        promoter.setUbigeo(promoterDetails.getUbigeo());
        return promoterRepository.save(promoter);
    }

    public void delete(Integer id) {
        Promoter promoter = promoterRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No existe promoter con id: " + id));
        promoter.setStatus(false);
        promoterRepository.save(promoter);
    }

    public void restore(Integer id) {
        Promoter promoter = promoterRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No existe promoter con id: " + id));
        promoter.setStatus(true);
        promoterRepository.save(promoter);
    }

    public void changeStatus(Integer id, Boolean status) {
        Promoter promoter = promoterRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No existe promoter con id: " + id));
        promoter.setStatus(status);
        promoterRepository.save(promoter);
    }
}
