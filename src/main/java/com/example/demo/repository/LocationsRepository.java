package com.example.demo.repository;

import com.example.demo.entity.Locations;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface LocationsRepository extends JpaRepository<Locations, Integer> {

    @Query("SELECT l FROM Locations l WHERE l.status = true")
    List<Locations> findAllActive();

    @Query("SELECT l FROM Locations l WHERE l.idLocations = :id AND l.status = true")
    Optional<Locations> findActiveById(@Param("id") Integer id);
}
