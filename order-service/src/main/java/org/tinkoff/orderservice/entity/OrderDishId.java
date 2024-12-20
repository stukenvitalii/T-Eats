package org.tinkoff.orderservice.entity;

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
    private static final long serialVersionUID = 4775494873755727397L;
    @Column(name = "order_id", nullable = false)
    private Long orderId;

    @Column(name = "dish_id", nullable = false)
    private Long dishId;

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