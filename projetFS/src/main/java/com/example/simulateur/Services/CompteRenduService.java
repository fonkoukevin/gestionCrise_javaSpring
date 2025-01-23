package com.example.simulateur.Services;

import com.example.simulateur.Entites.Action;
import com.example.simulateur.Entites.CompteRendu;
import com.example.simulateur.Entites.Signalement;
import com.example.simulateur.Entites.Utilisateur;
import com.example.simulateur.Repositories.ActionRepository;
import com.example.simulateur.Repositories.CompteRenduRepository;
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

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompteRenduService {

    private static final Logger logger = LoggerFactory.getLogger(CompteRenduService.class);

    private final CompteRenduRepository compteRenduRepository;
    private final ActionRepository actionRepository;
    private final OllamaChatModel chatModel;

    /**
     * Génère un compte rendu basé sur les actions "DONE" et les informations du signalement.
     *
     * @param signalement Le signalement lié à la crise.
     * @param utilisateur L'utilisateur associé au signalement.
     * @return Le compte rendu généré.
     */
    public CompteRendu generateCompteRendu(Signalement signalement, Utilisateur utilisateur) {
        // Récupérer toutes les actions "DONE" associées au signalement
        List<Action> actionsDone = actionRepository.findBySignalementAndStatut(signalement, Action.StatutAction.DONE);

        // Construire un prompt succinct pour Ollama
        String prompt = buildPrompt(signalement, actionsDone);

        // Obtenir la réponse concise d'Ollama
        String response = sendPromptToOllama(prompt);

        // Créer le compte rendu
        CompteRendu compteRendu = new CompteRendu();
        compteRendu.setContenu(response);
        compteRendu.setSignalement(signalement);
        compteRendu.setCitoyen(utilisateur);

        // Sauvegarder le compte rendu dans la base de données
        return compteRenduRepository.save(compteRendu);
    }

    /**
     * Construit un prompt succinct pour Ollama.
     *
     * @param signalement Le signalement lié à la crise.
     * @param actions     Liste des actions "DONE" associées au signalement.
     * @return Le texte du prompt.
     */
    private String buildPrompt(Signalement signalement, List<Action> actions) {
        StringBuilder prompt = new StringBuilder();
        prompt.append("Résumé de la gestion de crise :\n")
                .append("- Description de la crise : ").append(signalement.getDescription()).append("\n")
                .append("- Localisation : ").append(signalement.getLocalisation()).append("\n")
                .append("- Actions réalisées :\n")
                .append(actions.stream()
                        .map(action -> "- " + action.getDescription())
                        .collect(Collectors.joining("\n")))
                .append("\n\n")
                .append("Veuillez générer un compte rendu en quelques lignes, contenant :\n")
                .append("1. Un résumé de la crise.\n")
                .append("2. Une liste succincte des actions effectuées.\n")
                .append("3. Un pourcentage de réussite basé sur la complétion des actions proposées.");

        return prompt.toString();
    }

    /**
     * Envoie un prompt à Ollama et récupère une réponse concise.
     *
     * @param prompt Le texte du prompt à envoyer.
     * @return La réponse d'Ollama.
     */
    private String sendPromptToOllama(String prompt) {
        List<Message> messages = List.of(
                new SystemMessage("Vous êtes un assistant expert en évaluation de gestion de crise. Répondez en quelques lignes."),
                new UserMessage(prompt)
        );

        Prompt ollamaPrompt = new Prompt(messages);

        // Stream la réponse d'Ollama
        Flux<ChatResponse> chatResponses = chatModel.stream(ollamaPrompt);

        // Collecter la réponse complète
        String fullResponse = chatResponses.collectList()
                .blockOptional()
                .orElse(List.of())
                .stream()
                .map(response -> response.getResult().getOutput().getContent())
                .collect(Collectors.joining());

        logger.info("Réponse brute d'Ollama : {}", fullResponse);
        return fullResponse.strip();
    }
}
