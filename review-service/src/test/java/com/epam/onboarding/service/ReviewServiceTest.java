package com.epam.onboarding.service;

import com.epam.onboarding.dao.ReviewDAO;
import com.epam.onboarding.domain.Review;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class ReviewServiceTest {

    @Autowired
    private ReviewService reviewService;

    @MockBean
    private ReviewDAO reviewDAO;

    @Test
    public void saveReview() {
        when(reviewDAO.findAll()).thenReturn(Collections.emptyList());

        Review review = review("ok", 3);
        reviewService.save(review);

        verify(reviewDAO).save(review);
    }

    @Test
    public void findAllReviews() {
        List<Review> reviews = Arrays.asList(review(100L, "ok", 3), review(110L, "bad", 2), review(120L, "good", 4));
        when(reviewDAO.findAll()).thenReturn(reviews);

        assertEquals(reviews, reviewService.getAll());
        verify(reviewDAO).findAll();
    }

    @Test
    public void findReviewsByProduct() {
        List<Review> reviews = Arrays.asList(review(100L, 10L, "ok", 3), review(110L, 20L, "bad", 2), review(120L, 10L, "good", 4));
        when(reviewDAO.findAll()).thenReturn(reviews);

        List<Review> productReviews = Arrays.asList(review(100L, 10L, "ok", 3), review(120L, 10L, "good", 4));
        assertEquals(productReviews, reviewService.getAllForProduct(10L));
        verify(reviewDAO).findAll();
    }

    @Test
    public void findReviewById() {
        List<Review> reviews = Arrays.asList(review(100L, "ok", 3), review(110L, "bad", 2), review(120L, "good", 4));
        reviews.forEach(review -> when(reviewDAO.findById(review.getReviewId())).thenReturn(Optional.of(review)));

        assertEquals(review(100L, "ok", 3), reviewService.getById(100L));
        assertEquals(review(110L, "bad", 2), reviewService.getById(110L));

        verify(reviewDAO, times(2)).findById(anyLong());
    }

    @Test
    public void removeReview() {
        Review review = review("bad", 2);
        assertNotNull(review.getProductId());

        reviewService.remove(review);

        verify(reviewDAO).delete(review);
        assertNull(review.getReviewId());
    }

    @Test
    public void removeReviewById() {
        Review review = review(200L, "v.good", 5);

        when(reviewDAO.findById(review.getReviewId())).thenReturn(Optional.of(review));

        reviewService.removeById(200L);

        verify(reviewDAO).findById(200L);
        verify(reviewDAO).deleteById(200L);
    }

    private Review review(String description, int rating) {
        return review(10L, description, rating);
    }

    private Review review(Long reviewId, String description, int rating) {
        return review(reviewId, 10L, description, rating);
    }

    private Review review(Long reviewId, Long productId, String description, int rating) {
        return new Review().setReviewId(reviewId).setProductId(productId).setDescription(description).setRating(rating);
    }
}