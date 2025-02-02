package com.example.simulateur.Controleurs;

import com.example.simulateur.Entites.Action;
import com.example.simulateur.Services.ActionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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

    @PutMapping("/{actionId}/status")
    public ResponseEntity<Action> updateActionStatus(
            @PathVariable Long actionId,
            @RequestBody Map<String, String> requestBody) {
        String newStatus = requestBody.get("statut");

        if (newStatus == null || newStatus.isEmpty()) {
            return ResponseEntity.badRequest().body(null);
        }

        try {
            Action updatedAction = actionService.updateActionStatus(actionId, newStatus);
            return ResponseEntity.ok(updatedAction);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @DeleteMapping("/{actionId}")
    public ResponseEntity<Void> deleteAction(@PathVariable Long actionId) {
        try {
            actionService.deleteAction(actionId);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    @PutMapping("/{actionId}")
    public ResponseEntity<Action> updateAction(
            @PathVariable Long actionId,
            @RequestBody Map<String, String> requestBody) {
        String newDescription = requestBody.get("description");

        if (newDescription == null || newDescription.trim().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        Action updatedAction = actionService.updateAction(actionId, newDescription);
        return ResponseEntity.ok(updatedAction);
    }

}
