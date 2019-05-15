package com.epam.onboarding.controller;

import com.epam.onboarding.domain.Product;
import com.epam.onboarding.domain.Review;
import com.epam.onboarding.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

@RestController
@RequestMapping("/products")
public class ProductController {

    private static final String NAME = "name";
    private static final String DESCRIPTION = "description";
    private static final String PRODUCTS = "products";
    private static final String PRODUCT = "product";

    @Autowired
    private ReviewController reviewController;

    @Autowired
    private IProductService productService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Iterable<Product> fetchAll() {
        Iterable<Product> products = productService.getAll();

        for (Product product : products) {
            product.add(linkTo(ProductController.class).withRel(PRODUCTS));
            product.add(linkTo(ProductController.class).slash(product.getProductId()).withSelfRel());
        }

        return products;
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Product fetch(@PathVariable("id") Long productId) {
        Product product = productService.getById(productId);
        if (product != null) {
            product.add(linkTo(ProductController.class).withRel(PRODUCTS));
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

        product.add(linkTo(ProductController.class).withRel(PRODUCTS));
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

        product.add(linkTo(ProductController.class).withRel(PRODUCTS));
        product.add(linkTo(ProductController.class).slash(product.getProductId()).withSelfRel());
        return product;
    }

    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Product delete(@PathVariable("id") Long productId) {
        return productService.removeById(productId);
    }

    @GetMapping(value = "/{id}/reviews", produces = MediaType.APPLICATION_JSON_VALUE)
    public Iterable<Review> getReviews(@PathVariable("id") Long productId) {
        Iterable<Review> reviews = reviewController.fetchAll(productId);
        reviews.forEach(review -> review.add(linkTo(ProductController.class).slash(review.getProductId()).withRel(PRODUCT)));

        return reviews;
    }

    @PostMapping(value = "/{id}/reviews", produces = MediaType.APPLICATION_JSON_VALUE)
    public Review createReview(@PathVariable("id") Long productId, @RequestParam(DESCRIPTION) String description,
                               @RequestParam("rating") int rating) {
        Review review = reviewController.create(productId, description, rating);
        if (review != null) review.add(linkTo(ProductController.class).slash(review.getProductId()).withRel(PRODUCT));

        return review;
    }

    @PutMapping(value = "/{productId}/reviews/{reviewId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Review updateReview(@PathVariable("productId") Long productId, @PathVariable("reviewId") Long reviewId,
                               @RequestBody UpdateRequest updateRequest) {
        Review review = reviewController.update(productId, reviewId, updateRequest);
        if (review != null) review.add(linkTo(ProductController.class).slash(review.getProductId()).withRel(PRODUCT));

        return review;
    }

    @DeleteMapping(value = "/{productId}/reviews/{reviewId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Review deleteReview(@PathVariable("productId") Long productId, @PathVariable("reviewId") Long reviewId) {
        Review review = reviewController.delete(productId, reviewId);
        if (review != null) review.add(linkTo(ProductController.class).slash(review.getProductId()).withRel(PRODUCT));

        return review;
    }
}
