import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { LocationStrategy, HashLocationStrategy } from '@angular/common';
import { HttpClientModule } from '@angular/common/http';

import { ProductService, ReviewService } from './_services';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { AddProductComponent, ProductComponent, ProductListComponent } from './products';
import { AddReviewComponent, ReviewListComponent } from './reviews';

@NgModule({
  declarations: [
    AppComponent,
    AddProductComponent,
    ProductComponent,
    ProductListComponent,
    AddReviewComponent,
    ReviewListComponent,
  ],
  imports: [
    FormsModule,
    BrowserModule,
    HttpClientModule,
    AppRoutingModule,
  ],
  providers: [
    ProductService,
    ReviewService,

    { provide: LocationStrategy, useClass: HashLocationStrategy },
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
