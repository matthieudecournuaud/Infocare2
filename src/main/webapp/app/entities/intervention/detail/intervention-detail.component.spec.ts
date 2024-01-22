import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { InterventionDetailComponent } from './intervention-detail.component';

describe('Intervention Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [InterventionDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: InterventionDetailComponent,
              resolve: { intervention: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(InterventionDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load intervention on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', InterventionDetailComponent);

      // THEN
      expect(instance.intervention).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
