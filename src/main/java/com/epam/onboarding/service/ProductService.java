package com.epam.onboarding.service;

import com.epam.onboarding.dao.ProductDAO;
import com.epam.onboarding.domain.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.Predicate;
import java.util.stream.StreamSupport;

@Component
public class ProductService implements IProductService {

    @Autowired
    private ProductDAO productDAO;

    @Override
    public Product save(@Nonnull Product object) {
        productDAO.save(object);
        return object;
    }

    @Override
    public void remove(@Nonnull Product object) {
        productDAO.delete(object);
        object.setProductId(null);
    }

    @Nullable
    @Override
    public Product removeById(@Nonnull Long id) {
        Product object = getById(id);
        if (object != null) {
            productDAO.delete(id);
        }
        return object;
    }

    @Override
    public Iterable<Product> getAll() {
        return productDAO.findAll();
    }

    @Override
    public Product getById(@Nonnull Long id) {
        return productDAO.findOne(id);
    }

    @Nullable
    private Product findFirst(@Nonnull Predicate<Product> matcher) {
        return StreamSupport.stream(getAll().spliterator(), true).filter(matcher).findFirst().orElse(null);
    }

    @Override
    public Product getByName(@Nonnull String name) {
        return findFirst(a -> name.equals(a.getName()));
    }
}
