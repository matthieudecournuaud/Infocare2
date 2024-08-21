import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { IMaterial } from '../material.model';

@Component({
  standalone: true,
  selector: 'jhi-material-detail',
  templateUrl: './material-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class MaterialDetailComponent {
  material = input<IMaterial | null>(null);

  previousState(): void {
    window.history.back();
  }
}
