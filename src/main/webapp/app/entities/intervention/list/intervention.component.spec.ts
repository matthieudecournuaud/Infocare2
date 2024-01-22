import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { InterventionService } from '../service/intervention.service';

import { InterventionComponent } from './intervention.component';

describe('Intervention Management Component', () => {
  let comp: InterventionComponent;
  let fixture: ComponentFixture<InterventionComponent>;
  let service: InterventionService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        RouterTestingModule.withRoutes([{ path: 'intervention', component: InterventionComponent }]),
        HttpClientTestingModule,
        InterventionComponent,
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
      .overrideTemplate(InterventionComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(InterventionComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(InterventionService);

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
    expect(comp.interventions?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });

  describe('trackId', () => {
    it('Should forward to interventionService', () => {
      const entity = { id: 123 };
      jest.spyOn(service, 'getInterventionIdentifier');
      const id = comp.trackId(0, entity);
      expect(service.getInterventionIdentifier).toHaveBeenCalledWith(entity);
      expect(id).toBe(entity.id);
    });
  });
});
