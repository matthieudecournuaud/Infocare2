import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IApplicationUser, NewApplicationUser } from '../application-user.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IApplicationUser for edit and NewApplicationUserFormGroupInput for create.
 */
type ApplicationUserFormGroupInput = IApplicationUser | PartialWithRequiredKeyOf<NewApplicationUser>;

type ApplicationUserFormDefaults = Pick<NewApplicationUser, 'id' | 'tickets'>;

type ApplicationUserFormGroupContent = {
  id: FormControl<IApplicationUser['id'] | NewApplicationUser['id']>;
  phoneNumber: FormControl<IApplicationUser['phoneNumber']>;
  location: FormControl<IApplicationUser['location']>;
  avatar: FormControl<IApplicationUser['avatar']>;
  notes: FormControl<IApplicationUser['notes']>;
  user: FormControl<IApplicationUser['user']>;
  tickets: FormControl<IApplicationUser['tickets']>;
};

export type ApplicationUserFormGroup = FormGroup<ApplicationUserFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ApplicationUserFormService {
  createApplicationUserFormGroup(applicationUser: ApplicationUserFormGroupInput = { id: null }): ApplicationUserFormGroup {
    const applicationUserRawValue = {
      ...this.getFormDefaults(),
      ...applicationUser,
    };
    return new FormGroup<ApplicationUserFormGroupContent>({
      id: new FormControl(
        { value: applicationUserRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      phoneNumber: new FormControl(applicationUserRawValue.phoneNumber, {
        validators: [Validators.maxLength(20)],
      }),
      location: new FormControl(applicationUserRawValue.location, {
        validators: [Validators.maxLength(100)],
      }),
      avatar: new FormControl(applicationUserRawValue.avatar, {
        validators: [Validators.maxLength(200)],
      }),
      notes: new FormControl(applicationUserRawValue.notes, {
        validators: [Validators.maxLength(500)],
      }),
      user: new FormControl(applicationUserRawValue.user),
      tickets: new FormControl(applicationUserRawValue.tickets ?? []),
    });
  }

  getApplicationUser(form: ApplicationUserFormGroup): IApplicationUser | NewApplicationUser {
    return form.getRawValue() as IApplicationUser | NewApplicationUser;
  }

  resetForm(form: ApplicationUserFormGroup, applicationUser: ApplicationUserFormGroupInput): void {
    const applicationUserRawValue = { ...this.getFormDefaults(), ...applicationUser };
    form.reset(
      {
        ...applicationUserRawValue,
        id: { value: applicationUserRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): ApplicationUserFormDefaults {
    return {
      id: null,
      tickets: [],
    };
  }
}
