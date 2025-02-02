import { CommonModule } from '@angular/common';
import { Component, Inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';

@Component({
  selector: 'app-edit-action-dialog',
  templateUrl: './edit-action-dialog.component.html',
  styleUrls: ['./edit-action-dialog.component.scss'],
  imports: [
    CommonModule,
       FormsModule,
        MatFormFieldModule,
        MatInputModule,
        MatButtonModule
  ]
})
export class EditActionDialogComponent {
  description: string;

  constructor(
    public dialogRef: MatDialogRef<EditActionDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any
  ) {
    this.description = data.description;
  }

  save(): void {
    this.dialogRef.close(this.description); // Retourne la nouvelle description
  }

  close(): void {
    this.dialogRef.close(null);
  }
}
