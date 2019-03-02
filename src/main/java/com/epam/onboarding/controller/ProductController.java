package com.epam.onboarding.controller;

import com.epam.onboarding.domain.Product;
import com.epam.onboarding.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
public class ProductController {

    @Autowired
    private IProductService productService;

    @GetMapping(value = "/products", produces = MediaType.APPLICATION_JSON_VALUE)
    public Iterable<Product> fetchAll() {
        return productService.getAll();
    }

    @GetMapping(value = "/products/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Product fetch(@PathVariable("id") Long productId) {
        return productService.getById(productId);
    }

    @PostMapping(value = "/products", produces = MediaType.APPLICATION_JSON_VALUE)
    public Product create(@RequestParam("productName") String productName) {
        Product product = productService.getByName(productName);
        if (product != null) return product;

        return productService.save(new Product().setName(productName));
    }

    @PutMapping(value = "/products/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Product update(@PathVariable("id") Long productId, @RequestParam("productName") String productName) {
        Product product = productService.getById(productId);
        if (product == null) return null;

        product.setName(productName);
        productService.save(product);

        return product;
    }

    @DeleteMapping(value = "/products/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Product delete(@PathVariable("id") Long productId) {
        return productService.removeById(productId);
    }
}
