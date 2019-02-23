package com.epam.onboarding.service;

import com.epam.onboarding.domain.DomainObject;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public interface IBaseService<E extends DomainObject> {

    E save(@Nonnull E object);

    void remove(@Nonnull E object);

    E removeById(@Nonnull Long id);

    List<E> getAll();

    @Nullable
    E getById(@Nonnull Long id);
}
