import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AuthGuard } from './auth/auth.guard';
import { LoginGuard } from './auth/login.guard';
import { AdminGuard } from './auth/admin.guard';

import { LoginComponent } from './login/login.component';
import { RegistrationComponent } from './registration/registration.component';
import { UserHomeComponent } from './user/home/home.component';
import { UserOrdersComponent } from './user/orders/orders.component';
import { UserOrderDetailComponent } from './user/order-detail/order-detail.component';
import { UserProductDetailComponent } from './user/product-detail/product-detail.component';
import { UserProductComponent } from './user/product/product.component';
import { UserCartComponent } from './user/cart/cart.component';
import { UserWatchlistComponent } from './user/watchlist/watchlist.component';
import { UnauthorizedComponent } from './common/unauthorization/unauthorized.component';

import { AdminHomeComponent } from './admin/home/home.component';
import { AdminProductDetailComponent } from './admin/product-detail/product-detail.component';
import { AdminProductEditComponent } from './admin/product-edit/product-edit.component';
import { AdminProductFormComponent } from './admin/product-form/product-form.component';
import { AdminProductComponent } from './admin/product/product.component';
import { AdminOrderComponent } from './admin/order/order.component';
import { AdminOrderDetailComponent } from './admin/order-detail/order-detail.component';

const routes: Routes = [
  { path: 'login', component: LoginComponent, canActivate: [LoginGuard] },
  { path: 'register', component: RegistrationComponent },
  // user
  { path: 'user/home', component: UserHomeComponent, canActivate: [AuthGuard] },
  {
    path: 'user/orders',
    component: UserOrdersComponent,
    canActivate: [AuthGuard],
  },
  {
    path: 'user/orders/:orderId',
    component: UserOrderDetailComponent,
    canActivate: [AuthGuard],
  },
  {
    path: 'user/product/:productId',
    component: UserProductDetailComponent,
    canActivate: [AuthGuard],
  },
  {
    path: 'user/product',
    component: UserProductComponent,
    canActivate: [AuthGuard],
  },
  {
    path: 'user/cart',
    component: UserCartComponent,
    canActivate: [AuthGuard],
  },
  {
    path: 'user/watchlist',
    component: UserWatchlistComponent,
    canActivate: [AuthGuard],
  },
  // admin
  {
    path: 'admin/home',
    component: AdminHomeComponent,
    canActivate: [AdminGuard],
  },
  {
    path: 'admin/product',
    component: AdminProductComponent,
    canActivate: [AdminGuard],
  },
  {
    path: 'admin/product/:productId',
    component: AdminProductDetailComponent,
    canActivate: [AdminGuard],
  },
  {
    path: 'admin/product-edit/:productId',
    component: AdminProductEditComponent,
    canActivate: [AdminGuard],
  },
  {
    path: 'admin/product-form',
    component: AdminProductFormComponent,
    canActivate: [AdminGuard],
  },
  {
    path: 'admin/orders',
    component: AdminOrderComponent,
    canActivate: [AdminGuard],
  },
  {
    path: 'admin/orders/:orderId',
    component: AdminOrderDetailComponent,
    canActivate: [AdminGuard],
  },
  { path: '', redirectTo: '/login', pathMatch: 'full' },
  { path: 'unauthorized', component: UnauthorizedComponent },
  { path: '**', redirectTo: '/login' },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
