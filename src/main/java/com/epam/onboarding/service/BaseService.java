package com.epam.onboarding.service;

import com.epam.onboarding.common.Utils;
import com.epam.onboarding.domain.DomainObject;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.function.Predicate;

public abstract class BaseService<E extends DomainObject> implements IBaseService<E> {

    @Override
    public E save(@Nonnull E object) {
        if (object.getId() == null) {
            object.setId(Utils.randomLong());
        } else {
            daoStore().delete(object.getId());
        }
        daoStore().save(object);
        return object;
    }

    @Override
    public void remove(@Nonnull E object) {
        daoStore().delete(object);
        object.setId(null);
    }

    @Nullable
    @Override
    public E removeById(@Nonnull Long id) {
        E object = findFirst(a -> id.equals(a.getId()));
        if (object != null) {
            daoStore().delete(id);
        }
        return object;
    }

    @Override
    public List<E> getAll() {
        return daoStore().findAll();
}

    @Override
    public E getById(@Nonnull Long id) {
        return findFirst(a -> id.equals(a.getId()));
    }

    @Nullable
    E findFirst(@Nonnull Predicate<E> matcher) {
        return getAll().stream().filter(matcher).findFirst().orElse(null);
    }

    abstract MongoRepository<E, Long> daoStore();
}
