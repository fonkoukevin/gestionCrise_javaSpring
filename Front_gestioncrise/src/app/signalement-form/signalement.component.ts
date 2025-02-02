import { Component, OnInit, OnDestroy } from '@angular/core';
import { SignalementService } from '../services/signalement.service';
import { Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { CommonModule } from '@angular/common';
import { NavbarComponent } from '../components/navbar/navbar.component';

@Component({
  selector: 'app-signalement',
  templateUrl: './signalement.component.html',
  styleUrls: ['./signalement.component.css'],
  imports: [
    CommonModule,
    NavbarComponent
  ]
})
export class SignalementComponent implements OnInit, OnDestroy {
  signalement: any = null;
  private signalementSubscription!: Subscription;

  constructor(private signalementService: SignalementService, private router: Router) {}

  ngOnInit(): void {
    // ✅ Écoute des mises à jour en temps réel
    this.signalementSubscription = this.signalementService.latestSignalement$.subscribe((signalement) => {
      if (signalement) {
        console.log("🔍 Signalement mis à jour :", signalement);
        this.signalement = signalement;
      } else {
        this.signalement = this.signalementService.getLastSignalement();
        console.log("📦 Signalement récupéré depuis localStorage :", this.signalement);
      }
    });
  }

  // ✅ Méthode pour naviguer vers la page des actions
  voirActions(signalementId: number) {
    if (signalementId) {
      this.router.navigate(['/actions', signalementId]);
    } else {
      console.warn("❌ Signalement ID manquant !");
    }
  }

  ngOnDestroy(): void {
    this.signalementSubscription.unsubscribe();
  }
}
