package com.example.simulateur.Controleurs;


import com.example.simulateur.Entites.Signalement;
import com.example.simulateur.Entites.Utilisateur;
import com.example.simulateur.Services.SignalementService;
import com.example.simulateur.Services.UtilisateurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
        Utilisateur utilisateur = utilisateurService.findUtilisateurById(utilisateurId);
        Signalement signalement = signalementService.generateSignalement(utilisateur);
        return ResponseEntity.ok(signalement);
    }

}
