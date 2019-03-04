package com.epam.onboarding.dao;

import com.epam.onboarding.domain.Product;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

@Component
public interface ProductDAO extends CrudRepository<Product, Long> {
}
