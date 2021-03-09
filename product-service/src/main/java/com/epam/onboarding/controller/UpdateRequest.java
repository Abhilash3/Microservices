package com.epam.onboarding.controller;

import java.util.Objects;

class UpdateRequest {
    private String property;
    private String value;

    String getProperty() {
        return property;
    }

    UpdateRequest setProperty(String property) {
        this.property = property;
        return this;
    }

    String getValue() {
        return value;
    }

    UpdateRequest setValue(String value) {
        this.value = value;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UpdateRequest request = (UpdateRequest) o;
        return Objects.equals(property, request.property) &&
                Objects.equals(value, request.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(property, value);
    }
}
