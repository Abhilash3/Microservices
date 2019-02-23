package com.epam.onboarding.service;

import com.epam.onboarding.domain.Product;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface IProductService extends IBaseService<Product> {

    @Nullable
    Product getByName(@Nonnull String name);
}
