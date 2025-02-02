import axios from 'axios';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root',
})
export class ActionService {
  private baseUrl = 'http://localhost:8080/api/actions';
  private signalementBaseUrl = 'http://localhost:8080/api/signalements';

  getActionsBySignalement(signalementId: number): Promise<any[]> {
    console.log("🔍 Récupération des actions pour le signalement ID :", signalementId);
    return axios.get(`${this.baseUrl}/signalement/${signalementId}`)
      .then(response => {
        console.log("📥 Données reçues du serveur :", response.data);
        return response.data;
      })
      .catch(error => {
        console.error("❌ Erreur lors de la récupération des actions :", error);
        return [];
      });
  }
  
  
  getSignalementById(signalementId: number): Promise<any> {
    return axios
      .get(`${this.signalementBaseUrl}/${signalementId}`) // ✅ Correction ici
      .then(response => response.data)
      .catch(error => {
        console.error('❌ Erreur lors de la récupération du signalement:', error);
        throw error;
      });
  }
  
  addAction(signalementId: number, actionDescription: string) {
    if (!signalementId || isNaN(Number(signalementId))) {
      console.error("❌ signalementId est invalide :", signalementId);
      return Promise.reject("signalementId est invalide");
    }

    console.log("📤 Envoi de la requête POST avec :", {
      description: actionDescription,
      statut: "PENDING"
    });

    return axios.post(`${this.baseUrl}/signalement/${signalementId}`, {
      description: actionDescription,
      statut: "PENDING",
    });
  }

  updateActionStatus(actionId: number, newStatus: string): Promise<any> {
    return axios.put(`http://localhost:8080/api/actions/${actionId}/status`, {
        statut: newStatus
    })
    .then(response => response.data)
    .catch(error => {
        console.error("❌ Erreur lors de la mise à jour du statut :", error);
        throw error;
    });
}

deleteAction(actionId: number): Promise<void> {
  return axios.delete(`http://localhost:8080/api/actions/${actionId}`)
    .then(() => console.log(`🗑 Action #${actionId} supprimée`))
    .catch(error => {
      console.error("❌ Erreur lors de la suppression de l'action :", error);
      throw error;
    });
}

updateAction(actionId: number, newDescription: string): Promise<any> {
  return axios.put(`http://localhost:8080/api/actions/${actionId}`, { description: newDescription })
    .then(response => response.data)
    .catch(error => {
      console.error("❌ Erreur lors de la mise à jour de l'action :", error);
      throw error;
    });
}

  
}
