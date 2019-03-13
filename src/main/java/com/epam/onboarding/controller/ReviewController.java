package com.epam.onboarding.controller;

import com.epam.onboarding.domain.Review;
import com.epam.onboarding.service.IReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/{productId}/reviews")
public class ReviewController {

    private static final String DESCRIPTION = "description";
    private static final String RATING = "rating";

    @Autowired
    private IReviewService reviewService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Iterable<Review> fetchAll(@PathVariable("productId") Long productId) {
        return reviewService.getAllForProduct(productId);
    }

    @GetMapping(value = "/{reviewId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Review fetch(@PathVariable("reviewId") Long reviewId) {
        return reviewService.getById(reviewId);
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Review create(@PathVariable("productId") Long productId,
                         @RequestParam("description") String description, @RequestParam("rating") Integer rating) {
        return reviewService.save(new Review().setProductId(productId).setRating(rating).setDescription(description));
    }

    @PutMapping(value = "/{reviewId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Review update(@PathVariable("reviewId") Long reviewId, @RequestBody UpdateRequest updateRequest) {
        Review review = reviewService.getById(reviewId);
        if (review == null) return null;

        if (DESCRIPTION.equals(updateRequest.getProperty())) {
            review.setDescription(updateRequest.getValue());
        }
        if (RATING.equals(updateRequest.getProperty())) {
            review.setRating(Integer.parseInt(updateRequest.getValue()));
        }

        reviewService.save(review);

        return review;
    }

    @DeleteMapping(value = "/{reviewId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Review delete(@PathVariable("reviewId") Long reviewId) {
        return reviewService.removeById(reviewId);
    }

    static class UpdateRequest {
        private String property;
        private String value;

        public String getProperty() {
            return property;
        }

        public UpdateRequest setProperty(String property) {
            this.property = property;
            return this;
        }

        public String getValue() {
            return value;
        }

        public UpdateRequest setValue(String value) {
            this.value = value;
            return this;
        }
    }
}
