import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ApplicationUserTicketService } from '../service/application-user-ticket.service';
import { IApplicationUserTicket } from '../application-user-ticket.model';
import { ApplicationUserTicketFormService } from './application-user-ticket-form.service';

import { ApplicationUserTicketUpdateComponent } from './application-user-ticket-update.component';

describe('ApplicationUserTicket Management Update Component', () => {
  let comp: ApplicationUserTicketUpdateComponent;
  let fixture: ComponentFixture<ApplicationUserTicketUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let applicationUserTicketFormService: ApplicationUserTicketFormService;
  let applicationUserTicketService: ApplicationUserTicketService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), ApplicationUserTicketUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(ApplicationUserTicketUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ApplicationUserTicketUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    applicationUserTicketFormService = TestBed.inject(ApplicationUserTicketFormService);
    applicationUserTicketService = TestBed.inject(ApplicationUserTicketService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const applicationUserTicket: IApplicationUserTicket = { id: 456 };

      activatedRoute.data = of({ applicationUserTicket });
      comp.ngOnInit();

      expect(comp.applicationUserTicket).toEqual(applicationUserTicket);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IApplicationUserTicket>>();
      const applicationUserTicket = { id: 123 };
      jest.spyOn(applicationUserTicketFormService, 'getApplicationUserTicket').mockReturnValue(applicationUserTicket);
      jest.spyOn(applicationUserTicketService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ applicationUserTicket });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: applicationUserTicket }));
      saveSubject.complete();

      // THEN
      expect(applicationUserTicketFormService.getApplicationUserTicket).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(applicationUserTicketService.update).toHaveBeenCalledWith(expect.objectContaining(applicationUserTicket));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IApplicationUserTicket>>();
      const applicationUserTicket = { id: 123 };
      jest.spyOn(applicationUserTicketFormService, 'getApplicationUserTicket').mockReturnValue({ id: null });
      jest.spyOn(applicationUserTicketService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ applicationUserTicket: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: applicationUserTicket }));
      saveSubject.complete();

      // THEN
      expect(applicationUserTicketFormService.getApplicationUserTicket).toHaveBeenCalled();
      expect(applicationUserTicketService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IApplicationUserTicket>>();
      const applicationUserTicket = { id: 123 };
      jest.spyOn(applicationUserTicketService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ applicationUserTicket });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(applicationUserTicketService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
