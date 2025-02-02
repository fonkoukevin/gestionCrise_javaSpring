package com.example.simulateur.DTO;

import com.example.simulateur.Entites.Signalement;

import java.time.LocalDateTime;

public class SignalementDTO {
    private Long id;
    private String description;
    private String localisation;
    private String statut;
    private String citoyenUsername;
    private String citoyenEmail;
    private LocalDateTime dateSoumission;

    // ✅ Constructeur qui prend un `Signalement` en paramètre
    public SignalementDTO(Signalement signalement) {
        this.id = signalement.getId();
        this.description = signalement.getDescription();
        this.localisation = signalement.getLocalisation();
        this.statut = signalement.getStatut().name(); // Enum en String
        this.citoyenUsername = signalement.getCitoyen().getUsername();
        this.citoyenEmail = signalement.getCitoyen().getEmail();
        this.dateSoumission = signalement.getDateSoumission();
    }

    // ✅ Getters
    public Long getId() { return id; }
    public String getDescription() { return description; }
    public String getLocalisation() { return localisation; }
    public String getStatut() { return statut; }
    public String getCitoyenUsername() { return citoyenUsername; }
    public String getCitoyenEmail() { return citoyenEmail; }
    public LocalDateTime getDateSoumission() { return dateSoumission; }
}
