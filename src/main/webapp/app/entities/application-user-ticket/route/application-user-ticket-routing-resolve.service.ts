import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IApplicationUserTicket } from '../application-user-ticket.model';
import { ApplicationUserTicketService } from '../service/application-user-ticket.service';

export const applicationUserTicketResolve = (route: ActivatedRouteSnapshot): Observable<null | IApplicationUserTicket> => {
  const id = route.params['id'];
  if (id) {
    return inject(ApplicationUserTicketService)
      .find(id)
      .pipe(
        mergeMap((applicationUserTicket: HttpResponse<IApplicationUserTicket>) => {
          if (applicationUserTicket.body) {
            return of(applicationUserTicket.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default applicationUserTicketResolve;
