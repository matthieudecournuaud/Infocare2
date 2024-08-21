import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { IIntervention } from '../intervention.model';

@Component({
  standalone: true,
  selector: 'jhi-intervention-detail',
  templateUrl: './intervention-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class InterventionDetailComponent {
  intervention = input<IIntervention | null>(null);

  previousState(): void {
    window.history.back();
  }
}
