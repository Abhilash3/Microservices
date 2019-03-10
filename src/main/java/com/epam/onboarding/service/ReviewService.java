package com.epam.onboarding.service;

import com.epam.onboarding.dao.ReviewDAO;
import com.epam.onboarding.domain.Review;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Component
public class ReviewService implements IReviewService {

    @Autowired
    private ReviewDAO reviewDAO;

    @Override
    public Review save(@Nonnull Review object) {
        reviewDAO.save(object);
        return object;
    }

    @Override
    public void remove(@Nonnull Review object) {
        reviewDAO.delete(object);
        object.setReviewId(null);
    }

    @Nullable
    @Override
    public Review removeById(@Nonnull Long id) {
        Review object = getById(id);
        if (object != null) {
            reviewDAO.delete(id);
        }
        return object;
    }

    @Override
    public Iterable<Review> getAll() {
        return reviewDAO.findAll();
    }

    @Override
    public Iterable<Review> getAllForProduct(@Nonnull Long productId) {
        return StreamSupport.stream(getAll().spliterator(), false)
                .filter(a -> productId.equals(a.getProductId()))
                .collect(Collectors.toList());
    }

    @Override
    public Review getById(@Nonnull Long id) {
        return reviewDAO.findOne(id);
    }
}
