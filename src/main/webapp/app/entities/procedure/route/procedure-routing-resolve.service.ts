import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IProcedure } from '../procedure.model';
import { ProcedureService } from '../service/procedure.service';

export const procedureResolve = (route: ActivatedRouteSnapshot): Observable<null | IProcedure> => {
  const id = route.params['id'];
  if (id) {
    return inject(ProcedureService)
      .find(id)
      .pipe(
        mergeMap((procedure: HttpResponse<IProcedure>) => {
          if (procedure.body) {
            return of(procedure.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default procedureResolve;
