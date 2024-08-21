import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IProcedure } from '../procedure.model';
import { ProcedureService } from '../service/procedure.service';
import { ProcedureFormService, ProcedureFormGroup } from './procedure-form.service';

@Component({
  standalone: true,
  selector: 'jhi-procedure-update',
  templateUrl: './procedure-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class ProcedureUpdateComponent implements OnInit {
  isSaving = false;
  procedure: IProcedure | null = null;

  protected procedureService = inject(ProcedureService);
  protected procedureFormService = inject(ProcedureFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ProcedureFormGroup = this.procedureFormService.createProcedureFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ procedure }) => {
      this.procedure = procedure;
      if (procedure) {
        this.updateForm(procedure);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const procedure = this.procedureFormService.getProcedure(this.editForm);
    if (procedure.id !== null) {
      this.subscribeToSaveResponse(this.procedureService.update(procedure));
    } else {
      this.subscribeToSaveResponse(this.procedureService.create(procedure));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IProcedure>>): void {
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

  protected updateForm(procedure: IProcedure): void {
    this.procedure = procedure;
    this.procedureFormService.resetForm(this.editForm, procedure);
  }
}
