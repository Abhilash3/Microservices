import { Component, OnInit, Input } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { forkJoin } from 'rxjs';

import { Review } from '../../_models';
import { ReviewService } from '../../_services';

@Component({
  selector: 'app-add-review',
  templateUrl: './add-review.component.html',
  styleUrls: ['./add-review.component.css']
})
export class AddReviewComponent {
  @Input() public productId: string;

  public rating: string = '1';
  public description: string = '';

  constructor(private router: Router,
              private reviewService: ReviewService) {}

  addReview() {
    this.reviewService.add(this.productId, this.rating, this.description).subscribe(data => {
      console.log(data);
      this.reset();
    }, console.log);
  }

  private reset() {
    this.rating = '1';
    this.description = '';
  }
}
