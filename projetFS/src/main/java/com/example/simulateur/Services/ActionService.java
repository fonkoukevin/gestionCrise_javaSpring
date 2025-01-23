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
        // Vérifier si le signalement existe
        Signalement signalement = signalementRepository.findById(signalementId)
                .orElseThrow(() -> new RuntimeException("Signalement avec ID " + signalementId + " non trouvé."));

        // Vérifier si l'utilisateur assigné existe
        Long assigneeId = action.getAssignee().getId();
        Utilisateur assignee = utilisateurRepository.findById(assigneeId)
                .orElseThrow(() -> new RuntimeException("Utilisateur avec ID " + assigneeId + " non trouvé."));

        // Lier le signalement et l'utilisateur à l'action
        action.setSignalement(signalement);
        action.setAssignee(assignee);

        // Sauvegarder l'action
        return actionRepository.save(action);
    }
}
