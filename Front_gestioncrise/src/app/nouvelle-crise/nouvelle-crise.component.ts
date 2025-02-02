import { Component } from '@angular/core';
import {MatCard} from '@angular/material/card';
import {NgForOf} from '@angular/common';

@Component({
  selector: 'app-nouvelle-crise',
  templateUrl: './nouvelle-crise.component.html',
  styleUrls: ['./nouvelle-crise.component.scss'],
  imports: [
    MatCard,
    NgForOf
  ]
})
export class NouvelleCriseComponent {
  // Exemple de crise simulée
  crise = {
    title: 'Explosion chimique dans une usine',
    description:
      'Une explosion s’est produite dans une usine de produits chimiques, entraînant une fuite toxique dans les environs.',
    impacts: [
      'Risque pour la santé des employés et des habitants proches.',
      'Contamination de l’air et des eaux locales.',
      'Évacuation d’urgence requise dans un rayon de 5 km.',
    ],
    actions: [
      'Mettre en place un périmètre de sécurité immédiat.',
      'Contacter les équipes de gestion des matières dangereuses.',
      'Informer les habitants des mesures de sécurité à suivre.',
    ],
  };
}
