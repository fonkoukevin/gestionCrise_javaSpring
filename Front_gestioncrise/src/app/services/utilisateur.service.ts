import axios from 'axios';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root',
})
export class UtilisateurService {
  private baseUrl = 'http://localhost:8080/api/utilisateurs'; // URL de votre backend

  constructor() {}

  // Créer un utilisateur
  async createUtilisateur(utilisateur: any): Promise<any> {
    try {
      const response = await axios.post(this.baseUrl, utilisateur);
      return response.data;
    } catch (error) {
      console.error('Erreur lors de la création de l\'utilisateur', error);
      throw error;
    }
  }

  // Obtenir tous les utilisateurs
  async getAllUtilisateurs(): Promise<any> {
    try {
      const response = await axios.get(this.baseUrl);
      return response.data;
    } catch (error) {
      console.error('Erreur lors de la récupération des utilisateurs', error);
      throw error;
    }
  }
}
