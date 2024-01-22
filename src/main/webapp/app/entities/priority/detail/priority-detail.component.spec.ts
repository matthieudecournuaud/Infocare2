import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { PriorityDetailComponent } from './priority-detail.component';

describe('Priority Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PriorityDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: PriorityDetailComponent,
              resolve: { priority: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(PriorityDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load priority on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', PriorityDetailComponent);

      // THEN
      expect(instance.priority).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
