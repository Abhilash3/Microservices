package com.epam.onboarding;

import com.epam.onboarding.domain.Product;
import com.epam.onboarding.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Order(1)
@Profile("!test")
public class ApplicationInit implements CommandLineRunner {

    @Autowired
    private ProductService productService;

    @Override
    public void run(String... args) {
        productService.getAll().forEach(a -> productService.removeById(a.getProductId()));
        Arrays.asList("Car", "Bike", "Fridge", "Washing Machine", "Mobile", "Laptop")
                .forEach(a -> productService.save(new Product().setName(a).setDescription(a)));
    }
}
