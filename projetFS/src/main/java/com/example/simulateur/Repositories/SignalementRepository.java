package com.example.simulateur.Repositories;


import com.example.simulateur.Entites.Signalement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SignalementRepository extends JpaRepository<Signalement, Long> {
}