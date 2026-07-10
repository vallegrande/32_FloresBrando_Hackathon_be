package com.example.demo.repository;

import com.example.demo.entity.Career;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CareerRepository extends JpaRepository<Career, Integer> {

    @Query("SELECT c FROM Career c WHERE c.status = true")
    List<Career> findAllActive();

    @Query("SELECT c FROM Career c WHERE c.idCareer = :id AND c.status = true")
    Optional<Career> findActiveById(@Param("id") Integer id);
}
