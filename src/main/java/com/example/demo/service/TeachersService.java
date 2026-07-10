package com.example.demo.service;

import com.example.demo.entity.Teachers;
import com.example.demo.repository.TeachersRepository;
import com.example.demo.exception.BusinessException;
import com.example.demo.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TeachersService {

    private final TeachersRepository teachersRepository;

    public TeachersService(TeachersRepository teachersRepository) {
        this.teachersRepository = teachersRepository;
    }

    public List<Teachers> findAll() {
        return teachersRepository.findAllActive();
    }

    public Optional<Teachers> findById(Integer id) {
        return teachersRepository.findActiveById(id);
    }

    public Teachers save(Teachers teacher) {
        if (teachersRepository.findActiveByEmail(teacher.getCorporateEmail()).isPresent()) {
            throw new BusinessException("Ya existe un docente activo con este email");
        }
        if (teachersRepository.findActiveByNumerDoc(teacher.getNumerDoc()).isPresent()) {
            throw new BusinessException("Ya existe un docente activo con este documento");
        }
        if (teacher.getStatus() == null) {
            teacher.setStatus(true);
        }
        return teachersRepository.save(teacher);
    }

    public Teachers update(Integer id, Teachers teacherDetails) {
        Teachers teacher = teachersRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No existe teachers con id: " + id));
        teacher.setFirstName(teacherDetails.getFirstName());
        teacher.setLastName(teacherDetails.getLastName());
        teacher.setSpecialty(teacherDetails.getSpecialty());
        teacher.setDocumentType(teacherDetails.getDocumentType());
        teacher.setNumerDoc(teacherDetails.getNumerDoc());
        teacher.setCorporateEmail(teacherDetails.getCorporateEmail());
        teacher.setPhone(teacherDetails.getPhone());
        teacher.setAdress(teacherDetails.getAdress());
        teacher.setUbigeo(teacherDetails.getUbigeo());
        return teachersRepository.save(teacher);
    }

    public void delete(Integer id) {
        Teachers teacher = teachersRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No existe teachers con id: " + id));
        teacher.setStatus(false);
        teachersRepository.save(teacher);
    }

    public void restore(Integer id) {
        Teachers teacher = teachersRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No existe teachers con id: " + id));
        teacher.setStatus(true);
        teachersRepository.save(teacher);
    }

    public void changeStatus(Integer id, Boolean status) {
        Teachers teacher = teachersRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No existe teachers con id: " + id));
        teacher.setStatus(status);
        teachersRepository.save(teacher);
    }
}
