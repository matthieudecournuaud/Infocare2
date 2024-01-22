import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IProcedure } from 'app/entities/procedure/procedure.model';
import { ProcedureService } from 'app/entities/procedure/service/procedure.service';
import { ITicket } from 'app/entities/ticket/ticket.model';
import { TicketService } from 'app/entities/ticket/service/ticket.service';
import { InterventionService } from '../service/intervention.service';
import { IIntervention } from '../intervention.model';
import { InterventionFormService, InterventionFormGroup } from './intervention-form.service';

@Component({
  standalone: true,
  selector: 'jhi-intervention-update',
  templateUrl: './intervention-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class InterventionUpdateComponent implements OnInit {
  isSaving = false;
  intervention: IIntervention | null = null;

  proceduresCollection: IProcedure[] = [];
  ticketsSharedCollection: ITicket[] = [];

  editForm: InterventionFormGroup = this.interventionFormService.createInterventionFormGroup();

  constructor(
    protected interventionService: InterventionService,
    protected interventionFormService: InterventionFormService,
    protected procedureService: ProcedureService,
    protected ticketService: TicketService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  compareProcedure = (o1: IProcedure | null, o2: IProcedure | null): boolean => this.procedureService.compareProcedure(o1, o2);

  compareTicket = (o1: ITicket | null, o2: ITicket | null): boolean => this.ticketService.compareTicket(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ intervention }) => {
      this.intervention = intervention;
      if (intervention) {
        this.updateForm(intervention);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const intervention = this.interventionFormService.getIntervention(this.editForm);
    if (intervention.id !== null) {
      this.subscribeToSaveResponse(this.interventionService.update(intervention));
    } else {
      this.subscribeToSaveResponse(this.interventionService.create(intervention));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IIntervention>>): void {
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

  protected updateForm(intervention: IIntervention): void {
    this.intervention = intervention;
    this.interventionFormService.resetForm(this.editForm, intervention);

    this.proceduresCollection = this.procedureService.addProcedureToCollectionIfMissing<IProcedure>(
      this.proceduresCollection,
      intervention.procedure,
    );
    this.ticketsSharedCollection = this.ticketService.addTicketToCollectionIfMissing<ITicket>(
      this.ticketsSharedCollection,
      intervention.ticket,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.procedureService
      .query({ filter: 'intervention-is-null' })
      .pipe(map((res: HttpResponse<IProcedure[]>) => res.body ?? []))
      .pipe(
        map((procedures: IProcedure[]) =>
          this.procedureService.addProcedureToCollectionIfMissing<IProcedure>(procedures, this.intervention?.procedure),
        ),
      )
      .subscribe((procedures: IProcedure[]) => (this.proceduresCollection = procedures));

    this.ticketService
      .query()
      .pipe(map((res: HttpResponse<ITicket[]>) => res.body ?? []))
      .pipe(map((tickets: ITicket[]) => this.ticketService.addTicketToCollectionIfMissing<ITicket>(tickets, this.intervention?.ticket)))
      .subscribe((tickets: ITicket[]) => (this.ticketsSharedCollection = tickets));
  }
}
