package com.example.simulateur.Controleurs;

import com.example.simulateur.Entites.CompteRendu;
import com.example.simulateur.Entites.Signalement;
import com.example.simulateur.Entites.Utilisateur;
import com.example.simulateur.Services.CompteRenduService;
import com.example.simulateur.Services.SignalementService;
import com.example.simulateur.Services.UtilisateurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/compterendus")
public class CompteRenduController {

    @Autowired
    private CompteRenduService compteRenduService;

    @Autowired
    private SignalementService signalementService;

    @Autowired
    private UtilisateurService utilisateurService;

    @PostMapping("/generate")
    public ResponseEntity<CompteRendu> generateCompteRendu(
            @RequestParam Long signalementId,
            @RequestParam Long utilisateurId) {

        // Trouver le signalement par ID
       Signalement signalement = signalementService.findSignalementById(signalementId);

        // Trouver l'utilisateur par ID
        Utilisateur utilisateur = utilisateurService.findUtilisateurById(utilisateurId);

        // Générer le compte rendu
        CompteRendu compteRendu = compteRenduService.generateCompteRendu(signalement, utilisateur);

        return ResponseEntity.ok(compteRendu);
    }
}
