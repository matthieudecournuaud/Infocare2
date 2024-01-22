import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { ProcedureComponent } from './list/procedure.component';
import { ProcedureDetailComponent } from './detail/procedure-detail.component';
import { ProcedureUpdateComponent } from './update/procedure-update.component';
import ProcedureResolve from './route/procedure-routing-resolve.service';

const procedureRoute: Routes = [
  {
    path: '',
    component: ProcedureComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ProcedureDetailComponent,
    resolve: {
      procedure: ProcedureResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ProcedureUpdateComponent,
    resolve: {
      procedure: ProcedureResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ProcedureUpdateComponent,
    resolve: {
      procedure: ProcedureResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default procedureRoute;
