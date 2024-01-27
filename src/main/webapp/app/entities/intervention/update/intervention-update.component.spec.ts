import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { IProcedure } from 'app/entities/procedure/procedure.model';
import { ProcedureService } from 'app/entities/procedure/service/procedure.service';
import { ITicket } from 'app/entities/ticket/ticket.model';
import { TicketService } from 'app/entities/ticket/service/ticket.service';
import { IIntervention } from '../intervention.model';
import { InterventionService } from '../service/intervention.service';
import { InterventionFormService } from './intervention-form.service';

import { InterventionUpdateComponent } from './intervention-update.component';

describe('Intervention Management Update Component', () => {
  let comp: InterventionUpdateComponent;
  let fixture: ComponentFixture<InterventionUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let interventionFormService: InterventionFormService;
  let interventionService: InterventionService;
  let procedureService: ProcedureService;
  let ticketService: TicketService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), InterventionUpdateComponent],
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
      .overrideTemplate(InterventionUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(InterventionUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    interventionFormService = TestBed.inject(InterventionFormService);
    interventionService = TestBed.inject(InterventionService);
    procedureService = TestBed.inject(ProcedureService);
    ticketService = TestBed.inject(TicketService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call procedure query and add missing value', () => {
      const intervention: IIntervention = { id: 456 };
      const procedure: IProcedure = { id: 24347 };
      intervention.procedure = procedure;

      const procedureCollection: IProcedure[] = [{ id: 725 }];
      jest.spyOn(procedureService, 'query').mockReturnValue(of(new HttpResponse({ body: procedureCollection })));
      const expectedCollection: IProcedure[] = [procedure, ...procedureCollection];
      jest.spyOn(procedureService, 'addProcedureToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ intervention });
      comp.ngOnInit();

      expect(procedureService.query).toHaveBeenCalled();
      expect(procedureService.addProcedureToCollectionIfMissing).toHaveBeenCalledWith(procedureCollection, procedure);
      expect(comp.proceduresCollection).toEqual(expectedCollection);
    });

    it('Should call Ticket query and add missing value', () => {
      const intervention: IIntervention = { id: 456 };
      const ticket: ITicket = { id: 601 };
      intervention.ticket = ticket;

      const ticketCollection: ITicket[] = [{ id: 18390 }];
      jest.spyOn(ticketService, 'query').mockReturnValue(of(new HttpResponse({ body: ticketCollection })));
      const additionalTickets = [ticket];
      const expectedCollection: ITicket[] = [...additionalTickets, ...ticketCollection];
      jest.spyOn(ticketService, 'addTicketToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ intervention });
      comp.ngOnInit();

      expect(ticketService.query).toHaveBeenCalled();
      expect(ticketService.addTicketToCollectionIfMissing).toHaveBeenCalledWith(
        ticketCollection,
        ...additionalTickets.map(expect.objectContaining),
      );
      expect(comp.ticketsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const intervention: IIntervention = { id: 456 };
      const procedure: IProcedure = { id: 32189 };
      intervention.procedure = procedure;
      const ticket: ITicket = { id: 30241 };
      intervention.ticket = ticket;

      activatedRoute.data = of({ intervention });
      comp.ngOnInit();

      expect(comp.proceduresCollection).toContain(procedure);
      expect(comp.ticketsSharedCollection).toContain(ticket);
      expect(comp.intervention).toEqual(intervention);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IIntervention>>();
      const intervention = { id: 123 };
      jest.spyOn(interventionFormService, 'getIntervention').mockReturnValue(intervention);
      jest.spyOn(interventionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ intervention });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: intervention }));
      saveSubject.complete();

      // THEN
      expect(interventionFormService.getIntervention).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(interventionService.update).toHaveBeenCalledWith(expect.objectContaining(intervention));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IIntervention>>();
      const intervention = { id: 123 };
      jest.spyOn(interventionFormService, 'getIntervention').mockReturnValue({ id: null });
      jest.spyOn(interventionService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ intervention: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: intervention }));
      saveSubject.complete();

      // THEN
      expect(interventionFormService.getIntervention).toHaveBeenCalled();
      expect(interventionService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IIntervention>>();
      const intervention = { id: 123 };
      jest.spyOn(interventionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ intervention });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(interventionService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareProcedure', () => {
      it('Should forward to procedureService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(procedureService, 'compareProcedure');
        comp.compareProcedure(entity, entity2);
        expect(procedureService.compareProcedure).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareTicket', () => {
      it('Should forward to ticketService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(ticketService, 'compareTicket');
        comp.compareTicket(entity, entity2);
        expect(ticketService.compareTicket).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
