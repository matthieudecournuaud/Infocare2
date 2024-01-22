import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { ApplicationUserTicketComponent } from './list/application-user-ticket.component';
import { ApplicationUserTicketDetailComponent } from './detail/application-user-ticket-detail.component';
import { ApplicationUserTicketUpdateComponent } from './update/application-user-ticket-update.component';
import ApplicationUserTicketResolve from './route/application-user-ticket-routing-resolve.service';

const applicationUserTicketRoute: Routes = [
  {
    path: '',
    component: ApplicationUserTicketComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ApplicationUserTicketDetailComponent,
    resolve: {
      applicationUserTicket: ApplicationUserTicketResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ApplicationUserTicketUpdateComponent,
    resolve: {
      applicationUserTicket: ApplicationUserTicketResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ApplicationUserTicketUpdateComponent,
    resolve: {
      applicationUserTicket: ApplicationUserTicketResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default applicationUserTicketRoute;
