import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ICompany } from 'app/entities/company/company.model';
import { CompanyService } from 'app/entities/company/service/company.service';
import { ITicket } from 'app/entities/ticket/ticket.model';
import { TicketService } from 'app/entities/ticket/service/ticket.service';
import { MaterialService } from '../service/material.service';
import { IMaterial } from '../material.model';
import { MaterialFormService, MaterialFormGroup } from './material-form.service';

@Component({
  standalone: true,
  selector: 'jhi-material-update',
  templateUrl: './material-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class MaterialUpdateComponent implements OnInit {
  isSaving = false;
  material: IMaterial | null = null;

  companiesSharedCollection: ICompany[] = [];
  ticketsSharedCollection: ITicket[] = [];

  editForm: MaterialFormGroup = this.materialFormService.createMaterialFormGroup();

  constructor(
    protected materialService: MaterialService,
    protected materialFormService: MaterialFormService,
    protected companyService: CompanyService,
    protected ticketService: TicketService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  compareCompany = (o1: ICompany | null, o2: ICompany | null): boolean => this.companyService.compareCompany(o1, o2);

  compareTicket = (o1: ITicket | null, o2: ITicket | null): boolean => this.ticketService.compareTicket(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ material }) => {
      this.material = material;
      if (material) {
        this.updateForm(material);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const material = this.materialFormService.getMaterial(this.editForm);
    if (material.id !== null) {
      this.subscribeToSaveResponse(this.materialService.update(material));
    } else {
      this.subscribeToSaveResponse(this.materialService.create(material));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMaterial>>): void {
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

  protected updateForm(material: IMaterial): void {
    this.material = material;
    this.materialFormService.resetForm(this.editForm, material);

    this.companiesSharedCollection = this.companyService.addCompanyToCollectionIfMissing<ICompany>(
      this.companiesSharedCollection,
      material.company,
    );
    this.ticketsSharedCollection = this.ticketService.addTicketToCollectionIfMissing<ITicket>(
      this.ticketsSharedCollection,
      material.ticket,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.companyService
      .query()
      .pipe(map((res: HttpResponse<ICompany[]>) => res.body ?? []))
      .pipe(
        map((companies: ICompany[]) => this.companyService.addCompanyToCollectionIfMissing<ICompany>(companies, this.material?.company)),
      )
      .subscribe((companies: ICompany[]) => (this.companiesSharedCollection = companies));

    this.ticketService
      .query()
      .pipe(map((res: HttpResponse<ITicket[]>) => res.body ?? []))
      .pipe(map((tickets: ITicket[]) => this.ticketService.addTicketToCollectionIfMissing<ITicket>(tickets, this.material?.ticket)))
      .subscribe((tickets: ITicket[]) => (this.ticketsSharedCollection = tickets));
  }
}
