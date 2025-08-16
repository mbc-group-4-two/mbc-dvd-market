package org.group4.dvdshopbackend.models.cart.repository;

import org.group4.dvdshopbackend.common.entity.Cart;
import org.group4.dvdshopbackend.common.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {

	Cart findByMemberId(Long memberId);
}
