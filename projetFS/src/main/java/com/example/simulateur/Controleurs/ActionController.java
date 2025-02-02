package com.example.simulateur.Controleurs;

import com.example.simulateur.Entites.Action;
import com.example.simulateur.Services.ActionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/actions")
public class ActionController {

    @Autowired
    private ActionService actionService;

    @GetMapping("/signalement/{signalementId}")
    public ResponseEntity<List<Action>> getActionsBySignalement(@PathVariable Long signalementId) {
        return ResponseEntity.ok(actionService.findActionsBySignalementId(signalementId));
    }

    @PostMapping("/signalement/{signalementId}")
    public ResponseEntity<Action> saveAction(
            @RequestBody Action action,
            @PathVariable Long signalementId
    ) {
        try {
            if (action.getDescription() == null || action.getDescription().trim().isEmpty()) {
                return ResponseEntity.badRequest().build(); // ✅ Vérifier si la description est vide
            }

            Action savedAction = actionService.saveAction(action, signalementId);
            return ResponseEntity.ok(savedAction);
        } catch (Exception e) {
            e.printStackTrace(); // ✅ Log d'erreur pour voir le problème
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
