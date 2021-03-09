package com.epam.onboarding;

import com.epam.onboarding.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Order(1)
@Profile("!test")
public class ApplicationInit implements CommandLineRunner {

    @Autowired
    private ReviewService reviewService;

    @Override
    public void run(String... args) {
        reviewService.getAll().forEach(a -> reviewService.removeById(a.getReviewId()));
    }
}
