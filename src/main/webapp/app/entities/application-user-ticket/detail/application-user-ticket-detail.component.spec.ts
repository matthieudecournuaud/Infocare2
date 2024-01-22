import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { ApplicationUserTicketDetailComponent } from './application-user-ticket-detail.component';

describe('ApplicationUserTicket Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ApplicationUserTicketDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: ApplicationUserTicketDetailComponent,
              resolve: { applicationUserTicket: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(ApplicationUserTicketDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load applicationUserTicket on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', ApplicationUserTicketDetailComponent);

      // THEN
      expect(instance.applicationUserTicket).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
