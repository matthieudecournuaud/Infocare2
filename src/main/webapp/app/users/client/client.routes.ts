import { Routes } from '@angular/router';

const routes: Routes = [
  {
    path: 'materiels',
    loadComponent: () => import('./materiels/materiels.component'),
  },
  {
    path: 'mes-tickets',
    loadComponent: () => import('./mes-tickets/mes-tickets.component'),
  },
  {
    path: 'notifications',
    loadComponent: () => import('./notifications/notifications.component'),
  },
];

export default routes;
