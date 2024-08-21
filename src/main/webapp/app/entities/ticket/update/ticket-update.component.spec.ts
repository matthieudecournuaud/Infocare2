import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideHttpClient, HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject, from } from 'rxjs';

import { IApplicationUser } from 'app/entities/application-user/application-user.model';
import { ApplicationUserService } from 'app/entities/application-user/service/application-user.service';
import { ICategory } from 'app/entities/category/category.model';
import { CategoryService } from 'app/entities/category/service/category.service';
import { IStatus } from 'app/entities/status/status.model';
import { StatusService } from 'app/entities/status/service/status.service';
import { IPriority } from 'app/entities/priority/priority.model';
import { PriorityService } from 'app/entities/priority/service/priority.service';
import { IMaterial } from 'app/entities/material/material.model';
import { MaterialService } from 'app/entities/material/service/material.service';
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
  let applicationUserService: ApplicationUserService;
  let categoryService: CategoryService;
  let statusService: StatusService;
  let priorityService: PriorityService;
  let materialService: MaterialService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [TicketUpdateComponent],
      providers: [
        provideHttpClient(),
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
    applicationUserService = TestBed.inject(ApplicationUserService);
    categoryService = TestBed.inject(CategoryService);
    statusService = TestBed.inject(StatusService);
    priorityService = TestBed.inject(PriorityService);
    materialService = TestBed.inject(MaterialService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call applicationUser query and add missing value', () => {
      const ticket: ITicket = { id: 456 };
      const applicationUser: IApplicationUser = { id: 2474 };
      ticket.applicationUser = applicationUser;

      const applicationUserCollection: IApplicationUser[] = [{ id: 28586 }];
      jest.spyOn(applicationUserService, 'query').mockReturnValue(of(new HttpResponse({ body: applicationUserCollection })));
      const expectedCollection: IApplicationUser[] = [applicationUser, ...applicationUserCollection];
      jest.spyOn(applicationUserService, 'addApplicationUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ ticket });
      comp.ngOnInit();

      expect(applicationUserService.query).toHaveBeenCalled();
      expect(applicationUserService.addApplicationUserToCollectionIfMissing).toHaveBeenCalledWith(
        applicationUserCollection,
        applicationUser,
      );
      expect(comp.applicationUsersCollection).toEqual(expectedCollection);
    });

    it('Should call category query and add missing value', () => {
      const ticket: ITicket = { id: 456 };
      const category: ICategory = { id: 3789 };
      ticket.category = category;

      const categoryCollection: ICategory[] = [{ id: 14535 }];
      jest.spyOn(categoryService, 'query').mockReturnValue(of(new HttpResponse({ body: categoryCollection })));
      const expectedCollection: ICategory[] = [category, ...categoryCollection];
      jest.spyOn(categoryService, 'addCategoryToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ ticket });
      comp.ngOnInit();

      expect(categoryService.query).toHaveBeenCalled();
      expect(categoryService.addCategoryToCollectionIfMissing).toHaveBeenCalledWith(categoryCollection, category);
      expect(comp.categoriesCollection).toEqual(expectedCollection);
    });

    it('Should call status query and add missing value', () => {
      const ticket: ITicket = { id: 456 };
      const status: IStatus = { id: 30336 };
      ticket.status = status;

      const statusCollection: IStatus[] = [{ id: 10817 }];
      jest.spyOn(statusService, 'query').mockReturnValue(of(new HttpResponse({ body: statusCollection })));
      const expectedCollection: IStatus[] = [status, ...statusCollection];
      jest.spyOn(statusService, 'addStatusToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ ticket });
      comp.ngOnInit();

      expect(statusService.query).toHaveBeenCalled();
      expect(statusService.addStatusToCollectionIfMissing).toHaveBeenCalledWith(statusCollection, status);
      expect(comp.statusesCollection).toEqual(expectedCollection);
    });

    it('Should call priority query and add missing value', () => {
      const ticket: ITicket = { id: 456 };
      const priority: IPriority = { id: 6960 };
      ticket.priority = priority;

      const priorityCollection: IPriority[] = [{ id: 22933 }];
      jest.spyOn(priorityService, 'query').mockReturnValue(of(new HttpResponse({ body: priorityCollection })));
      const expectedCollection: IPriority[] = [priority, ...priorityCollection];
      jest.spyOn(priorityService, 'addPriorityToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ ticket });
      comp.ngOnInit();

      expect(priorityService.query).toHaveBeenCalled();
      expect(priorityService.addPriorityToCollectionIfMissing).toHaveBeenCalledWith(priorityCollection, priority);
      expect(comp.prioritiesCollection).toEqual(expectedCollection);
    });

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

    it('Should update editForm', () => {
      const ticket: ITicket = { id: 456 };
      const applicationUser: IApplicationUser = { id: 8991 };
      ticket.applicationUser = applicationUser;
      const category: ICategory = { id: 9009 };
      ticket.category = category;
      const status: IStatus = { id: 16842 };
      ticket.status = status;
      const priority: IPriority = { id: 4620 };
      ticket.priority = priority;
      const material: IMaterial = { id: 24346 };
      ticket.material = material;

      activatedRoute.data = of({ ticket });
      comp.ngOnInit();

      expect(comp.applicationUsersCollection).toContain(applicationUser);
      expect(comp.categoriesCollection).toContain(category);
      expect(comp.statusesCollection).toContain(status);
      expect(comp.prioritiesCollection).toContain(priority);
      expect(comp.materialsCollection).toContain(material);
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
    describe('compareApplicationUser', () => {
      it('Should forward to applicationUserService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(applicationUserService, 'compareApplicationUser');
        comp.compareApplicationUser(entity, entity2);
        expect(applicationUserService.compareApplicationUser).toHaveBeenCalledWith(entity, entity2);
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

    describe('compareMaterial', () => {
      it('Should forward to materialService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(materialService, 'compareMaterial');
        comp.compareMaterial(entity, entity2);
        expect(materialService.compareMaterial).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
