package com.epam.onboarding.dao;

import com.epam.onboarding.domain.Review;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

@Component
public interface ReviewDAO extends CrudRepository<Review, Long> {
}
