package com.epam.onboarding.service;

import com.epam.onboarding.dao.ReviewDAO;
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
    public void saveProduct() {
    }

    @Test
    public void findAllProducts() {
    }

    @Test
    public void findProductByName() {
    }

    @Test
    public void findProductById() {
    }

    @Test
    public void removeProduct() {
    }

    @Test
    public void removeProductById() {
    }
}