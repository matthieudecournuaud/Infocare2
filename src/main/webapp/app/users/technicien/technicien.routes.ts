import { Routes } from '@angular/router';

const routes: Routes = [
  {
    path: 'gestion-tickets',
    loadComponent: () => import('./gestion-tickets/gestion-tickets.component'),
  },
  {
    path: 'interventions',
    loadComponent: () => import('./interventions/interventions.component'),
  },
  {
    path: 'materiels',
    loadComponent: () => import('./materiels/materiels.component'),
  },
  {
    path: 'procedures',
    loadComponent: () => import('./procedures/procedures.component'),
  },
  {
    path: 'materiels',
    loadComponent: () => import('./profil/profil.component'),
  },
];

export default routes;
