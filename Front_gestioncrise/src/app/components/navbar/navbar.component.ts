import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { Router, RouterModule, NavigationEnd } from '@angular/router';
import { SignalementService } from '../../services/signalement.service';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.scss'],
  imports: [
    RouterModule,
    CommonModule
  ]
})
export class NavbarComponent implements OnInit {
  latestSignalementId: number | null = null;
  currentRoute: string = '';

  constructor(private router: Router, private signalementService: SignalementService) {}

  ngOnInit(): void {
    this.loadLatestSignalement();

    // Écoute des changements de route pour mettre à jour `currentRoute`
    this.router.events.subscribe(event => {
      if (event instanceof NavigationEnd) {
        this.currentRoute = event.url;
        console.log("🔍 Route actuelle :", this.currentRoute);
      }
    });
  }

  loadLatestSignalement(): void {
    const utilisateur = JSON.parse(localStorage.getItem('utilisateur') || '{}');

    if (utilisateur && utilisateur.signalements && utilisateur.signalements.length > 0) {
      this.latestSignalementId = utilisateur.signalements[utilisateur.signalements.length - 1].id;
      console.log("✅ Dernier signalement trouvé :", this.latestSignalementId);
    } else {
      console.warn("❌ Aucun signalement trouvé pour l'utilisateur connecté.");
    }
  }

  logout(): void {
    localStorage.removeItem('utilisateur');
    localStorage.removeItem('lastSignalement');
    this.router.navigate(['/login']);
  }

  goToActions() {
    if (this.latestSignalementId) {
      console.log("🔄 Navigation vers Actions avec signalementId :", this.latestSignalementId);
      this.router.navigate(['/actions', this.latestSignalementId]);
    } else {
      console.warn("❌ Aucun signalement disponible.");
    }
  }
  

  goToHome() {
    const utilisateur = JSON.parse(localStorage.getItem("utilisateur") || "{}");
  
    if (utilisateur && utilisateur.id) {
      console.log("🔄 Redirection vers Home avec utilisateurId :", utilisateur.id);
      this.router.navigate(['/home'], {
        queryParams: { utilisateurId: utilisateur.id }
      });
    } else {
      console.warn("❌ Aucun utilisateur connecté, redirection vers login.");
      this.router.navigate(['/login']);
    }
  }
  
}
