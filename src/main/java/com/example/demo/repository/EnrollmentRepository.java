package com.example.demo.repository;

import com.example.demo.entity.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Integer> {

    @Query("SELECT e FROM Enrollment e WHERE e.status = true")
    List<Enrollment> findAllActive();

    @Query("SELECT e FROM Enrollment e WHERE e.idEnrollment = :id AND e.status = true")
    Optional<Enrollment> findActiveById(@Param("id") Integer id);

    @Query("SELECT e FROM Enrollment e WHERE e.student.idStudents = :studentId AND e.status = true")
    List<Enrollment> findActiveByStudent(@Param("studentId") Integer studentId);
}
