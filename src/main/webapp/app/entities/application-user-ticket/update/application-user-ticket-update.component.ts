import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IApplicationUserTicket } from '../application-user-ticket.model';
import { ApplicationUserTicketService } from '../service/application-user-ticket.service';
import { ApplicationUserTicketFormService, ApplicationUserTicketFormGroup } from './application-user-ticket-form.service';

@Component({
  standalone: true,
  selector: 'jhi-application-user-ticket-update',
  templateUrl: './application-user-ticket-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class ApplicationUserTicketUpdateComponent implements OnInit {
  isSaving = false;
  applicationUserTicket: IApplicationUserTicket | null = null;

  editForm: ApplicationUserTicketFormGroup = this.applicationUserTicketFormService.createApplicationUserTicketFormGroup();

  constructor(
    protected applicationUserTicketService: ApplicationUserTicketService,
    protected applicationUserTicketFormService: ApplicationUserTicketFormService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ applicationUserTicket }) => {
      this.applicationUserTicket = applicationUserTicket;
      if (applicationUserTicket) {
        this.updateForm(applicationUserTicket);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const applicationUserTicket = this.applicationUserTicketFormService.getApplicationUserTicket(this.editForm);
    if (applicationUserTicket.id !== null) {
      this.subscribeToSaveResponse(this.applicationUserTicketService.update(applicationUserTicket));
    } else {
      this.subscribeToSaveResponse(this.applicationUserTicketService.create(applicationUserTicket));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IApplicationUserTicket>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(applicationUserTicket: IApplicationUserTicket): void {
    this.applicationUserTicket = applicationUserTicket;
    this.applicationUserTicketFormService.resetForm(this.editForm, applicationUserTicket);
  }
}
