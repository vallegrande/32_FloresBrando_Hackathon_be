package com.example.demo.repository;

import com.example.demo.entity.Students;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface StudentsRepository extends JpaRepository<Students, Integer> {

    @Query("SELECT s FROM Students s WHERE s.status = true")
    List<Students> findAllActive();

    @Query("SELECT s FROM Students s WHERE s.idStudents = :id AND s.status = true")
    Optional<Students> findActiveById(@Param("id") Integer id);

    @Query("SELECT s FROM Students s WHERE s.email = :email AND s.status = true")
    Optional<Students> findActiveByEmail(@Param("email") String email);

    @Query("SELECT s FROM Students s WHERE s.numerDoc = :numerDoc AND s.status = true")
    Optional<Students> findActiveByNumerDoc(@Param("numerDoc") String numerDoc);
}
