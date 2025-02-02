package com.example.simulateur.Services;

import com.example.simulateur.Entites.Action;
import com.example.simulateur.Entites.Signalement;
import com.example.simulateur.Entites.Utilisateur;
import com.example.simulateur.Repositories.ActionRepository;
import com.example.simulateur.Repositories.SignalementRepository;
import com.example.simulateur.Repositories.UtilisateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Service
public class ActionService {

    @Autowired
    private ActionRepository actionRepository;

    @Autowired
    private SignalementRepository signalementRepository;

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    public List<Action> findActionsBySignalementId(Long signalementId) {
        return actionRepository.findBySignalementId(signalementId);
    }

    public Action saveAction(Action action, Long signalementId) {
        Signalement signalement = signalementRepository.findById(signalementId)
                .orElseThrow(() -> new RuntimeException("❌ Signalement non trouvé avec ID : " + signalementId));

        action.setSignalement(signalement);

        if (action.getAssignee() == null) {
            System.out.println("⚠️ Aucun assignee spécifié, affectation d'un utilisateur par défaut...");
            Utilisateur utilisateur = utilisateurRepository.findById(signalement.getCitoyen().getId())
                    .orElseThrow(() -> new RuntimeException("❌ Utilisateur non trouvé avec ID : " + signalement.getCitoyen().getId()));

            action.setAssignee(utilisateur);
        }

        System.out.println("📤 Enregistrement de l'action : " + action);
        return actionRepository.save(action);
    }

    public Action findActionById(Long actionId) {
        return actionRepository.findById(actionId)
                .orElseThrow(() -> new RuntimeException("❌ Action non trouvée avec ID : " + actionId));
    }


    public Action updateActionStatus(Long actionId, String newStatus) {
        Action action = actionRepository.findById(actionId)
                .orElseThrow(() -> new RuntimeException("❌ Action non trouvée avec ID : " + actionId));

        try {
            // ✅ Vérification et conversion sécurisée en Enum
            Action.StatutAction statutEnum = Action.StatutAction.valueOf(newStatus.trim().toUpperCase());
            action.setStatut(statutEnum);

            // ✅ Mettre à jour la date de completion si l'action est DONE
            if (statutEnum == Action.StatutAction.DONE) {
                action.setDateCompletion(LocalDateTime.now());
            } else {
                action.setDateCompletion(null); // Remettre à null si ce n'est plus DONE
            }

            return actionRepository.save(action);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("❌ Statut invalide : " + newStatus + " - Statuts acceptés : " +
                    Arrays.toString(Action.StatutAction.values()));
        }
    }

    public void deleteAction(Long actionId) {
        Action action = actionRepository.findById(actionId)
                .orElseThrow(() -> new RuntimeException("❌ Action non trouvée avec ID : " + actionId));

        actionRepository.delete(action);
        System.out.println("🗑 Action supprimée : " + action.getId());
    }


    public Action updateAction(Long actionId, String newDescription) {
        Action action = actionRepository.findById(actionId)
                .orElseThrow(() -> new RuntimeException("❌ Action non trouvée avec ID : " + actionId));

        action.setDescription(newDescription);
        return actionRepository.save(action);
    }


}
