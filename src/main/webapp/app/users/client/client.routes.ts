import { Routes } from '@angular/router';
import MesTicketsComponent from './mes-tickets/mes-tickets.component';
import MaterielsComponent from './materiels/materiels.component';
import { olsenGuard } from 'app/olsen.guard';
import NotificationsComponent from './notifications/notifications.component';
import { ASC } from 'app/config/navigation.constants';

const clientRoutes: Routes = [
  {
    path: 'materiels',
    component: MaterielsComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [olsenGuard],
  },
  {
    path: 'mes-tickets',
    component: MesTicketsComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [olsenGuard],
  },
  {
    path: 'notifications',
    component: NotificationsComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [olsenGuard],
  },
];

export default clientRoutes;
