import { Component, Inject } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { FormsModule } from '@angular/forms'; // ✅ Import FormsModule for ngModel
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { ActionService } from '../services/action.service';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-add-action-dialog',
  templateUrl: './add-action-dialog.component.html',
  styleUrls: ['./add-action-dialog.component.scss'],
  imports: [
    CommonModule,
    FormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule
  ] // ✅ Import FormsModule
})
export class AddActionDialogComponent {
  actionDescription: string = '';
  signalementId: number;

  constructor(
    private actionService: ActionService,
    private dialogRef: MatDialogRef<AddActionDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any
  ) {
    this.signalementId = data.signalementId; // ✅ Correctly retrieve signalementId
    console.log("🆔 Signalement received in modal:", this.signalementId);
  }

  saveAction(): void {
    if (!this.signalementId || isNaN(Number(this.signalementId))) {
      alert("❌ Error: signalementId is invalid!");
      return;
    }

    if (!this.actionDescription.trim()) {
      alert('Please enter a description for the action.');
      return;
    }

    console.log("📤 Sending POST request with:", {
      description: this.actionDescription,
      statut: "PENDING"
    });

    this.actionService.addAction(this.signalementId, this.actionDescription)
      .then(() => {
        alert('✅ Action added successfully!');
        this.dialogRef.close(true);
      })
      .catch((err) => {
        console.error('❌ Error adding action:', err);
        alert('❌ An error occurred while adding the action.');
      });
  }
  close(): void {
    this.dialogRef.close();
  }
}
