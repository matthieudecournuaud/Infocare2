import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { ProcedureDetailComponent } from './procedure-detail.component';

describe('Procedure Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ProcedureDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: ProcedureDetailComponent,
              resolve: { procedure: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(ProcedureDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load procedure on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', ProcedureDetailComponent);

      // THEN
      expect(instance.procedure).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
