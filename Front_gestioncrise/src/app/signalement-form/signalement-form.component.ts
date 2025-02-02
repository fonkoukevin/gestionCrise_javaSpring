import { Component } from '@angular/core';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatOptionModule } from '@angular/material/core';
import { MatButtonModule } from '@angular/material/button';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-signalement-form',
  templateUrl: './signalement-form.component.html',
  styleUrls: ['./signalement-form.component.scss'],
  standalone: true,
  imports: [
    MatCardModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatOptionModule,
    MatButtonModule,
    FormsModule, // Nécessaire pour utiliser [(ngModel)]
  ],
})
export class SignalementFormComponent {
  description: string = '';
  localisation: string = '';
  statut: string = '';
}
