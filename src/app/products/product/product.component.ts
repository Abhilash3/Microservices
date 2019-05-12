import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { forkJoin } from 'rxjs';

import { Product } from '../../_models';
import { ProductService } from '../../_services';

@Component({
  selector: 'app-product',
  templateUrl: './product.component.html',
  styleUrls: ['./product.component.css']
})
export class ProductComponent implements OnInit {
  public product: Product;

  public name: string;
  public editName: boolean = false;

  public description: string;
  public editDescription: boolean = false;

  constructor(private router: Router,
              private route: ActivatedRoute,
              private productService: ProductService) {}

  ngOnInit() {
    this.route.params.subscribe(paramMap => {
      const productId = paramMap.productId;
      if (productId) {
        this.productService.product(productId).subscribe(data => {
          this.product = data;

          this.resetName();
          this.resetDescription();
        }, data => {
          console.log(data);
          this.routeToProducts();
        });
      } else {
        this.routeToProducts();
      }
    });
  }

  private routeToProducts() {
    this.router.navigate(['/products']);
  }

  componentAdded(component) {
    component.productId = this.product.productId;
  }

  resetName() {
    this.name = this.product.name;
    this.editName = false;
  }

  resetDescription() {
    this.description = this.product.description;
    this.editDescription = false;
  }

  updateName() {
    this.updateProduct('name', this.name);
  }

  updateDescription() {
    this.updateProduct('description', this.description);
  }

  private updateProduct(property: string, value: string) {
    this.productService.update(this.product.productId, property, value).subscribe(data => {
      this.product = data;
      this[`edit${property[0].toUpperCase()}${property.slice(1)}`] = false;
    }, console.log);
  }
}
