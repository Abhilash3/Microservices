package com.epam.onboarding.controller;

import com.epam.onboarding.domain.Product;
import com.epam.onboarding.domain.Review;
import com.epam.onboarding.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

@RestController
@RequestMapping("/products")
public class ProductController {

    private static final String REVIEW_SERVICE_URL = "http://localhost:8081/";
    private static final String NAME = "name";
    private static final String DESCRIPTION = "description";

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private IProductService productService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Iterable<Product> fetchAll() {
        Iterable<Product> products = productService.getAll();

        for (Product product : products) {
            product.add(linkTo(ProductController.class).withRel("products"));
            product.add(linkTo(ProductController.class).slash(product.getProductId()).withSelfRel());
        }

        return products;
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Product fetch(@PathVariable("id") Long productId) {
        Product product = productService.getById(productId);
        if (product != null) {
            product.add(linkTo(ProductController.class).withRel("products"));
            product.add(linkTo(ProductController.class).slash(product.getProductId()).withSelfRel());
        }

        return product;
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Product create(@RequestParam(NAME) String productName, @RequestParam(DESCRIPTION) String description) {
        Product product = productService.getByName(productName);
        if (product == null) {
            product = productService.save(new Product().setName(productName).setDescription(description));
        }
        product.add(linkTo(ProductController.class).withRel("products"));
        product.add(linkTo(ProductController.class).slash(product.getProductId()).withSelfRel());

        return product;
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Product update(@PathVariable("id") Long productId, @RequestBody UpdateRequest updateRequest) {
        Product product = productService.getById(productId);
        if (product == null) return null;

        if (NAME.equals(updateRequest.getProperty())) {
            product.setName(updateRequest.getValue());
        }
        if (DESCRIPTION.equals(updateRequest.getProperty())) {
            product.setDescription(updateRequest.getValue());
        }

        productService.save(product);
        return product;
    }

    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Product delete(@PathVariable("id") Long productId) {
        return productService.removeById(productId);
    }

    @PostMapping(value = "/{id}/reviews", produces = MediaType.APPLICATION_JSON_VALUE)
    public Review createReview(@PathVariable("id") Long productId,
                               @RequestParam(DESCRIPTION) String description, @RequestParam("rating") int rating) {
        String url = REVIEW_SERVICE_URL + productId + "/reviews";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        parameters.add(DESCRIPTION, description);
        parameters.add("rating", String.valueOf(rating));

        Review review = restTemplate.postForObject(url, new HttpEntity<>(parameters, headers), Review.class);
        review.add(linkTo(ProductController.class).slash(review.getProductId()).withSelfRel());

        return review;
    }

    @PutMapping(value = "/{productId}/reviews/{reviewId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Review updateReview(@PathVariable("productId") Long productId, @PathVariable("reviewId") Long reviewId,
                               @RequestBody UpdateRequest updateRequest) {
        String url = REVIEW_SERVICE_URL + productId + "/reviews/" + reviewId;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Review review = restTemplate.exchange(url, HttpMethod.PUT, new HttpEntity<>(updateRequest, headers), Review.class).getBody();
        review.add(linkTo(ProductController.class).slash(review.getProductId()).withSelfRel());

        return review;
    }

    @DeleteMapping(value = "/{productId}/reviews/{reviewId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Review deleteReview(@PathVariable("productId") Long productId, @PathVariable("reviewId") Long reviewId) {
        String url = REVIEW_SERVICE_URL + productId + "/reviews/" + reviewId;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        Review review = restTemplate.exchange(url, HttpMethod.DELETE, new HttpEntity<>(headers), Review.class).getBody();
        review.add(linkTo(ProductController.class).slash(review.getProductId()).withSelfRel());

        return review;
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
