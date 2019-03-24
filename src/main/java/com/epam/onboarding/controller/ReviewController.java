package com.epam.onboarding.controller;

import com.epam.onboarding.domain.Review;
import feign.RequestInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@FeignClient(value = "review-service", configuration = ReviewController.FeignConfiguration.class)
public interface ReviewController {

    @GetMapping(value = "/{productId}/reviews", produces = MediaType.APPLICATION_JSON_VALUE)
    Iterable<Review> fetchAll(@PathVariable("productId") Long productId);

    @GetMapping(value = "/{productId}/reviews/{reviewId}", produces = MediaType.APPLICATION_JSON_VALUE)
    Review fetch(@PathVariable("productId") Long productId, @PathVariable("reviewId") Long reviewId);

    @PostMapping(value = "/{productId}/reviews", produces = MediaType.APPLICATION_JSON_VALUE)
    Review create(@PathVariable("productId") Long productId, @RequestParam("description") String description,
                  @RequestParam("rating") Integer rating);

    @PutMapping(value = "/{productId}/reviews/{reviewId}", produces = MediaType.APPLICATION_JSON_VALUE)
    Review update(@PathVariable("productId") Long productId, @PathVariable("reviewId") Long reviewId,
                  @RequestBody UpdateRequest updateRequest);

    @DeleteMapping(value = "/{productId}/reviews/{reviewId}", produces = MediaType.APPLICATION_JSON_VALUE)
    Review delete(@PathVariable("productId") Long productId, @PathVariable("reviewId") Long reviewId);

    class FeignConfiguration {

        @Value("${api-header-name}")
        private String headerName;

        @Value("${api-header-value}")
        private String headerValue;

        @Bean
        public RequestInterceptor requestInterceptor() {
            return requestTemplate -> requestTemplate.header(headerName, headerValue);
        }
    }
}
