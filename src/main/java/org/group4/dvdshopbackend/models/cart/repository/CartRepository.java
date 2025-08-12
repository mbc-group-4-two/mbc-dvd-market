package org.group4.dvdshopbackend.models.cart.repository;

import org.group4.dvdshopbackend.common.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

}
