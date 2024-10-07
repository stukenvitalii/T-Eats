package org.tinkoff.userservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@Embeddable
public class OrderDishId implements Serializable {
    private static final long serialVersionUID = -6537480582010913969L;
    @Column(name = "order_id", nullable = false)
    private Integer orderId;

    @Column(name = "dish_id", nullable = false)
    private Integer dishId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        OrderDishId entity = (OrderDishId) o;
        return Objects.equals(this.orderId, entity.orderId) &&
                Objects.equals(this.dishId, entity.dishId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderId, dishId);
    }

}