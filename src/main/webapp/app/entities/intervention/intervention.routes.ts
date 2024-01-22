import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { InterventionComponent } from './list/intervention.component';
import { InterventionDetailComponent } from './detail/intervention-detail.component';
import { InterventionUpdateComponent } from './update/intervention-update.component';
import InterventionResolve from './route/intervention-routing-resolve.service';

const interventionRoute: Routes = [
  {
    path: '',
    component: InterventionComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: InterventionDetailComponent,
    resolve: {
      intervention: InterventionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: InterventionUpdateComponent,
    resolve: {
      intervention: InterventionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: InterventionUpdateComponent,
    resolve: {
      intervention: InterventionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default interventionRoute;
