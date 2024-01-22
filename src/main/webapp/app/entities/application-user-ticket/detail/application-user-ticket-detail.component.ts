import { Component, Input } from '@angular/core';
import { ActivatedRoute, RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { IApplicationUserTicket } from '../application-user-ticket.model';

@Component({
  standalone: true,
  selector: 'jhi-application-user-ticket-detail',
  templateUrl: './application-user-ticket-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class ApplicationUserTicketDetailComponent {
  @Input() applicationUserTicket: IApplicationUserTicket | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  previousState(): void {
    window.history.back();
  }
}
