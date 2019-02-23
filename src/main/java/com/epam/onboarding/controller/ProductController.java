package com.epam.onboarding.controller;

import com.epam.onboarding.domain.Product;
import com.epam.onboarding.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private IProductService productService;

    @GetMapping(value = "/fetch/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Product fetch(@PathVariable("id") Long productId) {
        return productService.getById(productId);
    }

    @GetMapping(value = "/fetch", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Product> fetch() {
        return productService.getAll();
    }

    @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
    public Product create(@RequestParam("productName") String productName) {
        String name = productName.trim();
        Product product = productService.getByName(name);
        if (product != null) return product;

        return productService.save(new Product().setName(name));
    }

    @PostMapping(value = "/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Product delete(@PathVariable("id") Long productId) {
        return productService.removeById(productId);
    }
}
