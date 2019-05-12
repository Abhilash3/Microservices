import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { AddProductComponent, ProductComponent, ProductListComponent } from './products';
import { AddReviewComponent, ReviewListComponent } from './reviews';

const routes: Routes = [
  { path: 'products'           , component: ProductListComponent },
  { path: 'products/add'       , component: AddProductComponent },
  { path: 'products/:productId', component: ProductComponent, children: [
    { path: ''                 , component: ReviewListComponent, outlet: 'review' },
    { path: 'reviews'          , component: ReviewListComponent, outlet: 'review' },
    { path: 'reviews/add'      , component: AddReviewComponent, outlet: 'review' },
  ] },

  { path: '**', redirectTo: 'products' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
