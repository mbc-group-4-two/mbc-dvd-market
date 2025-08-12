package org.group4.dvdshopbackend.models.order.repository;

import org.group4.dvdshopbackend.common.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
}
