import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { UtilisateurService } from '../services/utilisateur.service';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-login-page',
  templateUrl: './login-page.component.html',
  styleUrls: ['./login-page.component.scss'],
  imports: [
    FormsModule,
    CommonModule
  ]
})
export class LoginPageComponent {
  utilisateurs: any[] = [];
  utilisateurForm = {
    username: '',
    email: '',
    password: '',
  };

  constructor(
    private utilisateurService: UtilisateurService,
    private router: Router
  ) {}

  async login(): Promise<void> {
    try {
      // Création de l'utilisateur
      const newUtilisateur = await this.utilisateurService.createUtilisateur(this.utilisateurForm);

      // Stocker l'utilisateur dans le localStorage
      localStorage.setItem('utilisateur', JSON.stringify(newUtilisateur));

      console.log('✅ Utilisateur créé et connecté:', newUtilisateur);

      // Rediriger immédiatement vers Home
      this.router.navigate(['/home'], { queryParams: { utilisateurId: newUtilisateur.id } });
    } catch (error) {
      console.error("❌ Erreur lors de la connexion:", error);
    }
  }

  async loadUtilisateurs(): Promise<void> {
    try {
      this.utilisateurs = await this.utilisateurService.getAllUtilisateurs();
    } catch (error) {
      console.error("❌ Erreur lors du chargement des utilisateurs:", error);
    }
  }

  ngOnInit(): void {
    this.loadUtilisateurs();
  }
}
