package com.epam.onboarding.dao;

import com.epam.onboarding.domain.Product;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Component;

@Component
public interface ProductDAO extends MongoRepository<Product, Long> {
}
