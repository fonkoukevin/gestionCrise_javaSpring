package com.example.simulateur.Entites;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "signalement")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Signalement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private String localisation;

    @Enumerated(EnumType.STRING)
    private StatutSignalement statut = StatutSignalement.PENDING;

    @ManyToOne(fetch = FetchType.LAZY) // Ou FetchType.EAGER selon vos besoins
    @JoinColumn(name = "citoyen_id", nullable = false)
    private Utilisateur citoyen;


    @Column(name = "date_soumission")
    private LocalDateTime dateSoumission;

    @PrePersist
    protected void onCreate() {
        this.dateSoumission = LocalDateTime.now();
    }

    // Getters and Setters

    public enum StatutSignalement {
        PENDING,
        IN_PROGRESS,
        RESOLVED
    }
}