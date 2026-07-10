package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name = "career", uniqueConstraints = {
        @UniqueConstraint(name = "uq_career_name", columnNames = "name")
})
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Career extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_career")
    private Integer idCareer;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "cycle_duration", nullable = false)
    private Integer cycleDuration;

    @Column(name = "description", length = 200)
    private String description;
}
