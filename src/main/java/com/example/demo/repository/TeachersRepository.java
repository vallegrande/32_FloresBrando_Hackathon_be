package com.example.demo.repository;

import com.example.demo.entity.Teachers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TeachersRepository extends JpaRepository<Teachers, Integer> {

    @Query("SELECT t FROM Teachers t WHERE t.status = true")
    List<Teachers> findAllActive();

    @Query("SELECT t FROM Teachers t WHERE t.idTeacher = :id AND t.status = true")
    Optional<Teachers> findActiveById(@Param("id") Integer id);

    @Query("SELECT t FROM Teachers t WHERE t.corporateEmail = :email AND t.status = true")
    Optional<Teachers> findActiveByEmail(@Param("email") String email);

    @Query("SELECT t FROM Teachers t WHERE t.numerDoc = :numerDoc AND t.status = true")
    Optional<Teachers> findActiveByNumerDoc(@Param("numerDoc") String numerDoc);
}
