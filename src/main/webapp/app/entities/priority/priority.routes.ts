import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { PriorityComponent } from './list/priority.component';
import { PriorityDetailComponent } from './detail/priority-detail.component';
import { PriorityUpdateComponent } from './update/priority-update.component';
import PriorityResolve from './route/priority-routing-resolve.service';

const priorityRoute: Routes = [
  {
    path: '',
    component: PriorityComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PriorityDetailComponent,
    resolve: {
      priority: PriorityResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PriorityUpdateComponent,
    resolve: {
      priority: PriorityResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PriorityUpdateComponent,
    resolve: {
      priority: PriorityResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default priorityRoute;
