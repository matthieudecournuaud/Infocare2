import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IIntervention, NewIntervention } from '../intervention.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IIntervention for edit and NewInterventionFormGroupInput for create.
 */
type InterventionFormGroupInput = IIntervention | PartialWithRequiredKeyOf<NewIntervention>;

type InterventionFormDefaults = Pick<NewIntervention, 'id'>;

type InterventionFormGroupContent = {
  id: FormControl<IIntervention['id'] | NewIntervention['id']>;
  title: FormControl<IIntervention['title']>;
  description: FormControl<IIntervention['description']>;
  createdBy: FormControl<IIntervention['createdBy']>;
  createdAt: FormControl<IIntervention['createdAt']>;
  attachments: FormControl<IIntervention['attachments']>;
  notes: FormControl<IIntervention['notes']>;
  procedure: FormControl<IIntervention['procedure']>;
  ticket: FormControl<IIntervention['ticket']>;
};

export type InterventionFormGroup = FormGroup<InterventionFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class InterventionFormService {
  createInterventionFormGroup(intervention: InterventionFormGroupInput = { id: null }): InterventionFormGroup {
    const interventionRawValue = {
      ...this.getFormDefaults(),
      ...intervention,
    };
    return new FormGroup<InterventionFormGroupContent>({
      id: new FormControl(
        { value: interventionRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      title: new FormControl(interventionRawValue.title),
      description: new FormControl(interventionRawValue.description),
      createdBy: new FormControl(interventionRawValue.createdBy, {
        validators: [Validators.required],
      }),
      createdAt: new FormControl(interventionRawValue.createdAt, {
        validators: [Validators.required],
      }),
      attachments: new FormControl(interventionRawValue.attachments, {
        validators: [Validators.maxLength(5000)],
      }),
      notes: new FormControl(interventionRawValue.notes, {
        validators: [Validators.maxLength(500)],
      }),
      procedure: new FormControl(interventionRawValue.procedure),
      ticket: new FormControl(interventionRawValue.ticket),
    });
  }

  getIntervention(form: InterventionFormGroup): IIntervention | NewIntervention {
    return form.getRawValue() as IIntervention | NewIntervention;
  }

  resetForm(form: InterventionFormGroup, intervention: InterventionFormGroupInput): void {
    const interventionRawValue = { ...this.getFormDefaults(), ...intervention };
    form.reset(
      {
        ...interventionRawValue,
        id: { value: interventionRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): InterventionFormDefaults {
    return {
      id: null,
    };
  }
}
