package com.epam.onboarding.controller;

import com.epam.onboarding.dao.ReviewDAO;
import com.epam.onboarding.domain.Review;
import com.epam.onboarding.service.IReviewService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.any;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@RunWith(SpringRunner.class)
@WebMvcTest(ReviewController.class)
@ActiveProfiles("test")
public class ReviewControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private ReviewDAO reviewDAO;

    @MockBean
    private IReviewService reviewService;

    @Test
    public void fetchProduct() throws Exception {
    }

    @Test
    public void fetchProducts() throws Exception {
    }

    @Test
    public void createProduct() throws Exception {
    }

    @Test
    public void createDuplicateProduct() throws Exception {
    }

    @Test
    public void updateProduct() throws Exception {
    }

    @Test
    public void deleteProduct() throws Exception {
    }

    private Review review(String description) {
        return new Review().setDescription(description).setReviewId(100L);
    }
}