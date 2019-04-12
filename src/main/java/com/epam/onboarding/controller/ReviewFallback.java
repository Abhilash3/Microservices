package com.epam.onboarding.controller;

import com.epam.onboarding.domain.Review;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
public class ReviewFallback implements ReviewController {

    @Override
    public Iterable<Review> fetchAll(Long productId) {
        return Collections.emptyList();
    }

    @Override
    public Review fetch(Long productId, Long reviewId) {
        return new Review().setProductId(productId).setReviewId(reviewId);
    }

    @Override
    public Review create(Long productId, String description, Integer rating) {
        return new Review().setProductId(productId).setDescription(description).setRating(rating);
    }

    @Override
    public Review update(Long productId, Long reviewId, UpdateRequest updateRequest) {
        Review review = new Review().setReviewId(reviewId).setProductId(productId);
        if ("description".equals(updateRequest.getProperty())) {
            review.setDescription(updateRequest.getValue());
        } else if (updateRequest.getValue().matches("-?\\d+")){
            review.setRating(Integer.parseInt(updateRequest.getValue()));
        }

        return review;
    }

    @Override
    public Review delete(Long productId, Long reviewId) {
        return new Review().setReviewId(reviewId).setProductId(productId);
    }
}
