package com.epam.onboarding.controller;

import com.epam.onboarding.domain.Review;
import com.epam.onboarding.service.IReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ReviewController {

    private static final String DESCRIPTION = "description";
    private static final String RATING = "rating";

    @Autowired
    private DiscoveryClient discoveryClient;

    @Autowired
    private IReviewService reviewService;

    @RequestMapping("/service-instances/{application}")
    public ServiceInstance service(@PathVariable String application) {
        List<ServiceInstance> instances = discoveryClient.getInstances(application);
        if (instances == null || instances.isEmpty()) return null;

        return instances.get(0);
    }

    @GetMapping(value = "/{productId}/reviews", produces = MediaType.APPLICATION_JSON_VALUE)
    public Iterable<Review> fetchAll(@PathVariable("productId") Long productId) {
        return reviewService.getAllForProduct(productId);
    }

    @GetMapping(value = "/{productId}/reviews/{reviewId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Review fetch(@PathVariable("reviewId") Long reviewId) {
        return reviewService.getById(reviewId);
    }

    @PostMapping(value = "/{productId}/reviews", produces = MediaType.APPLICATION_JSON_VALUE)
    public Review create(@PathVariable("productId") Long productId,
                         @RequestParam("description") String description, @RequestParam("rating") Integer rating) {
        return reviewService.save(new Review().setProductId(productId).setRating(rating).setDescription(description));
    }

    @PutMapping(value = "/{productId}/reviews/{reviewId}", produces = MediaType.APPLICATION_JSON_VALUE)
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

    @DeleteMapping(value = "/{productId}/reviews/{reviewId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Review delete(@PathVariable("reviewId") Long reviewId) {
        return reviewService.removeById(reviewId);
    }

    static class UpdateRequest {
        private String property;
        private String value;

        String getProperty() {
            return property;
        }

        UpdateRequest setProperty(String property) {
            this.property = property;
            return this;
        }

        String getValue() {
            return value;
        }

        UpdateRequest setValue(String value) {
            this.value = value;
            return this;
        }
    }
}
