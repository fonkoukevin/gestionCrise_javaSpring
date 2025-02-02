package com.example.simulateur.Services;
import com.example.simulateur.Entites.Action;
import com.example.simulateur.Entites.Signalement;
import com.example.simulateur.Entites.Utilisateur;
import com.example.simulateur.Repositories.ActionRepository;
import com.example.simulateur.Repositories.SignalementRepository;
import com.example.simulateur.Repositories.UtilisateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class HistoriqueService {

    @Autowired
    private ActionRepository actionRepository;

    @Autowired
    private SignalementRepository signalementRepository;

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    public Map<String, List<Action>> getHistoriqueByUtilisateur(Long utilisateurId) {
        Utilisateur utilisateur = utilisateurRepository.findById(utilisateurId)
                .orElseThrow(() -> new RuntimeException("❌ Utilisateur non trouvé"));

        List<Signalement> signalements = signalementRepository.findByCitoyen(utilisateur);
        Map<String, List<Action>> historique = new HashMap<>();

        for (Signalement signalement : signalements) {
            List<Action> actions = actionRepository.findByAssigneeAndSignalementAndStatut(
                    utilisateur, signalement, Action.StatutAction.DONE
            );
            if (!actions.isEmpty()) {
                historique.put(signalement.getDescription(), actions);
            }
        }

        return historique;
    }
}
