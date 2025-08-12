package org.group4.dvdshopbackend.models.cart.repository;

import org.group4.dvdshopbackend.common.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

	Optional<CartItem> findCartItemByCartIdAndItemId(Long cartId, Long itemId);
}
