import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { CompteRenduService } from '../services/compte-rendu.service';
import { CommonModule } from '@angular/common';
import { NavbarComponent } from '../components/navbar/navbar.component';

@Component({
  selector: 'app-compte-rendu',
  templateUrl: './compte-rendu.component.html',
  styleUrls: ['./compte-rendu.component.scss'],
  imports: [
    CommonModule,
    NavbarComponent
  ]
})
export class CompteRenduComponent implements OnInit {
  signalementId: number | null = null;
  utilisateurId: number | null = null;
  compteRendu: any = null;
  loading: boolean = false;

  constructor(
    private route: ActivatedRoute,
    private compteRenduService: CompteRenduService
  ) {}

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      this.signalementId = +params['signalementId'];
    });

    // ✅ Récupérer l'utilisateur depuis `localStorage`
    const storedUser = localStorage.getItem('utilisateur');
    if (storedUser) {
      this.utilisateurId = JSON.parse(storedUser).id;
      console.log("✅ Utilisateur récupéré:", this.utilisateurId);
    } else {
      console.error("❌ Aucun utilisateur trouvé dans le localStorage !");
    }

    if (this.signalementId && this.utilisateurId) {
      this.genererCompteRendu(); // Générer le compte rendu
    }
  }

  genererCompteRendu(): void {
    if (!this.signalementId || !this.utilisateurId) {
      console.error("❌ Erreur : signalementId ou utilisateurId manquant !");
      return;
    }

    this.loading = true;

    this.compteRenduService.genererCompteRendu(this.signalementId, this.utilisateurId)
    .then(compteRendu => {
      console.log("✅ Compte Rendu généré et reçu :", compteRendu);
      this.compteRendu = compteRendu;
      this.loading = false;
    })
    .catch(error => {
      console.error("❌ Erreur lors de la génération du compte rendu :", error);
      this.loading = false;
    });
  }
}
