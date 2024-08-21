import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { ProcedureDetailComponent } from './procedure-detail.component';

describe('Procedure Management Detail Component', () => {
  let comp: ProcedureDetailComponent;
  let fixture: ComponentFixture<ProcedureDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ProcedureDetailComponent],
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

  beforeEach(() => {
    fixture = TestBed.createComponent(ProcedureDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load procedure on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', ProcedureDetailComponent);

      // THEN
      expect(instance.procedure()).toEqual(expect.objectContaining({ id: 123 }));
    });
  });

  describe('PreviousState', () => {
    it('Should navigate to previous state', () => {
      jest.spyOn(window.history, 'back');
      comp.previousState();
      expect(window.history.back).toHaveBeenCalled();
    });
  });
});
