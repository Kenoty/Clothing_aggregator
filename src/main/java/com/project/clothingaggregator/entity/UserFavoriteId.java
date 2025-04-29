package com.project.clothingaggregator.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serial;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;

@Setter
@Getter
@Embeddable
public class UserFavoriteId implements java.io.Serializable {
    @Serial
    private static final long serialVersionUID = 138063364808096430L;
    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @Column(name = "item_id", nullable = false)
    private String itemId;

    public UserFavoriteId() {
    }

    public UserFavoriteId(Integer userId, String itemId) {
        this.userId = userId;
        this.itemId = itemId;
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
        return Objects.equals(this.itemId, entity.itemId)
                && Objects.equals(this.userId, entity.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(itemId, userId);
    }

}