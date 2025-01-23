package com.example.simulateur.Services;

import com.example.simulateur.Entites.Action;
import com.example.simulateur.Entites.Signalement;
import com.example.simulateur.Entites.Utilisateur;
import com.example.simulateur.Repositories.ActionRepository;
import com.fasterxml.jackson.core.type.TypeReference;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OllamaService {

    private static final Logger logger = LoggerFactory.getLogger(OllamaService.class);

    private final OllamaChatModel chatModel;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final ActionRepository actionRepository;

    /**
     * Génère et sauvegarde une liste d'actions à partir d'un signalement.
     *
     * @param signalement Le signalement contenant les détails de la crise.
     * @param utilisateur L'utilisateur qui a signalé la crise.
     * @return Une liste d'actions sauvegardées dans la base de données.
     */
    public List<Action> generateAndSaveActions(Signalement signalement, Utilisateur utilisateur) {
        // Générer les actions via Ollama
        List<Action> actions = generateActions(signalement, utilisateur);

        // Sauvegarder les actions dans la base de données
        List<Action> savedActions = actionRepository.saveAll(actions);
        logger.info("Actions sauvegardées avec succès : {}", savedActions);

        return savedActions;
    }

    /**
     * Génère une liste d'actions à partir d'un signalement sans les sauvegarder.
     *
     * @param signalement Le signalement contenant les détails de la crise.
     * @param utilisateur L'utilisateur qui a signalé la crise.
     * @return Une liste d'actions générées par Ollama.
     */
    private List<Action> generateActions(Signalement signalement, Utilisateur utilisateur) {
        String prompt = buildPrompt(signalement);

        List<Message> messages = new ArrayList<>();
        messages.add(new SystemMessage("Vous êtes un assistant spécialisé dans la gestion des crises."));
        messages.add(new UserMessage(prompt));

        Prompt promptToSend = new Prompt(messages);

        Flux<ChatResponse> chatResponses = chatModel.stream(promptToSend);

        String fullResponse = chatResponses.collectList()
                .blockOptional()
                .orElse(new ArrayList<>())
                .stream()
                .map(response -> response.getResult().getOutput().getContent())
                .collect(Collectors.joining());

        logger.info("Réponse brute d'Ollama : {}", fullResponse);

        String cleanedResponse = cleanResponse(fullResponse);

        return parseActionsFromJson(cleanedResponse, signalement, utilisateur);
    }

    /**
     * Construit le prompt à envoyer à Ollama.
     *
     * @param signalement Le signalement contenant les détails de la crise.
     * @return Le texte du prompt.
     */
    private String buildPrompt(Signalement signalement) {
        return "Une crise a été signalée avec les informations suivantes :\n" +
                "- Description : " + signalement.getDescription() + "\n" +
                "- Localisation : " + signalement.getLocalisation() + "\n" +
                "- Statut : " + signalement.getStatut() + "\n\n" +
                "Veuillez fournir une liste d'actions au format JSON comme suit :\n" +
                "[{\"action\": \"Action 1\"}, {\"action\": \"Action 2\"}]. " +
                "Ne fournissez que le JSON, sans texte supplémentaire.";
    }

    /**
     * Nettoie la réponse brute reçue d'Ollama.
     *
     * @param response La réponse brute.
     * @return La réponse nettoyée.
     */
    private String cleanResponse(String response) {
        if (response == null || response.isBlank()) {
            return "";
        }
        response = response.replaceAll("```", "").trim();
        response = response.strip();
        logger.info("Réponse nettoyée : {}", response);
        return response;
    }

    /**
     * Parse une réponse JSON en une liste d'actions.
     *
     * @param jsonResponse La réponse JSON contenant les actions.
     * @param signalement  Le signalement associé aux actions.
     * @param utilisateur  L'utilisateur qui a signalé la crise.
     * @return Une liste d'actions.
     */
    private List<Action> parseActionsFromJson(String jsonResponse, Signalement signalement, Utilisateur utilisateur) {
        try {
            List<ActionJson> actionJsonList = objectMapper.readValue(jsonResponse, new TypeReference<>() {});

            return actionJsonList.stream()
                    .map(actionJson -> {
                        Action action = new Action();
                        action.setDescription(actionJson.getAction());
                        action.setSignalement(signalement);
                        action.setAssignee(utilisateur); // Attribuer l'utilisateur
                        action.setStatut(Action.StatutAction.PENDING);
                        return action;
                    })
                    .collect(Collectors.toList());
        } catch (IOException e) {
            logger.error("Erreur lors du parsing JSON : {}", e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Classe interne représentant une action JSON.
     */
    private static class ActionJson {
        private String action;

        public String getAction() {
            return action;
        }

        public void setAction(String action) {
            this.action = action;
        }

        @Override
        public String toString() {
            return "ActionJson{" +
                    "action='" + action + '\'' +
                    '}';
        }
    }
}
