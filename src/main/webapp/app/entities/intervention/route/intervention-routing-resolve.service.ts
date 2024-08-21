import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IIntervention } from '../intervention.model';
import { InterventionService } from '../service/intervention.service';

const interventionResolve = (route: ActivatedRouteSnapshot): Observable<null | IIntervention> => {
  const id = route.params['id'];
  if (id) {
    return inject(InterventionService)
      .find(id)
      .pipe(
        mergeMap((intervention: HttpResponse<IIntervention>) => {
          if (intervention.body) {
            return of(intervention.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default interventionResolve;
