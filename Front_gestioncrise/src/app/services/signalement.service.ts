import { Injectable } from '@angular/core';
import axios from 'axios';
import { BehaviorSubject } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class SignalementService {
  private baseUrl = 'http://localhost:8080/api/signalements';
  
  private latestSignalementSubject = new BehaviorSubject<any>(null);
  latestSignalement$ = this.latestSignalementSubject.asObservable();

  private updateLastSignalement(signalement: any) {
    localStorage.setItem('lastSignalement', JSON.stringify(signalement));
    this.latestSignalementSubject.next(signalement);
  }

  async generateSignalement(utilisateurId: number): Promise<any> {
    try {
      const response = await axios.post(`${this.baseUrl}/generate`, null, {
        params: { utilisateurId: utilisateurId.toString() },
      });

      console.log("✅ Signalement reçu du serveur :", response.data);

      // 🔥 Mise à jour de l'utilisateur stocké dans localStorage
      let utilisateur = JSON.parse(localStorage.getItem('utilisateur') || '{}');
      if (!utilisateur || !utilisateur.id) {
        console.warn("❌ Aucun utilisateur trouvé dans localStorage !");
        return response.data;
      }

      if (!utilisateur.signalements) {
        utilisateur.signalements = [];
      }
      
      utilisateur.signalements.push(response.data); // Ajout du nouveau signalement
      localStorage.setItem('utilisateur', JSON.stringify(utilisateur));

      // 🚀 Mise à jour du dernier signalement
      this.updateLastSignalement(response.data);
      return response.data;
    } catch (error) {
      console.error("❌ Erreur lors de la génération du signalement :", error);
      throw error;
    }
  }

  getLastSignalement(): any {
    const savedSignalement = localStorage.getItem('lastSignalement');
    return savedSignalement ? JSON.parse(savedSignalement) : null;
  }

  async refreshLatestSignalement(utilisateurId: number): Promise<void> {
    try {
      const response = await axios.get(`${this.baseUrl}/utilisateur/${utilisateurId}`);
      
      if (response.data.length > 0) {
        const latestSignalement = response.data[response.data.length - 1];
        console.log("✅ Dernier signalement mis à jour :", latestSignalement);
        this.updateLastSignalement(latestSignalement);
      } else {
        console.warn("❌ Aucun signalement trouvé pour cet utilisateur.");
        this.updateLastSignalement(null);
      }
    } catch (error) {
      console.error("❌ Erreur lors de la récupération du dernier signalement :", error);
    }
  }
  
}
