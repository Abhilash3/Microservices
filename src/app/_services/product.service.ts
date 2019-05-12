import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

import { environment } from '../../environments/environment.prod';
import { Product } from '../_models';

@Injectable()
export class ProductService {
  constructor(private http: HttpClient) {}

  public products() {
    return this.http.get<Array<Product>>(`${environment.url}/products`);
  }

  public product(productId: string) {
    return this.http.get<Product>(`${environment.url}/products/${productId}`);
  }

  public add(name: string, description: string) {
    const formData = new FormData();
    formData.append('name', name);
    formData.append('description', description);

    return this.http.post<Product>(`${environment.url}/products`, formData);
  }

  public update(productId: number, property: string, value: string) {
    return this.http.put<Product>(`${environment.url}/products/${productId}`, {property, value});
  }

  public delete(productId: number) {
    return this.http.delete<Product>(`${environment.url}/products/${productId}`);
  }
}
