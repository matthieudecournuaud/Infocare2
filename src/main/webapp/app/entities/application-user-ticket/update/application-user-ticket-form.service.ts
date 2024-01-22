import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IApplicationUserTicket, NewApplicationUserTicket } from '../application-user-ticket.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IApplicationUserTicket for edit and NewApplicationUserTicketFormGroupInput for create.
 */
type ApplicationUserTicketFormGroupInput = IApplicationUserTicket | PartialWithRequiredKeyOf<NewApplicationUserTicket>;

type ApplicationUserTicketFormDefaults = Pick<NewApplicationUserTicket, 'id'>;

type ApplicationUserTicketFormGroupContent = {
  id: FormControl<IApplicationUserTicket['id'] | NewApplicationUserTicket['id']>;
};

export type ApplicationUserTicketFormGroup = FormGroup<ApplicationUserTicketFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ApplicationUserTicketFormService {
  createApplicationUserTicketFormGroup(
    applicationUserTicket: ApplicationUserTicketFormGroupInput = { id: null },
  ): ApplicationUserTicketFormGroup {
    const applicationUserTicketRawValue = {
      ...this.getFormDefaults(),
      ...applicationUserTicket,
    };
    return new FormGroup<ApplicationUserTicketFormGroupContent>({
      id: new FormControl(
        { value: applicationUserTicketRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
    });
  }

  getApplicationUserTicket(form: ApplicationUserTicketFormGroup): IApplicationUserTicket | NewApplicationUserTicket {
    return form.getRawValue() as IApplicationUserTicket | NewApplicationUserTicket;
  }

  resetForm(form: ApplicationUserTicketFormGroup, applicationUserTicket: ApplicationUserTicketFormGroupInput): void {
    const applicationUserTicketRawValue = { ...this.getFormDefaults(), ...applicationUserTicket };
    form.reset(
      {
        ...applicationUserTicketRawValue,
        id: { value: applicationUserTicketRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): ApplicationUserTicketFormDefaults {
    return {
      id: null,
    };
  }
}
