import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideHttpClient, HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject, from } from 'rxjs';

import { ProcedureService } from '../service/procedure.service';
import { IProcedure } from '../procedure.model';
import { ProcedureFormService } from './procedure-form.service';

import { ProcedureUpdateComponent } from './procedure-update.component';

describe('Procedure Management Update Component', () => {
  let comp: ProcedureUpdateComponent;
  let fixture: ComponentFixture<ProcedureUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let procedureFormService: ProcedureFormService;
  let procedureService: ProcedureService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [ProcedureUpdateComponent],
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
      .overrideTemplate(ProcedureUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ProcedureUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    procedureFormService = TestBed.inject(ProcedureFormService);
    procedureService = TestBed.inject(ProcedureService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const procedure: IProcedure = { id: 456 };

      activatedRoute.data = of({ procedure });
      comp.ngOnInit();

      expect(comp.procedure).toEqual(procedure);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IProcedure>>();
      const procedure = { id: 123 };
      jest.spyOn(procedureFormService, 'getProcedure').mockReturnValue(procedure);
      jest.spyOn(procedureService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ procedure });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: procedure }));
      saveSubject.complete();

      // THEN
      expect(procedureFormService.getProcedure).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(procedureService.update).toHaveBeenCalledWith(expect.objectContaining(procedure));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IProcedure>>();
      const procedure = { id: 123 };
      jest.spyOn(procedureFormService, 'getProcedure').mockReturnValue({ id: null });
      jest.spyOn(procedureService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ procedure: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: procedure }));
      saveSubject.complete();

      // THEN
      expect(procedureFormService.getProcedure).toHaveBeenCalled();
      expect(procedureService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IProcedure>>();
      const procedure = { id: 123 };
      jest.spyOn(procedureService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ procedure });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(procedureService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
