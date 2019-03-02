package com.epam.onboarding.service;

import com.epam.onboarding.domain.Product;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface IProductService {

    Product save(@Nonnull Product object);

    void remove(@Nonnull Product object);

    Product removeById(@Nonnull Long id);

    Iterable<Product> getAll();

    @Nullable
    Product getById(@Nonnull Long id);

    @Nullable
    Product getByName(@Nonnull String name);
}
