package com.example.simulateur.Services;

import com.example.simulateur.Entites.Action;
import com.example.simulateur.Entites.Signalement;
import com.example.simulateur.Entites.Utilisateur;
import com.example.simulateur.Repositories.ActionRepository;
import com.example.simulateur.Repositories.SignalementRepository;
import com.example.simulateur.Repositories.UtilisateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

}
