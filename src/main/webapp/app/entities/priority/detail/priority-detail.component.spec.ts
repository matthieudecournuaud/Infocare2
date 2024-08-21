import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { PriorityDetailComponent } from './priority-detail.component';

describe('Priority Management Detail Component', () => {
  let comp: PriorityDetailComponent;
  let fixture: ComponentFixture<PriorityDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PriorityDetailComponent],
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

  beforeEach(() => {
    fixture = TestBed.createComponent(PriorityDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load priority on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', PriorityDetailComponent);

      // THEN
      expect(instance.priority()).toEqual(expect.objectContaining({ id: 123 }));
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
