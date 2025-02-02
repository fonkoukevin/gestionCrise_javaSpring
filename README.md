Presentation:

Ce projet est une application FullStack permettant la gestion de crises à travers la création de signalements par ollama , l'ajout d'actions, et la génération automatique de comptes rendus à l'aide l'ollama .

Technologies Utilisées:

    DB name: gestioncrise
    Backend : Spring Boot, Hibernate, MySQL
    Frontend : Angular 17 , TypeScript
    Base de Données : MySQL 
    API IA : llama3.2:1b

1🔹 Frontend (Angular)

Installer Angular CLI

Installe Angular CLI :

npm install -g @angular/cli

Vérifie que l’installation s’est bien passée :

ng version

cd gestionCrise_javaSpring
cd Front_gestioncrise

npm install
ng serve

lancer le projet : ng serve

2. Routes API (Spring Boot)
🔹 Inscription utilisateur

    POST /api/utilisateurs→ Créer un utilisateur
   
🔹 Gestion des Signalements

    POST /api/signalements/generate?utilisateurId=ID → Créer un signalement
    GET /api/signalements/{id} → Récupérer un signalement

🔹 Gestion des Actions

    GET /api/actions/signalement/{signalementId} → Lister les actions d’un signalement
    POST /api/actions/signalement/{signalementId} → Ajouter une action
    PUT /api/actions/{actionId}/status → Mettre à jour le statut d’une action
    DELETE /api/actions/{actionId} → Supprimer une action

🔹 Compte Rendu

    POST /api/compterendus/generate?signalementId=ID&utilisateurId=ID → Générer un compte rendu

🔹 Historique

    GET /api/historique/{utilisateurId} → Récupérer l’historique des crises d’un utilisateur


