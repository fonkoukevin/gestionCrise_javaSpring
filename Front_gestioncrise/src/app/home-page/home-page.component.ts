import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { SignalementService } from '../services/signalement.service';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { CommonModule } from '@angular/common';
import { NavbarComponent } from '../components/navbar/navbar.component';

@Component({
  selector: 'app-home-page',
  templateUrl: './home-page.component.html',
  styleUrls: ['./home-page.component.scss'],
  standalone: true,
  imports: [
    CommonModule,
    MatCardModule,
    MatButtonModule,
    NavbarComponent
  ]
})
export class HomePageComponent implements OnInit {
  utilisateurId: number | null = null;

  constructor(
    private route: ActivatedRoute,
    private signalementService: SignalementService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.route.queryParams.subscribe(params => {
      this.utilisateurId = params['utilisateurId'] ? +params['utilisateurId'] : null;
    });
  }

  // Générer le signalement et rediriger vers /signalement
  generateSignalement(): void {
    if (!this.utilisateurId) {
      console.error("Erreur : L'ID utilisateur est manquant.");
      return;
    }

    this.signalementService.generateSignalement(this.utilisateurId)
      .then(() => {
        // Rediriger vers la page signalement après génération
        this.router.navigate(['/signalement']);
      })
      .catch(err => {
        console.error('Erreur lors de la génération du signalement:', err);
      });
  }
}
