import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { TicketDetailComponent } from './ticket-detail.component';

describe('Ticket Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TicketDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: TicketDetailComponent,
              resolve: { ticket: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(TicketDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load ticket on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', TicketDetailComponent);

      // THEN
      expect(instance.ticket).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
