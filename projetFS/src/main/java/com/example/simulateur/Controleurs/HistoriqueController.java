package com.example.simulateur.Controleurs;

import com.example.simulateur.Services.HistoriqueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.List;
import com.example.simulateur.Entites.Action;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/historique")
public class HistoriqueController {

    @Autowired
    private HistoriqueService historiqueService;

    @GetMapping("/{utilisateurId}")
    public ResponseEntity<Map<String, List<Action>>> getHistorique(@PathVariable Long utilisateurId) {
        Map<String, List<Action>> historique = historiqueService.getHistoriqueByUtilisateur(utilisateurId);
        return ResponseEntity.ok(historique);
    }
}
