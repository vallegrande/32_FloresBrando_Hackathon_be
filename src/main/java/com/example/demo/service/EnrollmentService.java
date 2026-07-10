package com.example.demo.service;

import com.example.demo.entity.Enrollment;
import com.example.demo.entity.Locations;
import com.example.demo.entity.Promoter;
import com.example.demo.entity.Students;
import com.example.demo.exception.BusinessException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.EnrollmentRepository;
import com.example.demo.repository.LocationsRepository;
import com.example.demo.repository.PromoterRepository;
import com.example.demo.repository.StudentsRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Tabla transaccional (cabecera): enrollment.
 * Cada matrícula referencia a un estudiante (maestra), una sede y un promotor.
 */
@Service
public class EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final StudentsRepository studentsRepository;
    private final LocationsRepository locationsRepository;
    private final PromoterRepository promoterRepository;

    public EnrollmentService(EnrollmentRepository enrollmentRepository,
                              StudentsRepository studentsRepository,
                              LocationsRepository locationsRepository,
                              PromoterRepository promoterRepository) {
        this.enrollmentRepository = enrollmentRepository;
        this.studentsRepository = studentsRepository;
        this.locationsRepository = locationsRepository;
        this.promoterRepository = promoterRepository;
    }

    public List<Enrollment> findAll() {
        return enrollmentRepository.findAllActive();
    }

    public Optional<Enrollment> findById(Integer id) {
        return enrollmentRepository.findActiveById(id);
    }

    public List<Enrollment> findByStudent(Integer studentId) {
        return enrollmentRepository.findActiveByStudent(studentId);
    }

    @Transactional
    public Enrollment save(Enrollment enrollment) {
        if (enrollment.getStudent() == null || enrollment.getStudent().getIdStudents() == null) {
            throw new BusinessException("El estudiante (id_student) es obligatorio");
        }
        if (enrollment.getLocations() == null || enrollment.getLocations().getIdLocations() == null) {
            throw new BusinessException("La sede (id_locations) es obligatoria");
        }
        if (enrollment.getPromoter() == null || enrollment.getPromoter().getIdPromoter() == null) {
            throw new BusinessException("El promotor (id_promoter) es obligatorio");
        }

        Students student = studentsRepository.findActiveById(enrollment.getStudent().getIdStudents())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No existe un estudiante activo con id: " + enrollment.getStudent().getIdStudents()));
        Locations locations = locationsRepository.findActiveById(enrollment.getLocations().getIdLocations())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No existe una sede activa con id: " + enrollment.getLocations().getIdLocations()));
        Promoter promoter = promoterRepository.findActiveById(enrollment.getPromoter().getIdPromoter())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No existe un promotor activo con id: " + enrollment.getPromoter().getIdPromoter()));

        // Regla de negocio: un estudiante no puede tener dos matrículas activas a la vez
        List<Enrollment> existingEnrollments = enrollmentRepository.findActiveByStudent(student.getIdStudents());
        if (!existingEnrollments.isEmpty()) {
            throw new BusinessException("El estudiante ya tiene una matrícula activa. No se puede matricular dos veces.");
        }

        enrollment.setStudent(student);
        enrollment.setLocations(locations);
        enrollment.setPromoter(promoter);
        if (enrollment.getEnrollmentDate() == null) {
            enrollment.setEnrollmentDate(LocalDateTime.now());
        }
        if (enrollment.getStatus() == null) {
            enrollment.setStatus(true);
        }

        return enrollmentRepository.save(enrollment);
    }

    @Transactional
    public Enrollment update(Integer id, Enrollment enrollmentDetails) {
        Enrollment enrollment = enrollmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No existe la matrícula con id: " + id));

        if (enrollmentDetails.getStudent() != null && enrollmentDetails.getStudent().getIdStudents() != null) {
            Students student = studentsRepository.findActiveById(enrollmentDetails.getStudent().getIdStudents())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "No existe un estudiante activo con id: " + enrollmentDetails.getStudent().getIdStudents()));
            enrollment.setStudent(student);
        }
        if (enrollmentDetails.getLocations() != null && enrollmentDetails.getLocations().getIdLocations() != null) {
            Locations locations = locationsRepository.findActiveById(enrollmentDetails.getLocations().getIdLocations())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "No existe una sede activa con id: " + enrollmentDetails.getLocations().getIdLocations()));
            enrollment.setLocations(locations);
        }
        if (enrollmentDetails.getPromoter() != null && enrollmentDetails.getPromoter().getIdPromoter() != null) {
            Promoter promoter = promoterRepository.findActiveById(enrollmentDetails.getPromoter().getIdPromoter())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "No existe un promotor activo con id: " + enrollmentDetails.getPromoter().getIdPromoter()));
            enrollment.setPromoter(promoter);
        }
        if (enrollmentDetails.getAmount() != null) {
            enrollment.setAmount(enrollmentDetails.getAmount());
        }
        if (enrollmentDetails.getEnrollmentDate() != null) {
            enrollment.setEnrollmentDate(enrollmentDetails.getEnrollmentDate());
        }

        return enrollmentRepository.save(enrollment);
    }

    /** Baja lógica: status = false (el header y sus detalles quedan en la BD). */
    @Transactional
    public void delete(Integer id) {
        Enrollment enrollment = enrollmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No existe la matrícula con id: " + id));
        enrollment.setStatus(false);
        enrollmentRepository.save(enrollment);
    }

    /** Restauración lógica: status = true. */
    @Transactional
    public void restore(Integer id) {
        Enrollment enrollment = enrollmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No existe la matrícula con id: " + id));
        enrollment.setStatus(true);
        enrollmentRepository.save(enrollment);
    }

    @Transactional
    public void changeStatus(Integer id, Boolean status) {
        Enrollment enrollment = enrollmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No existe la matrícula con id: " + id));
        enrollment.setStatus(status);
        enrollmentRepository.save(enrollment);
    }
}
