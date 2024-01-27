import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { ICompany, NewCompany } from '../company.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ICompany for edit and NewCompanyFormGroupInput for create.
 */
type CompanyFormGroupInput = ICompany | PartialWithRequiredKeyOf<NewCompany>;

type CompanyFormDefaults = Pick<NewCompany, 'id'>;

type CompanyFormGroupContent = {
  id: FormControl<ICompany['id'] | NewCompany['id']>;
  name: FormControl<ICompany['name']>;
  phone: FormControl<ICompany['phone']>;
  siret: FormControl<ICompany['siret']>;
  address: FormControl<ICompany['address']>;
  email: FormControl<ICompany['email']>;
  contactPerson: FormControl<ICompany['contactPerson']>;
  contactPersonPhone: FormControl<ICompany['contactPersonPhone']>;
  contactPersonEmail: FormControl<ICompany['contactPersonEmail']>;
  size: FormControl<ICompany['size']>;
  notes: FormControl<ICompany['notes']>;
};

export type CompanyFormGroup = FormGroup<CompanyFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class CompanyFormService {
  createCompanyFormGroup(company: CompanyFormGroupInput = { id: null }): CompanyFormGroup {
    const companyRawValue = {
      ...this.getFormDefaults(),
      ...company,
    };
    return new FormGroup<CompanyFormGroupContent>({
      id: new FormControl(
        { value: companyRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(companyRawValue.name, {
        validators: [Validators.required, Validators.maxLength(50)],
      }),
      phone: new FormControl(companyRawValue.phone, {
        validators: [Validators.required, Validators.minLength(10), Validators.maxLength(15)],
      }),
      siret: new FormControl(companyRawValue.siret, {
        validators: [Validators.required, Validators.minLength(14), Validators.maxLength(14)],
      }),
      address: new FormControl(companyRawValue.address, {
        validators: [Validators.required, Validators.maxLength(50)],
      }),
      email: new FormControl(companyRawValue.email, {
        validators: [Validators.maxLength(100)],
      }),
      contactPerson: new FormControl(companyRawValue.contactPerson, {
        validators: [Validators.maxLength(100)],
      }),
      contactPersonPhone: new FormControl(companyRawValue.contactPersonPhone, {
        validators: [Validators.maxLength(15)],
      }),
      contactPersonEmail: new FormControl(companyRawValue.contactPersonEmail, {
        validators: [Validators.maxLength(100)],
      }),
      size: new FormControl(companyRawValue.size, {
        validators: [Validators.maxLength(50)],
      }),
      notes: new FormControl(companyRawValue.notes, {
        validators: [Validators.maxLength(500)],
      }),
    });
  }

  getCompany(form: CompanyFormGroup): ICompany | NewCompany {
    return form.getRawValue() as ICompany | NewCompany;
  }

  resetForm(form: CompanyFormGroup, company: CompanyFormGroupInput): void {
    const companyRawValue = { ...this.getFormDefaults(), ...company };
    form.reset(
      {
        ...companyRawValue,
        id: { value: companyRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): CompanyFormDefaults {
    return {
      id: null,
    };
  }
}
