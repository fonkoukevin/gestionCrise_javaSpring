import { Injectable } from '@angular/core';
import axios from 'axios';

@Injectable({
  providedIn: 'root'
})
export class HistoriqueService {

  private apiUrl = 'http://localhost:8080/api/historique';

  constructor() {}

  async getHistorique(utilisateurId: number): Promise<any> {
    try {
      const response = await axios.get(`${this.apiUrl}/${utilisateurId}`);
      console.log("📥 Historique reçu :", response.data);
      return response.data;
    } catch (error) {
      console.error("❌ Erreur lors de la récupération de l'historique :", error);
      throw error;
    }
  }
}
