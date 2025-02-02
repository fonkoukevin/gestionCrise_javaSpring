import { Component, OnInit } from '@angular/core';
import { HistoriqueService } from '../services/historique.service';
import { CommonModule } from '@angular/common';
import { NavbarComponent } from '../components/navbar/navbar.component';

@Component({
  selector: 'app-historique',
  templateUrl: './historique.component.html',
  styleUrls: ['./historique.component.scss'],
  imports: [
    CommonModule,
    NavbarComponent
  ]
})
export class HistoriqueComponent implements OnInit {
  utilisateurId: number | null = null;
  historique: { [key: string]: any[] } = {}; // Stockage structuré des crises et actions
  loading: boolean = false;

  constructor(private historiqueService: HistoriqueService) {}

  ngOnInit(): void {
    const storedUser = localStorage.getItem('utilisateur'); // ✅ On récupère l'utilisateur complet
    if (storedUser) {
      const utilisateur = JSON.parse(storedUser);
      this.utilisateurId = utilisateur.id; // ✅ On extrait l'ID
      console.log("✅ Utilisateur connecté, ID :", this.utilisateurId);
      this.loadHistorique();
    } else {
      console.error("❌ Aucun utilisateur connecté.");
    }
  }

  async loadHistorique(): Promise<void> {
    if (!this.utilisateurId) return;

    this.loading = true;
    try {
      this.historique = await this.historiqueService.getHistorique(this.utilisateurId);
      console.log("✅ Historique chargé :", this.historique);
    } catch (error) {
      console.error("❌ Erreur lors du chargement de l'historique :", error);
    } finally {
      this.loading = false;
    }
  }

  getCrises(): string[] {
    return Object.keys(this.historique);
  }
}
