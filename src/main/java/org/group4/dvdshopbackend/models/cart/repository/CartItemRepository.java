package org.group4.dvdshopbackend.models.cart.repository;

import org.group4.dvdshopbackend.common.entity.CartItem;
import org.group4.dvdshopbackend.common.entity.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

	Optional<CartItem> findCartItemByCartIdAndItemId(Long cartId, Long itemId);

	List<CartItem> findAllByCartId(Long cartId);

	void deleteAllByCartId(Long cartId);

	void deleteByCartIdAndItemId(Long cartId, Long itemId);
}
