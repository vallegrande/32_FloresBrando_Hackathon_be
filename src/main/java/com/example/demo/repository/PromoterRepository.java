package com.example.demo.repository;

import com.example.demo.entity.Promoter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PromoterRepository extends JpaRepository<Promoter, Integer> {

    @Query("SELECT p FROM Promoter p WHERE p.status = true")
    List<Promoter> findAllActive();

    @Query("SELECT p FROM Promoter p WHERE p.idPromoter = :id AND p.status = true")
    Optional<Promoter> findActiveById(@Param("id") Integer id);

    @Query("SELECT p FROM Promoter p WHERE p.corporateEmail = :email AND p.status = true")
    Optional<Promoter> findActiveByEmail(@Param("email") String email);

    @Query("SELECT p FROM Promoter p WHERE p.numerDoc = :numerDoc AND p.status = true")
    Optional<Promoter> findActiveByNumerDoc(@Param("numerDoc") String numerDoc);
}
