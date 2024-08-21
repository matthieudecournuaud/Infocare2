import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IIntervention } from '../intervention.model';
import { InterventionService } from '../service/intervention.service';

@Component({
  standalone: true,
  templateUrl: './intervention-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class InterventionDeleteDialogComponent {
  intervention?: IIntervention;

  protected interventionService = inject(InterventionService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.interventionService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
