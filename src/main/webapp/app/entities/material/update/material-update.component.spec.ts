import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ICompany } from 'app/entities/company/company.model';
import { CompanyService } from 'app/entities/company/service/company.service';
import { MaterialService } from '../service/material.service';
import { IMaterial } from '../material.model';
import { MaterialFormService } from './material-form.service';

import { MaterialUpdateComponent } from './material-update.component';

describe('Material Management Update Component', () => {
  let comp: MaterialUpdateComponent;
  let fixture: ComponentFixture<MaterialUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let materialFormService: MaterialFormService;
  let materialService: MaterialService;
  let companyService: CompanyService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), MaterialUpdateComponent],
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
      .overrideTemplate(MaterialUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(MaterialUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    materialFormService = TestBed.inject(MaterialFormService);
    materialService = TestBed.inject(MaterialService);
    companyService = TestBed.inject(CompanyService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call company query and add missing value', () => {
      const material: IMaterial = { id: 456 };
      const company: ICompany = { id: 27154 };
      material.company = company;

      const companyCollection: ICompany[] = [{ id: 17218 }];
      jest.spyOn(companyService, 'query').mockReturnValue(of(new HttpResponse({ body: companyCollection })));
      const expectedCollection: ICompany[] = [company, ...companyCollection];
      jest.spyOn(companyService, 'addCompanyToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ material });
      comp.ngOnInit();

      expect(companyService.query).toHaveBeenCalled();
      expect(companyService.addCompanyToCollectionIfMissing).toHaveBeenCalledWith(companyCollection, company);
      expect(comp.companiesCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const material: IMaterial = { id: 456 };
      const company: ICompany = { id: 7148 };
      material.company = company;

      activatedRoute.data = of({ material });
      comp.ngOnInit();

      expect(comp.companiesCollection).toContain(company);
      expect(comp.material).toEqual(material);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMaterial>>();
      const material = { id: 123 };
      jest.spyOn(materialFormService, 'getMaterial').mockReturnValue(material);
      jest.spyOn(materialService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ material });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: material }));
      saveSubject.complete();

      // THEN
      expect(materialFormService.getMaterial).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(materialService.update).toHaveBeenCalledWith(expect.objectContaining(material));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMaterial>>();
      const material = { id: 123 };
      jest.spyOn(materialFormService, 'getMaterial').mockReturnValue({ id: null });
      jest.spyOn(materialService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ material: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: material }));
      saveSubject.complete();

      // THEN
      expect(materialFormService.getMaterial).toHaveBeenCalled();
      expect(materialService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMaterial>>();
      const material = { id: 123 };
      jest.spyOn(materialService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ material });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(materialService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareCompany', () => {
      it('Should forward to companyService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(companyService, 'compareCompany');
        comp.compareCompany(entity, entity2);
        expect(companyService.compareCompany).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
