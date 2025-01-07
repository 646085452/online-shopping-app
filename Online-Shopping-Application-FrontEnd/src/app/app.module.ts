import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ReactiveFormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { AppRoutingModule } from './app-routing.module';

import { AppComponent } from './app.component';
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

@NgModule({
  imports: [
    BrowserModule,
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    HttpClientModule,
    AppRoutingModule,
  ],
  declarations: [
    AppComponent,
    LoginComponent,
    RegistrationComponent,
    UserHomeComponent,
    UserOrdersComponent,
    UserOrderDetailComponent,
    UserProductDetailComponent,
    UserProductComponent,
    UserCartComponent,
    UserWatchlistComponent,
    UnauthorizedComponent,
    AdminHomeComponent,
    AdminProductComponent,
    AdminProductDetailComponent,
    AdminProductEditComponent,
    AdminProductFormComponent,
    AdminOrderComponent,
    AdminOrderDetailComponent,
  ],
  providers: [],
  bootstrap: [AppComponent],
})
export class AppModule {}
