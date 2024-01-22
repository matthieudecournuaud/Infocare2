import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { IApplicationUserTicket } from '../application-user-ticket.model';
import { ApplicationUserTicketService } from '../service/application-user-ticket.service';

import applicationUserTicketResolve from './application-user-ticket-routing-resolve.service';

describe('ApplicationUserTicket routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let service: ApplicationUserTicketService;
  let resultApplicationUserTicket: IApplicationUserTicket | null | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: {
            snapshot: {
              paramMap: convertToParamMap({}),
            },
          },
        },
      ],
    });
    mockRouter = TestBed.inject(Router);
    jest.spyOn(mockRouter, 'navigate').mockImplementation(() => Promise.resolve(true));
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRoute).snapshot;
    service = TestBed.inject(ApplicationUserTicketService);
    resultApplicationUserTicket = undefined;
  });

  describe('resolve', () => {
    it('should return IApplicationUserTicket returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      TestBed.runInInjectionContext(() => {
        applicationUserTicketResolve(mockActivatedRouteSnapshot).subscribe({
          next(result) {
            resultApplicationUserTicket = result;
          },
        });
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultApplicationUserTicket).toEqual({ id: 123 });
    });

    it('should return null if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      TestBed.runInInjectionContext(() => {
        applicationUserTicketResolve(mockActivatedRouteSnapshot).subscribe({
          next(result) {
            resultApplicationUserTicket = result;
          },
        });
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultApplicationUserTicket).toEqual(null);
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse<IApplicationUserTicket>({ body: null })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      TestBed.runInInjectionContext(() => {
        applicationUserTicketResolve(mockActivatedRouteSnapshot).subscribe({
          next(result) {
            resultApplicationUserTicket = result;
          },
        });
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultApplicationUserTicket).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
