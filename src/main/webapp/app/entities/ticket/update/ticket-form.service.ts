import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { ITicket, NewTicket } from '../ticket.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ITicket for edit and NewTicketFormGroupInput for create.
 */
type TicketFormGroupInput = ITicket | PartialWithRequiredKeyOf<NewTicket>;

type TicketFormDefaults = Pick<NewTicket, 'id'>;

type TicketFormGroupContent = {
  id: FormControl<ITicket['id'] | NewTicket['id']>;
  title: FormControl<ITicket['title']>;
  description: FormControl<ITicket['description']>;
  createdAt: FormControl<ITicket['createdAt']>;
  resolutionDate: FormControl<ITicket['resolutionDate']>;
  closedAt: FormControl<ITicket['closedAt']>;
  limitDate: FormControl<ITicket['limitDate']>;
  impact: FormControl<ITicket['impact']>;
  resolution: FormControl<ITicket['resolution']>;
  attachments: FormControl<ITicket['attachments']>;
  material: FormControl<ITicket['material']>;
  category: FormControl<ITicket['category']>;
  status: FormControl<ITicket['status']>;
  priority: FormControl<ITicket['priority']>;
};

export type TicketFormGroup = FormGroup<TicketFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class TicketFormService {
  createTicketFormGroup(ticket: TicketFormGroupInput = { id: null }): TicketFormGroup {
    const ticketRawValue = {
      ...this.getFormDefaults(),
      ...ticket,
    };
    return new FormGroup<TicketFormGroupContent>({
      id: new FormControl(
        { value: ticketRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      title: new FormControl(ticketRawValue.title, {
        validators: [Validators.required, Validators.maxLength(200)],
      }),
      description: new FormControl(ticketRawValue.description, {
        validators: [Validators.required, Validators.maxLength(5000)],
      }),
      createdAt: new FormControl(ticketRawValue.createdAt, {
        validators: [Validators.required],
      }),
      resolutionDate: new FormControl(ticketRawValue.resolutionDate),
      closedAt: new FormControl(ticketRawValue.closedAt),
      limitDate: new FormControl(ticketRawValue.limitDate),
      impact: new FormControl(ticketRawValue.impact, {
        validators: [Validators.maxLength(50)],
      }),
      resolution: new FormControl(ticketRawValue.resolution, {
        validators: [Validators.maxLength(5000)],
      }),
      attachments: new FormControl(ticketRawValue.attachments, {
        validators: [Validators.maxLength(5000)],
      }),
      material: new FormControl(ticketRawValue.material),
      category: new FormControl(ticketRawValue.category),
      status: new FormControl(ticketRawValue.status),
      priority: new FormControl(ticketRawValue.priority),
    });
  }

  getTicket(form: TicketFormGroup): ITicket | NewTicket {
    return form.getRawValue() as ITicket | NewTicket;
  }

  resetForm(form: TicketFormGroup, ticket: TicketFormGroupInput): void {
    const ticketRawValue = { ...this.getFormDefaults(), ...ticket };
    form.reset(
      {
        ...ticketRawValue,
        id: { value: ticketRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): TicketFormDefaults {
    return {
      id: null,
    };
  }
}
