import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ICompany } from 'app/entities/company/company.model';
import { CompanyService } from 'app/entities/company/service/company.service';
import { IMaterial } from '../material.model';
import { MaterialService } from '../service/material.service';
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

  companiesCollection: ICompany[] = [];

  protected materialService = inject(MaterialService);
  protected materialFormService = inject(MaterialFormService);
  protected companyService = inject(CompanyService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: MaterialFormGroup = this.materialFormService.createMaterialFormGroup();

  compareCompany = (o1: ICompany | null, o2: ICompany | null): boolean => this.companyService.compareCompany(o1, o2);

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

    this.companiesCollection = this.companyService.addCompanyToCollectionIfMissing<ICompany>(this.companiesCollection, material.company);
  }

  protected loadRelationshipsOptions(): void {
    this.companyService
      .query({ filter: 'material-is-null' })
      .pipe(map((res: HttpResponse<ICompany[]>) => res.body ?? []))
      .pipe(
        map((companies: ICompany[]) => this.companyService.addCompanyToCollectionIfMissing<ICompany>(companies, this.material?.company)),
      )
      .subscribe((companies: ICompany[]) => (this.companiesCollection = companies));
  }
}
