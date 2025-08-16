package org.group4.dvdshopbackend.models.order.repository;

import org.group4.dvdshopbackend.common.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

	List<Order> findAllByMemberId(Long memberId);

	Page<Order> findAllByMemberId(Long memberId, Pageable pageable);
}
