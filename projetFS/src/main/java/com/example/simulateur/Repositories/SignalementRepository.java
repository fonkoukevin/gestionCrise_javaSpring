package com.example.simulateur.Repositories;


import com.example.simulateur.Entites.Signalement;
import com.example.simulateur.Entites.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SignalementRepository extends JpaRepository<Signalement, Long> {
    List<Signalement> findByCitoyen(Utilisateur utilisateur);
}