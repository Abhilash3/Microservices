package com.epam.onboarding.domain;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "reviews")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long reviewId;
    private Long productId;
    private String description;
    private Integer rating;

    public Long getReviewId() {
        return reviewId;
    }

    public Review setReviewId(Long reviewId) {
        this.reviewId = reviewId;
        return this;
    }

    public Long getProductId() {
        return productId;
    }

    public Review setProductId(Long productId) {
        this.productId = productId;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public Review setDescription(String description) {
        this.description = description;
        return this;
    }

    public int getRating() {
        return rating;
    }

    public Review setRating(Integer rating) {
        this.rating = rating;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Review review = (Review) o;
        return Objects.equals(reviewId, review.reviewId) &&
                Objects.equals(productId, review.productId) &&
                Objects.equals(rating, review.rating) &&
                Objects.equals(description, review.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(reviewId, productId, description, rating);
    }

    @Override
    public String toString() {
        return "Review{reviewId=" + reviewId +
                ", productId=" + productId +
                ", description='" + description + '\'' +
                ", rating=" + rating +
                '}';
    }
}
