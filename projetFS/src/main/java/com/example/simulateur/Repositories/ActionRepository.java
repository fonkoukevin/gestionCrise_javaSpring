package com.example.simulateur.Repositories;


import com.example.simulateur.Entites.Action;
import com.example.simulateur.Entites.Signalement;
import com.example.simulateur.Entites.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ActionRepository extends JpaRepository<Action, Long> {
    List<Action> findBySignalement(Signalement signalement);

    List<Action> findBySignalementAndStatut(Signalement signalement, Action.StatutAction statut);
    List<Action> findBySignalementId(Long signalementId);


    List<Action> findByAssigneeAndStatut(Utilisateur utilisateur, Action.StatutAction statut);
    List<Action> findByAssigneeAndSignalementAndStatut(Utilisateur utilisateur, Signalement signalement, Action.StatutAction statut);
}