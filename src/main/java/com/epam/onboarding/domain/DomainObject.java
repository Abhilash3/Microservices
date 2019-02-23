package com.epam.onboarding.domain;

import org.springframework.data.annotation.Id;

public abstract class DomainObject {

    @Id
    Long id;

    public Long getId() {
        return id;
    }

    public DomainObject setId(Long id) {
        this.id = id;
        return this;
    }
}
