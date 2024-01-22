import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../application-user-ticket.test-samples';

import { ApplicationUserTicketFormService } from './application-user-ticket-form.service';

describe('ApplicationUserTicket Form Service', () => {
  let service: ApplicationUserTicketFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ApplicationUserTicketFormService);
  });

  describe('Service methods', () => {
    describe('createApplicationUserTicketFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createApplicationUserTicketFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
          }),
        );
      });

      it('passing IApplicationUserTicket should create a new form with FormGroup', () => {
        const formGroup = service.createApplicationUserTicketFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
          }),
        );
      });
    });

    describe('getApplicationUserTicket', () => {
      it('should return NewApplicationUserTicket for default ApplicationUserTicket initial value', () => {
        const formGroup = service.createApplicationUserTicketFormGroup(sampleWithNewData);

        const applicationUserTicket = service.getApplicationUserTicket(formGroup) as any;

        expect(applicationUserTicket).toMatchObject(sampleWithNewData);
      });

      it('should return NewApplicationUserTicket for empty ApplicationUserTicket initial value', () => {
        const formGroup = service.createApplicationUserTicketFormGroup();

        const applicationUserTicket = service.getApplicationUserTicket(formGroup) as any;

        expect(applicationUserTicket).toMatchObject({});
      });

      it('should return IApplicationUserTicket', () => {
        const formGroup = service.createApplicationUserTicketFormGroup(sampleWithRequiredData);

        const applicationUserTicket = service.getApplicationUserTicket(formGroup) as any;

        expect(applicationUserTicket).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IApplicationUserTicket should not enable id FormControl', () => {
        const formGroup = service.createApplicationUserTicketFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewApplicationUserTicket should disable id FormControl', () => {
        const formGroup = service.createApplicationUserTicketFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
