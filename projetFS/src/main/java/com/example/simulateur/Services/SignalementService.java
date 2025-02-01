package com.example.simulateur.Services;

import com.example.simulateur.Entites.Signalement;
import com.example.simulateur.Entites.Utilisateur;
import com.example.simulateur.Repositories.SignalementRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SignalementService {

    private static final Logger logger = LoggerFactory.getLogger(SignalementService.class);

    private final SignalementRepository signalementRepository;
    private final OllamaChatModel chatModel;
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Génère un signalement via Ollama et le sauvegarde dans la base de données.
     *
     * @param utilisateur L'utilisateur associé au signalement.
     * @return Le signalement généré et sauvegardé.
     */
    public Signalement generateSignalement(Utilisateur utilisateur) {
        // Vérifiez l'ID de l'utilisateur reçu
        logger.info("Utilisateur reçu pour le signalement : ID = {}", utilisateur.getId());

        // Continuez avec le reste de la méthode
        String prompt = "Générer uniquement la description et la localisation d'une crise. " +
                "Retournez un JSON au format suivant : " +
                "{\"description\": \"Description de la crise\", \"localisation\": \"Localisation de la crise\"}. " +
                "Ne fournissez que le JSON, sans texte supplémentaire.";

        String response = sendPromptToOllama(prompt);

        if (!isValidJson(response)) {
            logger.error("Réponse d'Ollama invalide : {}", response);
            throw new RuntimeException("Réponse d'Ollama invalide : Le JSON attendu n'a pas été généré.");
        }

        return parseSignalementFromResponse(response, utilisateur);
    }


    public Signalement findSignalementById(Long id) {
        return signalementRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Signalement non trouvé avec l'ID : " + id));
    }

    public Signalement saveSignalement(Signalement signalement) {
        return signalementRepository.save(signalement);
    }

    public List<Signalement> findAll() {
        return signalementRepository.findAll();
    }

    private String sendPromptToOllama(String prompt) {
        List<Message> messages = new ArrayList<>();
        messages.add(new SystemMessage("Vous êtes un assistant expert en gestion des crises."));
        messages.add(new UserMessage(prompt));

        Prompt ollamaPrompt = new Prompt(messages);

        Flux<ChatResponse> chatResponses = chatModel.stream(ollamaPrompt);
        String fullResponse = chatResponses.collectList()
                .blockOptional()
                .orElse(new ArrayList<>())
                .stream()
                .map(response -> response.getResult().getOutput().getContent())
                .reduce("", String::concat);

        logger.info("Réponse brute d'Ollama : {}", fullResponse);

        return cleanResponse(fullResponse);
    }

    private String cleanResponse(String response) {
        if (response == null || response.isBlank()) {
            throw new RuntimeException("La réponse d'Ollama est vide ou nulle.");
        }
        // Supprimez les balises ``` et ```json
        response = response.replaceAll("(?i)```json", "").replaceAll("```", "").trim();
        return response;
    }

    private boolean isValidJson(String json) {
        try {
            objectMapper.readTree(json);
            return true;
        } catch (JsonProcessingException e) {
            logger.error("Validation JSON échouée : {}", e.getMessage());
            return false;
        }
    }

    private Signalement parseSignalementFromResponse(String jsonResponse, Utilisateur utilisateur) {
        try {
            // Parse uniquement les champs description et localisation
            Signalement partialSignalement = objectMapper.readValue(jsonResponse, Signalement.class);

            // Vérifier et appliquer une valeur par défaut pour la localisation
            if (partialSignalement.getLocalisation() == null || partialSignalement.getLocalisation().isBlank()) {
                logger.warn("La localisation est vide ou nulle, utilisation de la valeur par défaut : 'France'.");
                partialSignalement.setLocalisation("France");
            }

            // Créer un signalement avec les valeurs par défaut
            Signalement signalement = new Signalement();
            signalement.setDescription(partialSignalement.getDescription());
            signalement.setLocalisation(partialSignalement.getLocalisation());
            signalement.setStatut(Signalement.StatutSignalement.PENDING); // Toujours "PENDING" au début
            signalement.setCitoyen(utilisateur);

            // Sauvegarder le signalement
            return signalementRepository.save(signalement);
        } catch (JsonProcessingException e) {
            logger.error("Erreur lors du parsing JSON : {}", e.getMessage());
            throw new RuntimeException("Erreur lors de la génération du signalement : réponse JSON invalide.");
        }
    }
}
