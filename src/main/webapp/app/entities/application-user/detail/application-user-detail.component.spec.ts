import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { ApplicationUserDetailComponent } from './application-user-detail.component';

describe('ApplicationUser Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ApplicationUserDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: ApplicationUserDetailComponent,
              resolve: { applicationUser: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(ApplicationUserDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load applicationUser on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', ApplicationUserDetailComponent);

      // THEN
      expect(instance.applicationUser).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
