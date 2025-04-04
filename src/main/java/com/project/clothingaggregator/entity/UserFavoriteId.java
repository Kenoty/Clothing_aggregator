package com.project.clothingaggregator.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;

@Setter
@Getter
@Embeddable
public class UserFavoriteId implements java.io.Serializable {
    private static final long serialVersionUID = 138063364808096430L;
    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @Column(name = "product_id", nullable = false)
    private Integer productId;

    public UserFavoriteId() {
    }

    public UserFavoriteId(Integer userId, Integer productId) {
        this.userId = userId;
        this.productId = productId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
            return false;
        }
        UserFavoriteId entity = (UserFavoriteId) o;
        return Objects.equals(this.productId, entity.productId)
                && Objects.equals(this.userId, entity.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productId, userId);
    }

}