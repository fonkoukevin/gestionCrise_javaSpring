import { Component } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { MatDialog } from '@angular/material/dialog';
import { ActionService } from '../services/action.service';
import { AddActionDialogComponent } from './add-action-dialog.component';
import { CommonModule } from '@angular/common';
import { EditActionDialogComponent } from './edit-action-dialog.component';
import { NavbarComponent } from '../components/navbar/navbar.component';

@Component({
  selector: 'app-actions',
  templateUrl: './actions.component.html',
  styleUrls: ['./actions.component.scss'],
  imports: [
    CommonModule,
    NavbarComponent
  ]
})
export class ActionsComponent {
  actions: any[] = [];
  signalementId: number | null = null;
  signalementNom: string = ''; // Déclaration de la propriété signalementNom

  constructor(
    private actionService: ActionService,
    private route: ActivatedRoute,
    private dialog: MatDialog,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      this.signalementId = +params['signalementId'];
      console.log("📥 Signalement ID récupéré dans ActionsComponent :", this.signalementId);

      if (this.signalementId) {
        this.loadActions(this.signalementId);
        this.loadSignalementNom(this.signalementId); // Charger le nom du signalement
      }
    });
  }

  loadActions(signalementId: number): void {
    this.actionService.getActionsBySignalement(signalementId)
    .then(actions => {
      console.log("✅ Actions reçues :", actions);
      if (Array.isArray(actions)) {
        this.actions = actions;
      } else {
        console.error("❌ Les données reçues ne sont pas un tableau :", actions);
        this.actions = [];
      }
    })
    .catch(error => console.error("❌ Erreur lors de la récupération des actions :", error));
  
  }
  
  loadSignalementNom(signalementId: number): void {
    this.actionService.getSignalementById(signalementId)
      .then(signalement => {
        this.signalementNom = signalement.description; // ✅ Correction ici
        console.log("✅ Nom du signalement reçu :", this.signalementNom);
      })
      .catch(error => console.error("❌ Erreur lors de la récupération du nom du signalement :", error));
}

  openAddActionDialog(): void {
    if (!this.signalementId) {
      alert("❌ Erreur : Aucun signalement sélectionné !");
      return;
    }

    const dialogRef = this.dialog.open(AddActionDialogComponent, {
      width: '400px',
      data: { signalementId: this.signalementId } // Passage du signalementId
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.loadActions(this.signalementId!); // Recharge les actions après ajout
      }
    });
  }

  toggleActionStatus(action: any): void {
    const newStatus = action.statut === "DONE" ? "PENDING" : "DONE";

    this.actionService.updateActionStatus(action.id, newStatus)
        .then(updatedAction => {
            action.statut = updatedAction.statut; // Mise à jour locale après succès
            console.log(`✅ Statut de l'action #${action.id} mis à jour : ${action.statut}`);
        })
        .catch(error => console.error("❌ Erreur lors de la mise à jour du statut de l'action :", error));
}

deleteAction(actionId: number): void {
  if (!confirm("❌ Voulez-vous vraiment supprimer cette action ?")) {
    return;
  }

  this.actionService.deleteAction(actionId)
    .then(() => {
      this.actions = this.actions.filter(action => action.id !== actionId); // Mise à jour locale
      console.log(`🗑 Action #${actionId} supprimée avec succès.`);
    })
    .catch(error => console.error("❌ Erreur lors de la suppression de l'action :", error));
}
openEditActionDialog(action: any): void {
  const dialogRef = this.dialog.open(EditActionDialogComponent, {
    width: '400px',
    data: { description: action.description }
  });

  dialogRef.afterClosed().subscribe(result => {
    if (result !== null) {
      this.updateAction(action.id, result);
    }
  });
}

updateAction(actionId: number, newDescription: string): void {
  this.actionService.updateAction(actionId, newDescription)
    .then(updatedAction => {
      const action = this.actions.find(a => a.id === actionId);
      if (action) {
        action.description = updatedAction.description; // Mise à jour locale
      }
      console.log(`✅ Action #${actionId} mise à jour : ${updatedAction.description}`);
    })
    .catch(error => console.error("❌ Erreur lors de la mise à jour de l'action :", error));
}

goToCompteRendu(): void {
  if (!this.signalementId) {
    alert("❌ Erreur : Aucun signalement sélectionné !");
    return;
  }

  this.router.navigate(['/compte-rendu', this.signalementId]);
}
  
}
