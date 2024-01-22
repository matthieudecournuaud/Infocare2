import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { ApplicationUserTicketService } from '../service/application-user-ticket.service';

import { ApplicationUserTicketComponent } from './application-user-ticket.component';

describe('ApplicationUserTicket Management Component', () => {
  let comp: ApplicationUserTicketComponent;
  let fixture: ComponentFixture<ApplicationUserTicketComponent>;
  let service: ApplicationUserTicketService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        RouterTestingModule.withRoutes([{ path: 'application-user-ticket', component: ApplicationUserTicketComponent }]),
        HttpClientTestingModule,
        ApplicationUserTicketComponent,
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
      .overrideTemplate(ApplicationUserTicketComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ApplicationUserTicketComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(ApplicationUserTicketService);

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
    expect(comp.applicationUserTickets?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });

  describe('trackId', () => {
    it('Should forward to applicationUserTicketService', () => {
      const entity = { id: 123 };
      jest.spyOn(service, 'getApplicationUserTicketIdentifier');
      const id = comp.trackId(0, entity);
      expect(service.getApplicationUserTicketIdentifier).toHaveBeenCalledWith(entity);
      expect(id).toBe(entity.id);
    });
  });
});
