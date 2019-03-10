package com.epam.onboarding.domain;

import org.springframework.hateoas.ResourceSupport;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "products")
public class Product extends ResourceSupport {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long productId;
    private String name;
    private String description;

    public Long getProductId() {
        return productId;
    }

    public Product setProductId(Long productId) {
        this.productId = productId;
        return this;
    }

    public String getName() {
        return name;
    }

    public Product setName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public Product setDescription(String description) {
        this.description = description;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Objects.equals(name, product.name) && Objects.equals(description, product.description) && Objects.equals(productId, product.productId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productId, name, description);
    }

    @Override
    public String toString() {
        return "Product{productId=" + productId +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
