import { Component } from '@angular/core';
import { Router } from '@angular/router';

import { Product } from '../../_models';
import { ProductService } from '../../_services';

@Component({
  selector: 'app-add-product',
  templateUrl: './add-product.component.html',
  styleUrls: ['./add-product.component.css']
})
export class AddProductComponent {
  public name: string = '';
  public description: string = '';

  constructor(private productService: ProductService) {}

  addProduct() {
    this.productService.add(this.name, this.description).subscribe(data => {
      console.log(data);
      this.reset();
    }, console.log);
  }

  private reset() {
    this.name = '';
    this.description = '';
  }
}
