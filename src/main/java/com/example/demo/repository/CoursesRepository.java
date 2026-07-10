package com.example.demo.repository;

import com.example.demo.entity.Courses;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CoursesRepository extends JpaRepository<Courses, Integer> {

    @Query("SELECT c FROM Courses c WHERE c.status = true")
    List<Courses> findAllActive();

    @Query("SELECT c FROM Courses c WHERE c.idCourses = :id AND c.status = true")
    Optional<Courses> findActiveById(@Param("id") Integer id);

    @Query("SELECT c FROM Courses c WHERE c.career.idCareer = :careerId AND c.status = true")
    List<Courses> findActiveByCareer(@Param("careerId") Integer careerId);
}
