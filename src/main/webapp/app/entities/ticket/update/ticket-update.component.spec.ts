import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { IMaterial } from 'app/entities/material/material.model';
import { MaterialService } from 'app/entities/material/service/material.service';
import { ICategory } from 'app/entities/category/category.model';
import { CategoryService } from 'app/entities/category/service/category.service';
import { IStatus } from 'app/entities/status/status.model';
import { StatusService } from 'app/entities/status/service/status.service';
import { IPriority } from 'app/entities/priority/priority.model';
import { PriorityService } from 'app/entities/priority/service/priority.service';
import { ITicket } from '../ticket.model';
import { TicketService } from '../service/ticket.service';
import { TicketFormService } from './ticket-form.service';

import { TicketUpdateComponent } from './ticket-update.component';

describe('Ticket Management Update Component', () => {
  let comp: TicketUpdateComponent;
  let fixture: ComponentFixture<TicketUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let ticketFormService: TicketFormService;
  let ticketService: TicketService;
  let materialService: MaterialService;
  let categoryService: CategoryService;
  let statusService: StatusService;
  let priorityService: PriorityService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), TicketUpdateComponent],
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
      .overrideTemplate(TicketUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(TicketUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    ticketFormService = TestBed.inject(TicketFormService);
    ticketService = TestBed.inject(TicketService);
    materialService = TestBed.inject(MaterialService);
    categoryService = TestBed.inject(CategoryService);
    statusService = TestBed.inject(StatusService);
    priorityService = TestBed.inject(PriorityService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call material query and add missing value', () => {
      const ticket: ITicket = { id: 456 };
      const material: IMaterial = { id: 19577 };
      ticket.material = material;

      const materialCollection: IMaterial[] = [{ id: 12916 }];
      jest.spyOn(materialService, 'query').mockReturnValue(of(new HttpResponse({ body: materialCollection })));
      const expectedCollection: IMaterial[] = [material, ...materialCollection];
      jest.spyOn(materialService, 'addMaterialToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ ticket });
      comp.ngOnInit();

      expect(materialService.query).toHaveBeenCalled();
      expect(materialService.addMaterialToCollectionIfMissing).toHaveBeenCalledWith(materialCollection, material);
      expect(comp.materialsCollection).toEqual(expectedCollection);
    });

    it('Should call Category query and add missing value', () => {
      const ticket: ITicket = { id: 456 };
      const category: ICategory = { id: 3789 };
      ticket.category = category;

      const categoryCollection: ICategory[] = [{ id: 14535 }];
      jest.spyOn(categoryService, 'query').mockReturnValue(of(new HttpResponse({ body: categoryCollection })));
      const additionalCategories = [category];
      const expectedCollection: ICategory[] = [...additionalCategories, ...categoryCollection];
      jest.spyOn(categoryService, 'addCategoryToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ ticket });
      comp.ngOnInit();

      expect(categoryService.query).toHaveBeenCalled();
      expect(categoryService.addCategoryToCollectionIfMissing).toHaveBeenCalledWith(
        categoryCollection,
        ...additionalCategories.map(expect.objectContaining),
      );
      expect(comp.categoriesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Status query and add missing value', () => {
      const ticket: ITicket = { id: 456 };
      const status: IStatus = { id: 30336 };
      ticket.status = status;

      const statusCollection: IStatus[] = [{ id: 10817 }];
      jest.spyOn(statusService, 'query').mockReturnValue(of(new HttpResponse({ body: statusCollection })));
      const additionalStatuses = [status];
      const expectedCollection: IStatus[] = [...additionalStatuses, ...statusCollection];
      jest.spyOn(statusService, 'addStatusToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ ticket });
      comp.ngOnInit();

      expect(statusService.query).toHaveBeenCalled();
      expect(statusService.addStatusToCollectionIfMissing).toHaveBeenCalledWith(
        statusCollection,
        ...additionalStatuses.map(expect.objectContaining),
      );
      expect(comp.statusesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Priority query and add missing value', () => {
      const ticket: ITicket = { id: 456 };
      const priority: IPriority = { id: 6960 };
      ticket.priority = priority;

      const priorityCollection: IPriority[] = [{ id: 22933 }];
      jest.spyOn(priorityService, 'query').mockReturnValue(of(new HttpResponse({ body: priorityCollection })));
      const additionalPriorities = [priority];
      const expectedCollection: IPriority[] = [...additionalPriorities, ...priorityCollection];
      jest.spyOn(priorityService, 'addPriorityToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ ticket });
      comp.ngOnInit();

      expect(priorityService.query).toHaveBeenCalled();
      expect(priorityService.addPriorityToCollectionIfMissing).toHaveBeenCalledWith(
        priorityCollection,
        ...additionalPriorities.map(expect.objectContaining),
      );
      expect(comp.prioritiesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const ticket: ITicket = { id: 456 };
      const material: IMaterial = { id: 24346 };
      ticket.material = material;
      const category: ICategory = { id: 9009 };
      ticket.category = category;
      const status: IStatus = { id: 16842 };
      ticket.status = status;
      const priority: IPriority = { id: 4620 };
      ticket.priority = priority;

      activatedRoute.data = of({ ticket });
      comp.ngOnInit();

      expect(comp.materialsCollection).toContain(material);
      expect(comp.categoriesSharedCollection).toContain(category);
      expect(comp.statusesSharedCollection).toContain(status);
      expect(comp.prioritiesSharedCollection).toContain(priority);
      expect(comp.ticket).toEqual(ticket);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITicket>>();
      const ticket = { id: 123 };
      jest.spyOn(ticketFormService, 'getTicket').mockReturnValue(ticket);
      jest.spyOn(ticketService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ ticket });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: ticket }));
      saveSubject.complete();

      // THEN
      expect(ticketFormService.getTicket).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(ticketService.update).toHaveBeenCalledWith(expect.objectContaining(ticket));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITicket>>();
      const ticket = { id: 123 };
      jest.spyOn(ticketFormService, 'getTicket').mockReturnValue({ id: null });
      jest.spyOn(ticketService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ ticket: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: ticket }));
      saveSubject.complete();

      // THEN
      expect(ticketFormService.getTicket).toHaveBeenCalled();
      expect(ticketService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITicket>>();
      const ticket = { id: 123 };
      jest.spyOn(ticketService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ ticket });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(ticketService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareMaterial', () => {
      it('Should forward to materialService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(materialService, 'compareMaterial');
        comp.compareMaterial(entity, entity2);
        expect(materialService.compareMaterial).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareCategory', () => {
      it('Should forward to categoryService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(categoryService, 'compareCategory');
        comp.compareCategory(entity, entity2);
        expect(categoryService.compareCategory).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareStatus', () => {
      it('Should forward to statusService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(statusService, 'compareStatus');
        comp.compareStatus(entity, entity2);
        expect(statusService.compareStatus).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('comparePriority', () => {
      it('Should forward to priorityService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(priorityService, 'comparePriority');
        comp.comparePriority(entity, entity2);
        expect(priorityService.comparePriority).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
