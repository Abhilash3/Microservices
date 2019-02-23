package com.epam.onboarding.service;

import com.epam.onboarding.dao.ProductDAO;
import com.epam.onboarding.domain.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;

@Component
public class ProductService extends BaseService<Product> implements IProductService {

    @Autowired
    private ProductDAO productDAO;

    @Override
    public Product getByName(@Nonnull String name) {
        return findFirst(a -> name.equals(a.getName()));
    }

    @Override
    MongoRepository<Product, Long> daoStore() {
        return productDAO;
    }
}
