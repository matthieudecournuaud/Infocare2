import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IMaterial } from 'app/entities/material/material.model';
import { MaterialService } from 'app/entities/material/service/material.service';
import { ICategory } from 'app/entities/category/category.model';
import { CategoryService } from 'app/entities/category/service/category.service';
import { IStatus } from 'app/entities/status/status.model';
import { StatusService } from 'app/entities/status/service/status.service';
import { IPriority } from 'app/entities/priority/priority.model';
import { PriorityService } from 'app/entities/priority/service/priority.service';
import { TicketService } from '../service/ticket.service';
import { ITicket } from '../ticket.model';
import { TicketFormService, TicketFormGroup } from './ticket-form.service';

@Component({
  standalone: true,
  selector: 'jhi-ticket-update',
  templateUrl: './ticket-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class TicketUpdateComponent implements OnInit {
  isSaving = false;
  ticket: ITicket | null = null;

  materialsCollection: IMaterial[] = [];
  categoriesSharedCollection: ICategory[] = [];
  statusesSharedCollection: IStatus[] = [];
  prioritiesSharedCollection: IPriority[] = [];

  editForm: TicketFormGroup = this.ticketFormService.createTicketFormGroup();

  constructor(
    protected ticketService: TicketService,
    protected ticketFormService: TicketFormService,
    protected materialService: MaterialService,
    protected categoryService: CategoryService,
    protected statusService: StatusService,
    protected priorityService: PriorityService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  compareMaterial = (o1: IMaterial | null, o2: IMaterial | null): boolean => this.materialService.compareMaterial(o1, o2);

  compareCategory = (o1: ICategory | null, o2: ICategory | null): boolean => this.categoryService.compareCategory(o1, o2);

  compareStatus = (o1: IStatus | null, o2: IStatus | null): boolean => this.statusService.compareStatus(o1, o2);

  comparePriority = (o1: IPriority | null, o2: IPriority | null): boolean => this.priorityService.comparePriority(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ ticket }) => {
      this.ticket = ticket;
      if (ticket) {
        this.updateForm(ticket);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const ticket = this.ticketFormService.getTicket(this.editForm);
    if (ticket.id !== null) {
      this.subscribeToSaveResponse(this.ticketService.update(ticket));
    } else {
      this.subscribeToSaveResponse(this.ticketService.create(ticket));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITicket>>): void {
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

  protected updateForm(ticket: ITicket): void {
    this.ticket = ticket;
    this.ticketFormService.resetForm(this.editForm, ticket);

    this.materialsCollection = this.materialService.addMaterialToCollectionIfMissing<IMaterial>(this.materialsCollection, ticket.material);
    this.categoriesSharedCollection = this.categoryService.addCategoryToCollectionIfMissing<ICategory>(
      this.categoriesSharedCollection,
      ticket.category,
    );
    this.statusesSharedCollection = this.statusService.addStatusToCollectionIfMissing<IStatus>(
      this.statusesSharedCollection,
      ticket.status,
    );
    this.prioritiesSharedCollection = this.priorityService.addPriorityToCollectionIfMissing<IPriority>(
      this.prioritiesSharedCollection,
      ticket.priority,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.materialService
      .query({ filter: 'ticket-is-null' })
      .pipe(map((res: HttpResponse<IMaterial[]>) => res.body ?? []))
      .pipe(
        map((materials: IMaterial[]) => this.materialService.addMaterialToCollectionIfMissing<IMaterial>(materials, this.ticket?.material)),
      )
      .subscribe((materials: IMaterial[]) => (this.materialsCollection = materials));

    this.categoryService
      .query()
      .pipe(map((res: HttpResponse<ICategory[]>) => res.body ?? []))
      .pipe(
        map((categories: ICategory[]) =>
          this.categoryService.addCategoryToCollectionIfMissing<ICategory>(categories, this.ticket?.category),
        ),
      )
      .subscribe((categories: ICategory[]) => (this.categoriesSharedCollection = categories));

    this.statusService
      .query()
      .pipe(map((res: HttpResponse<IStatus[]>) => res.body ?? []))
      .pipe(map((statuses: IStatus[]) => this.statusService.addStatusToCollectionIfMissing<IStatus>(statuses, this.ticket?.status)))
      .subscribe((statuses: IStatus[]) => (this.statusesSharedCollection = statuses));

    this.priorityService
      .query()
      .pipe(map((res: HttpResponse<IPriority[]>) => res.body ?? []))
      .pipe(
        map((priorities: IPriority[]) =>
          this.priorityService.addPriorityToCollectionIfMissing<IPriority>(priorities, this.ticket?.priority),
        ),
      )
      .subscribe((priorities: IPriority[]) => (this.prioritiesSharedCollection = priorities));
  }
}
