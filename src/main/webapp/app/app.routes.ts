import { Routes } from '@angular/router';

import { Authority } from 'app/config/authority.constants';
import { errorRoute } from './layouts/error/error.route';

import { HomeComponent } from './accueil/home/home.component';
import NavbarComponent from './layouts/navbar/navbar.component';
import LoginComponent from './login/login.component';
import { olsenGuard } from './olsen.guard';

const routes: Routes = [
  {
    path: '',
    component: HomeComponent,
    title: 'home.title',
  },
  {
    path: '',
    component: NavbarComponent,
    outlet: 'navbar',
  },
  {
    path: 'admin',
    data: {
      authorities: [Authority.ADMIN],
    },
    canActivate: [olsenGuard],
    loadChildren: () => import('./users/admin/admin.routes'),
  },
  {
    path: 'technician',
    data: {
      authorities: [Authority.USER],
    },
    canActivate: [olsenGuard],
    loadChildren: () => import('./users/technicien/technicien.routes'),
  },
  {
    path: 'client',
    data: {
      authorities: [Authority.CLIENT],
    },
    loadChildren: () => import('./users/client/client.routes'),
  },
  {
    path: 'account',
    loadChildren: () => import('./account/account.route'),
  },
  {
    path: 'login',
    component: LoginComponent,
    title: 'login.title',
  },
  {
    path: '',
    loadChildren: () => import(`./entities/entity.routes`),
  },
  ...errorRoute,
];

export default routes;
