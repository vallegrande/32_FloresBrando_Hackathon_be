package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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
@Table(name = "promoter")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Promoter extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_promoter")
    private Integer idPromoter;

    @NotBlank
    @Size(max = 50)
    @Column(name = "first_name", nullable = false, length = 50)
    private String firstName;

    @NotBlank
    @Size(max = 50)
    @Column(name = "last_name", nullable = false, length = 50)
    private String lastName;

    @NotBlank
    @Size(max = 10)
    @Column(name = "document_type", nullable = false, length = 10)
    private String documentType;

    @NotBlank
    @Size(max = 20)
    @Column(name = "numer_doc", nullable = false, unique = true, length = 20)
    private String numerDoc;

    @NotBlank
    @Email
    @Size(max = 100)
    @Column(name = "corporate_email", nullable = false, unique = true, length = 100)
    private String corporateEmail;

    @Size(max = 15)
    @Column(name = "phone", length = 15)
    private String phone;

    @Size(max = 200)
    @Column(name = "adress", length = 200)
    private String adress;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_locations", nullable = false)
    private Locations locations;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_ubigeo", nullable = false)
    private Ubigeo ubigeo;
}
