import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IProcedure, NewProcedure } from '../procedure.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IProcedure for edit and NewProcedureFormGroupInput for create.
 */
type ProcedureFormGroupInput = IProcedure | PartialWithRequiredKeyOf<NewProcedure>;

type ProcedureFormDefaults = Pick<NewProcedure, 'id'>;

type ProcedureFormGroupContent = {
  id: FormControl<IProcedure['id'] | NewProcedure['id']>;
  name: FormControl<IProcedure['name']>;
  description: FormControl<IProcedure['description']>;
  category: FormControl<IProcedure['category']>;
  procedureId: FormControl<IProcedure['procedureId']>;
  stepByStepGuide: FormControl<IProcedure['stepByStepGuide']>;
  estimatedTime: FormControl<IProcedure['estimatedTime']>;
  requiredTools: FormControl<IProcedure['requiredTools']>;
  skillsRequired: FormControl<IProcedure['skillsRequired']>;
  safetyInstructions: FormControl<IProcedure['safetyInstructions']>;
  lastReviewed: FormControl<IProcedure['lastReviewed']>;
  reviewedBy: FormControl<IProcedure['reviewedBy']>;
  attachments: FormControl<IProcedure['attachments']>;
};

export type ProcedureFormGroup = FormGroup<ProcedureFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ProcedureFormService {
  createProcedureFormGroup(procedure: ProcedureFormGroupInput = { id: null }): ProcedureFormGroup {
    const procedureRawValue = {
      ...this.getFormDefaults(),
      ...procedure,
    };
    return new FormGroup<ProcedureFormGroupContent>({
      id: new FormControl(
        { value: procedureRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(procedureRawValue.name),
      description: new FormControl(procedureRawValue.description),
      category: new FormControl(procedureRawValue.category),
      procedureId: new FormControl(procedureRawValue.procedureId),
      stepByStepGuide: new FormControl(procedureRawValue.stepByStepGuide, {
        validators: [Validators.maxLength(5000)],
      }),
      estimatedTime: new FormControl(procedureRawValue.estimatedTime),
      requiredTools: new FormControl(procedureRawValue.requiredTools, {
        validators: [Validators.maxLength(500)],
      }),
      skillsRequired: new FormControl(procedureRawValue.skillsRequired, {
        validators: [Validators.maxLength(500)],
      }),
      safetyInstructions: new FormControl(procedureRawValue.safetyInstructions, {
        validators: [Validators.maxLength(500)],
      }),
      lastReviewed: new FormControl(procedureRawValue.lastReviewed),
      reviewedBy: new FormControl(procedureRawValue.reviewedBy, {
        validators: [Validators.maxLength(50)],
      }),
      attachments: new FormControl(procedureRawValue.attachments, {
        validators: [Validators.maxLength(200)],
      }),
    });
  }

  getProcedure(form: ProcedureFormGroup): IProcedure | NewProcedure {
    return form.getRawValue() as IProcedure | NewProcedure;
  }

  resetForm(form: ProcedureFormGroup, procedure: ProcedureFormGroupInput): void {
    const procedureRawValue = { ...this.getFormDefaults(), ...procedure };
    form.reset(
      {
        ...procedureRawValue,
        id: { value: procedureRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): ProcedureFormDefaults {
    return {
      id: null,
    };
  }
}
