package org.group4.dvdshopbackend.models.order.repository;

import org.group4.dvdshopbackend.common.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

}
