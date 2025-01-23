package com.example.simulateur.Entites;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "action")
public class Action {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String description;

    @Enumerated(EnumType.STRING)
    private StatutAction statut = StatutAction.PENDING;

    @ManyToOne
    @JoinColumn(name = "signalement_id", nullable = false)
    private Signalement signalement;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assignee_id", nullable = false)
    private Utilisateur assignee;

    @Column(name = "date_proposition")
    private LocalDateTime dateProposition;

    @Column(name = "date_completion")
    private LocalDateTime dateCompletion;

    @PrePersist
    protected void onCreate() {
        this.dateProposition = LocalDateTime.now();
    }

    public enum StatutAction {
        PENDING,
        IN_PROGRESS,
        DONE,
        FAILED
    }
}
