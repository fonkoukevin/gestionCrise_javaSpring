package com.example.simulateur.Controleurs;

import com.example.simulateur.Entites.Action;
import com.example.simulateur.Entites.Signalement;
import com.example.simulateur.Entites.Utilisateur;
import com.example.simulateur.Repositories.SignalementRepository;
import com.example.simulateur.Repositories.UtilisateurRepository;
import com.example.simulateur.Services.OllamaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ollama")
public class OllamaController {

    @Autowired
    private OllamaService ollamaService;

    @Autowired
    private SignalementRepository signalementRepository;

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    /**
     * Génère et sauvegarde les actions pour un signalement donné par un utilisateur.
     *
     * @param signalementId L'identifiant du signalement.
     * @param userId        L'identifiant de l'utilisateur.
     * @return Une liste d'actions générées et sauvegardées.
     */
    @PostMapping("/generate-actions")
    public ResponseEntity<List<Action>> generateActions(
            @RequestParam Long signalementId,
            @RequestParam Long userId) {
        // Récupérer le signalement depuis le repository
        Signalement signalement = signalementRepository.findById(signalementId)
                .orElseThrow(() -> new RuntimeException("Signalement non trouvé avec l'ID : " + signalementId));

        // Récupérer l'utilisateur depuis le repository
        Utilisateur utilisateur = utilisateurRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé avec l'ID : " + userId));

        // Générer et sauvegarder les actions
        List<Action> actions = ollamaService.generateAndSaveActions(signalement, utilisateur);

        return ResponseEntity.ok(actions);
    }
}
