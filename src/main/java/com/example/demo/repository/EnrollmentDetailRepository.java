package com.example.demo.repository;

import com.example.demo.entity.EnrollmentDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface EnrollmentDetailRepository extends JpaRepository<EnrollmentDetail, Integer> {

    @Query("SELECT ed FROM EnrollmentDetail ed WHERE ed.status = true")
    List<EnrollmentDetail> findAllActive();

    @Query("SELECT ed FROM EnrollmentDetail ed WHERE ed.idEnrollmentDetail = :id AND ed.status = true")
    Optional<EnrollmentDetail> findActiveById(@Param("id") Integer id);

    @Query("SELECT ed FROM EnrollmentDetail ed WHERE ed.enrollment.idEnrollment = :enrollmentId AND ed.status = true")
    List<EnrollmentDetail> findActiveByEnrollment(@Param("enrollmentId") Integer enrollmentId);

    @Query("SELECT ed FROM EnrollmentDetail ed WHERE ed.enrollment.idEnrollment = :enrollmentId AND ed.courses.idCourses = :courseId AND ed.status = true")
    Optional<EnrollmentDetail> findActiveByEnrollmentAndCourse(@Param("enrollmentId") Integer enrollmentId, @Param("courseId") Integer courseId);
}
