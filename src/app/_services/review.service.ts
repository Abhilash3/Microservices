import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

import { environment } from '../../environments/environment.prod';
import { Review } from '../_models';

@Injectable()
export class ReviewService {
  constructor(private http: HttpClient) {}

  public reviews(productId: number) {
    return this.http.get<Array<Review>>(`${environment.url}/products/${productId}/reviews`);
  }

  public add(productId: string, rating: string, description: string) {
    const formData = new FormData();
    formData.append('rating', rating);
    formData.append('description', description);

    return this.http.post<Review>(`${environment.url}/products/${productId}/reviews`, formData);
  }

  public update(productId: number, reviewId: number, property: string, value: string) {
    return this.http.put<Review>(`${environment.url}/products/${productId}/reviews/${reviewId}`, {property, value});
  }

  public delete(productId: number, reviewId: number) {
    return this.http.delete<Review>(`${environment.url}/products/${productId}/reviews/${reviewId}`);
  }
}
