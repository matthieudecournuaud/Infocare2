import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IProcedure } from '../procedure.model';
import { ProcedureService } from '../service/procedure.service';

@Component({
  standalone: true,
  templateUrl: './procedure-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class ProcedureDeleteDialogComponent {
  procedure?: IProcedure;

  protected procedureService = inject(ProcedureService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.procedureService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
