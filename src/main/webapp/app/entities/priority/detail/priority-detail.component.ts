import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { IPriority } from '../priority.model';

@Component({
  standalone: true,
  selector: 'jhi-priority-detail',
  templateUrl: './priority-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class PriorityDetailComponent {
  priority = input<IPriority | null>(null);

  previousState(): void {
    window.history.back();
  }
}
