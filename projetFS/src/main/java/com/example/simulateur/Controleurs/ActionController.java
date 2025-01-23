package com.example.simulateur.Controleurs;

import com.example.simulateur.Entites.Action;
import com.example.simulateur.Services.ActionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
        return ResponseEntity.ok(actionService.saveAction(action, signalementId));
    }
}
