import { Injectable } from '@angular/core';
import axios from 'axios';

@Injectable({
  providedIn: 'root'
})
export class CompteRenduService {
  private apiUrl = 'http://localhost:8080/api/compterendus';

  async genererCompteRendu(signalementId: number, utilisateurId: number): Promise<any> {
    try {
      const response = await axios.post(`${this.apiUrl}/generate`, null, {
        params: { signalementId, utilisateurId }
      });
      return response.data;
    } catch (error) {
      console.error("❌ Erreur lors de l'appel API de génération du compte rendu :", error);
      throw error;
    }
  }
}
