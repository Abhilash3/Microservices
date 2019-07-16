import { Component, OnInit } from '@angular/core';

import { Product } from '../../_models';
import { ProductService } from '../../_services';

@Component({
  selector: 'app-product-list',
  templateUrl: './product-list.component.html',
  styleUrls: ['./product-list.component.css']
})
export class ProductListComponent implements OnInit {
  public products: Array<Product> = [];
  public delete: Array<boolean> = [];

  constructor(private productService: ProductService) {}

  ngOnInit() {
    this.productService.products().subscribe(data => {
      this.products = data;
      this.delete = new Array(data.length).fill(false);
    });
  }

  deleteProduct(index: number) {
    this.productService.delete(this.products[index].productId).subscribe(data => {
      console.log(data);
      this.products.splice(index, 1);
      this.delete.splice(index, 1);
    }, console.log);
  }
}
