import { Component, OnInit, Input } from '@angular/core';

import { Review } from '../../_models';
import { ReviewService } from '../../_services';

@Component({
  selector: 'app-review-list',
  templateUrl: './review-list.component.html',
  styleUrls: ['./review-list.component.css']
})
export class ReviewListComponent implements OnInit {
  @Input() public productId: number;

  public reviews: Array<Review> = [];
  public delete: Array<boolean> = [];

  constructor(private reviewService: ReviewService) {}

  ngOnInit() {
    if (this.productId) {
      this.reviewService.reviews(this.productId).subscribe(data => {
        this.reviews = data;
        this.delete = new Array(data.length).fill(false);
      }, data => console.log(data));
    }
  }

  deleteReview(index: number) {
    this.reviewService.delete(this.productId, this.reviews[index].reviewId).subscribe(data => {
      console.log(data);
      this.reviews.splice(index, 1);
      this.delete.splice(index, 1);
    }, console.log);
  }
}
