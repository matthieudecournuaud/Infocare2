import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { ProcedureService } from '../service/procedure.service';

import { ProcedureComponent } from './procedure.component';

describe('Procedure Management Component', () => {
  let comp: ProcedureComponent;
  let fixture: ComponentFixture<ProcedureComponent>;
  let service: ProcedureService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        RouterTestingModule.withRoutes([{ path: 'procedure', component: ProcedureComponent }]),
        HttpClientTestingModule,
        ProcedureComponent,
      ],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: {
            data: of({
              defaultSort: 'id,asc',
            }),
            queryParamMap: of(
              jest.requireActual('@angular/router').convertToParamMap({
                page: '1',
                size: '1',
                sort: 'id,desc',
              }),
            ),
            snapshot: { queryParams: {} },
          },
        },
      ],
    })
      .overrideTemplate(ProcedureComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ProcedureComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(ProcedureService);

    const headers = new HttpHeaders();
    jest.spyOn(service, 'query').mockReturnValue(
      of(
        new HttpResponse({
          body: [{ id: 123 }],
          headers,
        }),
      ),
    );
  });

  it('Should call load all on init', () => {
    // WHEN
    comp.ngOnInit();

    // THEN
    expect(service.query).toHaveBeenCalled();
    expect(comp.procedures?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });

  describe('trackId', () => {
    it('Should forward to procedureService', () => {
      const entity = { id: 123 };
      jest.spyOn(service, 'getProcedureIdentifier');
      const id = comp.trackId(0, entity);
      expect(service.getProcedureIdentifier).toHaveBeenCalledWith(entity);
      expect(id).toBe(entity.id);
    });
  });
});
