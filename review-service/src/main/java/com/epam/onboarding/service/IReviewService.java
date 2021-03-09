package com.epam.onboarding.service;

import com.epam.onboarding.domain.Review;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface IReviewService {

    Review save(@Nonnull Review object);

    void remove(@Nonnull Review object);

    Review removeById(@Nonnull Long id);

    Iterable<Review> getAll();

    Iterable<Review> getAllForProduct(@Nonnull Long productId);

    @Nullable
    Review getById(@Nonnull Long id);
}
