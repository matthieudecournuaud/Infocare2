import { Routes } from '@angular/router';

const routes: Routes = [
  {
    path: 'ticket',
    data: { pageTitle: 'infocare2App.ticket.home.title' },
    loadChildren: () => import('./ticket/ticket.routes'),
  },
  {
    path: 'application-user',
    data: { pageTitle: 'infocare2App.applicationUser.home.title' },
    loadChildren: () => import('./application-user/application-user.routes'),
  },
  {
    path: 'application-user-ticket',
    data: { pageTitle: 'infocare2App.applicationUserTicket.home.title' },
    loadChildren: () => import('./application-user-ticket/application-user-ticket.routes'),
  },
  {
    path: 'category',
    data: { pageTitle: 'infocare2App.category.home.title' },
    loadChildren: () => import('./category/category.routes'),
  },
  {
    path: 'status',
    data: { pageTitle: 'infocare2App.status.home.title' },
    loadChildren: () => import('./status/status.routes'),
  },
  {
    path: 'priority',
    data: { pageTitle: 'infocare2App.priority.home.title' },
    loadChildren: () => import('./priority/priority.routes'),
  },
  {
    path: 'material',
    data: { pageTitle: 'infocare2App.material.home.title' },
    loadChildren: () => import('./material/material.routes'),
  },
  {
    path: 'company',
    data: { pageTitle: 'infocare2App.company.home.title' },
    loadChildren: () => import('./company/company.routes'),
  },
  {
    path: 'comment',
    data: { pageTitle: 'infocare2App.comment.home.title' },
    loadChildren: () => import('./comment/comment.routes'),
  },
  {
    path: 'intervention',
    data: { pageTitle: 'infocare2App.intervention.home.title' },
    loadChildren: () => import('./intervention/intervention.routes'),
  },
  {
    path: 'procedure',
    data: { pageTitle: 'infocare2App.procedure.home.title' },
    loadChildren: () => import('./procedure/procedure.routes'),
  },
  /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
];

export default routes;
