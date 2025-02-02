package com.example.simulateur.Controleurs;


import com.example.simulateur.Entites.Signalement;
import com.example.simulateur.Entites.Utilisateur;
import com.example.simulateur.Services.SignalementService;
import com.example.simulateur.Services.UtilisateurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/signalements")
public class SignalementController {

    @Autowired
    private SignalementService signalementService;
    @Autowired
    private UtilisateurService utilisateurService;

    @PostMapping
    public ResponseEntity<Signalement> createSignalement(@RequestBody Signalement signalement) {
        Signalement newSignalement = signalementService.saveSignalement(signalement);
        return ResponseEntity.ok(newSignalement);
    }

    @GetMapping
    public ResponseEntity<List<Signalement>> getAllSignalements() {
        return ResponseEntity.ok(signalementService.findAll());
    }

    @PostMapping("/generate")
    public ResponseEntity<Signalement> generateSignalement(@RequestParam Long utilisateurId) {
        System.out.println("Utilisateur ID reçu : " + utilisateurId);

        Utilisateur utilisateur = utilisateurService.findUtilisateurById(utilisateurId);
        if (utilisateur == null) {
            return ResponseEntity.badRequest().build(); // Retourne un 400 si l'utilisateur n'existe pas
        }

        Signalement signalement = signalementService.generateSignalement(utilisateur);
        System.out.println("Signalement généré avec ID : " + signalement.getId());

        return ResponseEntity.ok(signalement);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Signalement> getSignalementById(@PathVariable Long id) {
        Signalement signalement = signalementService.findById(id);
        if (signalement == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(signalement);
    }


}
