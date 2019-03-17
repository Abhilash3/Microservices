package com.epam.onboarding.controller;

import com.epam.onboarding.domain.Product;
import com.epam.onboarding.domain.Review;
import com.epam.onboarding.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

@RestController
public class ProductController {

    static final String REVIEW_SERVICE = "review-service";
    private static final String NAME = "name";
    private static final String DESCRIPTION = "description";
    private static final String PRODUCTS = "products";

    @Value("${api-header-name}")
    private String headerName;

    @Value("${api-header-value}")
    private String headerValue;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private DiscoveryClient discoveryClient;

    @Autowired
    private IProductService productService;

    @RequestMapping("/service-instances/{application}")
    public ServiceInstance service(@PathVariable String application) {
        List<ServiceInstance> instances = discoveryClient.getInstances(application);
        if (instances == null || instances.isEmpty()) return null;

        return instances.get(0);
    }

    private String reviewService() {
        ServiceInstance instance = service(REVIEW_SERVICE);
        if (instance == null) return "";

        return instance.getUri().toString();
    }

    @GetMapping(value = "/products", produces = MediaType.APPLICATION_JSON_VALUE)
    public Iterable<Product> fetchAll() {
        Iterable<Product> products = productService.getAll();

        for (Product product : products) {
            product.add(linkTo(ProductController.class).withRel(PRODUCTS));
            product.add(linkTo(ProductController.class).slash(product.getProductId()).withSelfRel());
        }

        return products;
    }

    @GetMapping(value = "/products/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Product fetch(@PathVariable("id") Long productId) {
        Product product = productService.getById(productId);
        if (product != null) {
            product.add(linkTo(ProductController.class).withRel(PRODUCTS));
            product.add(linkTo(ProductController.class).slash(product.getProductId()).withSelfRel());
        }

        return product;
    }

    @PostMapping(value = "/products", produces = MediaType.APPLICATION_JSON_VALUE)
    public Product create(@RequestParam(NAME) String productName, @RequestParam(DESCRIPTION) String description) {
        Product product = productService.getByName(productName);
        if (product == null) {
            product = productService.save(new Product().setName(productName).setDescription(description));
        }
        product.add(linkTo(ProductController.class).withRel(PRODUCTS));
        product.add(linkTo(ProductController.class).slash(product.getProductId()).withSelfRel());

        return product;
    }

    @PutMapping(value = "/products/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
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

    @DeleteMapping(value = "/products/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Product delete(@PathVariable("id") Long productId) {
        return productService.removeById(productId);
    }

    @PostMapping(value = "/products/{id}/reviews", produces = MediaType.APPLICATION_JSON_VALUE)
    public Review createReview(@PathVariable("id") Long productId,
                               @RequestParam(DESCRIPTION) String description, @RequestParam("rating") int rating) {
        String url = String.format("%s/%d/reviews", reviewService(), productId);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.add(headerName, headerValue);

        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        parameters.add(DESCRIPTION, description);
        parameters.add("rating", String.valueOf(rating));

        Review review = restTemplate.postForObject(url, new HttpEntity<>(parameters, headers), Review.class);
        if (review != null) review.add(linkTo(ProductController.class).slash(review.getProductId()).withSelfRel());

        return review;
    }

    @PutMapping(value = "/products/{productId}/reviews/{reviewId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Review updateReview(@PathVariable("productId") Long productId, @PathVariable("reviewId") Long reviewId,
                               @RequestBody UpdateRequest updateRequest) {
        String url = String.format("%s/%d/reviews/%d", reviewService(), productId, reviewId);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add(headerName, headerValue);

        Review review = restTemplate.exchange(url, HttpMethod.PUT, new HttpEntity<>(updateRequest, headers), Review.class).getBody();
        if (review != null) review.add(linkTo(ProductController.class).slash(review.getProductId()).withSelfRel());

        return review;
    }

    @DeleteMapping(value = "/products/{productId}/reviews/{reviewId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Review deleteReview(@PathVariable("productId") Long productId, @PathVariable("reviewId") Long reviewId) {
        String url = String.format("%s/%d/reviews/%d", reviewService(), productId, reviewId);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.add(headerName, headerValue);

        Review review = restTemplate.exchange(url, HttpMethod.DELETE, new HttpEntity<>(headers), Review.class).getBody();
        if (review != null) review.add(linkTo(ProductController.class).slash(review.getProductId()).withSelfRel());

        return review;
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
