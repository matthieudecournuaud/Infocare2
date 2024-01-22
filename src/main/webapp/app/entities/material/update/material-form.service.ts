import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IMaterial, NewMaterial } from '../material.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IMaterial for edit and NewMaterialFormGroupInput for create.
 */
type MaterialFormGroupInput = IMaterial | PartialWithRequiredKeyOf<NewMaterial>;

type MaterialFormDefaults = Pick<NewMaterial, 'id'>;

type MaterialFormGroupContent = {
  id: FormControl<IMaterial['id'] | NewMaterial['id']>;
  name: FormControl<IMaterial['name']>;
  type: FormControl<IMaterial['type']>;
  purchaseDate: FormControl<IMaterial['purchaseDate']>;
  warrantyEndDate: FormControl<IMaterial['warrantyEndDate']>;
  manufacturer: FormControl<IMaterial['manufacturer']>;
  model: FormControl<IMaterial['model']>;
  statusMaterial: FormControl<IMaterial['statusMaterial']>;
  lastMaintenanceDate: FormControl<IMaterial['lastMaintenanceDate']>;
  note: FormControl<IMaterial['note']>;
  serialNumber: FormControl<IMaterial['serialNumber']>;
  company: FormControl<IMaterial['company']>;
};

export type MaterialFormGroup = FormGroup<MaterialFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class MaterialFormService {
  createMaterialFormGroup(material: MaterialFormGroupInput = { id: null }): MaterialFormGroup {
    const materialRawValue = {
      ...this.getFormDefaults(),
      ...material,
    };
    return new FormGroup<MaterialFormGroupContent>({
      id: new FormControl(
        { value: materialRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(materialRawValue.name, {
        validators: [Validators.required, Validators.maxLength(50)],
      }),
      type: new FormControl(materialRawValue.type, {
        validators: [Validators.required, Validators.maxLength(50)],
      }),
      purchaseDate: new FormControl(materialRawValue.purchaseDate),
      warrantyEndDate: new FormControl(materialRawValue.warrantyEndDate),
      manufacturer: new FormControl(materialRawValue.manufacturer, {
        validators: [Validators.maxLength(50)],
      }),
      model: new FormControl(materialRawValue.model, {
        validators: [Validators.maxLength(50)],
      }),
      statusMaterial: new FormControl(materialRawValue.statusMaterial, {
        validators: [Validators.maxLength(50)],
      }),
      lastMaintenanceDate: new FormControl(materialRawValue.lastMaintenanceDate),
      note: new FormControl(materialRawValue.note, {
        validators: [Validators.maxLength(500)],
      }),
      serialNumber: new FormControl(materialRawValue.serialNumber),
      company: new FormControl(materialRawValue.company),
    });
  }

  getMaterial(form: MaterialFormGroup): IMaterial | NewMaterial {
    return form.getRawValue() as IMaterial | NewMaterial;
  }

  resetForm(form: MaterialFormGroup, material: MaterialFormGroupInput): void {
    const materialRawValue = { ...this.getFormDefaults(), ...material };
    form.reset(
      {
        ...materialRawValue,
        id: { value: materialRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): MaterialFormDefaults {
    return {
      id: null,
    };
  }
}
