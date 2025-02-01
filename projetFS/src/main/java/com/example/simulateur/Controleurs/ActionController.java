package com.example.simulateur.Controleurs;

import com.example.simulateur.Entites.Action;
import com.example.simulateur.Services.ActionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/actions")
public class ActionController {

    @Autowired
    private ActionService actionService;

    // ✅ Endpoint pour récupérer les actions liées à un signalement
    @GetMapping("/signalement/{signalementId}")
    public ResponseEntity<List<Action>> getActionsBySignalement(@PathVariable Long signalementId) {
        List<Action> actions = actionService.findActionsBySignalementId(signalementId);

        if (actions == null || actions.isEmpty()) {
            return ResponseEntity.notFound().build(); // ✅ Retourne 404 si aucune action n'est trouvée
        }

        return ResponseEntity.ok(actions);
    }

    // ✅ Endpoint pour enregistrer une action liée à un signalement
    @PostMapping("/signalement/{signalementId}")
    public ResponseEntity<Action> saveAction(
            @RequestBody Action action,
            @PathVariable Long signalementId
    ) {
        Action savedAction = actionService.saveAction(action, signalementId);

        if (savedAction == null) {
            return ResponseEntity.badRequest().build(); // ✅ Retourne 400 si l'action n'a pas pu être créée
        }

        return ResponseEntity.ok(savedAction);
    }
}