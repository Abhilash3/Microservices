package com.epam.onboarding.controller;

import com.epam.onboarding.dao.ReviewDAO;
import com.epam.onboarding.domain.Review;
import com.epam.onboarding.service.IReviewService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(value = ReviewController.class, secure = false)
@ActiveProfiles("test")
public class ReviewControllerTest {

    private static final String REVIEW_DESCRIPTION_EXPRESSION = "$.description";
    private static final String D_REVIEWS_D = "/%d/reviews/%d";

    @Autowired
    private MockMvc mvc;

    @MockBean
    private ReviewDAO reviewDAO;

    @MockBean
    private IReviewService reviewService;

    @MockBean
    private DiscoveryClient discoveryClient;

    @Test
    public void fetchReview() throws Exception {
        Review review = review("ok", 3);

        when(reviewService.getById(review.getReviewId())).thenReturn(review);

        mvc.perform(get(String.format(D_REVIEWS_D, review.getProductId(), review.getReviewId())).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath(REVIEW_DESCRIPTION_EXPRESSION, is(review.getDescription())));
        verify(reviewService).getById(review.getReviewId());
    }

    @Test
    public void fetchReviews() throws Exception {
        List<Review> reviews = Arrays.asList(review(100L, "ok", 3), review(110L, "bad", 2), review(120L, "good", 4));

        when(reviewService.getAllForProduct(10L)).thenReturn(reviews);

        mvc.perform(get("/10/reviews").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].description", is(reviews.get(0).getDescription())))
                .andExpect(jsonPath("$[1].description", is(reviews.get(1).getDescription())))
                .andExpect(jsonPath("$[2].description", is(reviews.get(2).getDescription())));
        verify(reviewService).getAllForProduct(10L);
    }

    @Test
    public void createReview() throws Exception {
        Review review = review(null, 20L, "bad", 2);

        when(reviewService.save(any(Review.class))).thenReturn(review);

        mvc.perform(
                post("/20/reviews")
                        .param("description", review.getDescription())
                        .param("rating", String.valueOf(review.getRating()))
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath(REVIEW_DESCRIPTION_EXPRESSION, is(review.getDescription())));

        verify(reviewService).save(eq(review));
    }

    @Test
    public void updateReview() throws Exception {
        Review review = review("v.good", 5);

        when(reviewService.getById(review.getReviewId())).thenReturn(review);

        mvc.perform(
                put(String.format(D_REVIEWS_D, review.getProductId(), review.getReviewId()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"property\": \"description\", \"value\": \"very good\"}")
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath(REVIEW_DESCRIPTION_EXPRESSION, is("very good")));

        verify(reviewService).getById(review.getProductId());
        verify(reviewService).save(eq(review));
    }

    @Test
    public void deleteReview() throws Exception {
        Review review = review("bad", 2);

        when(reviewService.removeById(review.getReviewId())).thenReturn(review);

        mvc.perform(delete(String.format(D_REVIEWS_D, review.getProductId(), review.getReviewId())).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath(REVIEW_DESCRIPTION_EXPRESSION, is(review.getDescription())));

        verify(reviewService).removeById(review.getReviewId());
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