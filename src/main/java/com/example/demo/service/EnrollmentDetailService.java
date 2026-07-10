package com.example.demo.service;

import com.example.demo.entity.Courses;
import com.example.demo.entity.Enrollment;
import com.example.demo.entity.EnrollmentDetail;
import com.example.demo.exception.BusinessException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.CoursesRepository;
import com.example.demo.repository.EnrollmentDetailRepository;
import com.example.demo.repository.EnrollmentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Tabla transaccional (detalle): enrollment_detail.
 * Cada fila asocia una matrícula (enrollment) con un curso (courses).
 */
@Service
public class EnrollmentDetailService {

    private final EnrollmentDetailRepository enrollmentDetailRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final CoursesRepository coursesRepository;

    public EnrollmentDetailService(EnrollmentDetailRepository enrollmentDetailRepository,
                                    EnrollmentRepository enrollmentRepository,
                                    CoursesRepository coursesRepository) {
        this.enrollmentDetailRepository = enrollmentDetailRepository;
        this.enrollmentRepository = enrollmentRepository;
        this.coursesRepository = coursesRepository;
    }

    public List<EnrollmentDetail> findAll() {
        return enrollmentDetailRepository.findAllActive();
    }

    public Optional<EnrollmentDetail> findById(Integer id) {
        return enrollmentDetailRepository.findActiveById(id);
    }

    public List<EnrollmentDetail> findByEnrollment(Integer enrollmentId) {
        return enrollmentDetailRepository.findActiveByEnrollment(enrollmentId);
    }

    @Transactional
    public EnrollmentDetail save(EnrollmentDetail enrollmentDetail) {
        if (enrollmentDetail.getEnrollment() == null || enrollmentDetail.getEnrollment().getIdEnrollment() == null) {
            throw new BusinessException("La matrícula (id_enrollment) es obligatoria");
        }
        if (enrollmentDetail.getCourses() == null || enrollmentDetail.getCourses().getIdCourses() == null) {
            throw new BusinessException("El curso (id_courses) es obligatorio");
        }

        Enrollment enrollment = enrollmentRepository.findActiveById(enrollmentDetail.getEnrollment().getIdEnrollment())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No existe una matrícula activa con id: " + enrollmentDetail.getEnrollment().getIdEnrollment()));
        Courses course = coursesRepository.findActiveById(enrollmentDetail.getCourses().getIdCourses())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No existe un curso activo con id: " + enrollmentDetail.getCourses().getIdCourses()));

        // Evita el mismo curso repetido en la misma matrícula (igual que la restricción uq_enrollment_course de la BD)
        if (enrollmentDetailRepository.findActiveByEnrollmentAndCourse(
                enrollment.getIdEnrollment(), course.getIdCourses()).isPresent()) {
            throw new BusinessException("El curso ya está asignado a esta matrícula");
        }

        enrollmentDetail.setEnrollment(enrollment);
        enrollmentDetail.setCourses(course);
        if (enrollmentDetail.getStatus() == null) {
            enrollmentDetail.setStatus(true);
        }
        return enrollmentDetailRepository.save(enrollmentDetail);
    }

    @Transactional
    public EnrollmentDetail update(Integer id, EnrollmentDetail enrollmentDetailDetails) {
        EnrollmentDetail enrollmentDetail = enrollmentDetailRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No existe el detalle de matrícula con id: " + id));

        if (enrollmentDetailDetails.getEnrollment() != null && enrollmentDetailDetails.getEnrollment().getIdEnrollment() != null) {
            Enrollment enrollment = enrollmentRepository.findActiveById(enrollmentDetailDetails.getEnrollment().getIdEnrollment())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "No existe una matrícula activa con id: " + enrollmentDetailDetails.getEnrollment().getIdEnrollment()));
            enrollmentDetail.setEnrollment(enrollment);
        }
        if (enrollmentDetailDetails.getCourses() != null && enrollmentDetailDetails.getCourses().getIdCourses() != null) {
            Courses course = coursesRepository.findActiveById(enrollmentDetailDetails.getCourses().getIdCourses())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "No existe un curso activo con id: " + enrollmentDetailDetails.getCourses().getIdCourses()));
            enrollmentDetail.setCourses(course);
        }

        return enrollmentDetailRepository.save(enrollmentDetail);
    }

    /** Baja lógica: status = false. */
    @Transactional
    public void delete(Integer id) {
        EnrollmentDetail enrollmentDetail = enrollmentDetailRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No existe el detalle de matrícula con id: " + id));
        enrollmentDetail.setStatus(false);
        enrollmentDetailRepository.save(enrollmentDetail);
    }

    /** Restauración lógica: status = true. */
    @Transactional
    public void restore(Integer id) {
        EnrollmentDetail enrollmentDetail = enrollmentDetailRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No existe el detalle de matrícula con id: " + id));
        enrollmentDetail.setStatus(true);
        enrollmentDetailRepository.save(enrollmentDetail);
    }

    @Transactional
    public void changeStatus(Integer id, Boolean status) {
        EnrollmentDetail enrollmentDetail = enrollmentDetailRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No existe el detalle de matrícula con id: " + id));
        enrollmentDetail.setStatus(status);
        enrollmentDetailRepository.save(enrollmentDetail);
    }
}
