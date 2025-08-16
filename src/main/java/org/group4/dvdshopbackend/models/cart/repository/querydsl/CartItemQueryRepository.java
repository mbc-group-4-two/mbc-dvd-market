package org.group4.dvdshopbackend.models.cart.repository.querydsl;

import org.group4.dvdshopbackend.models.cart.dto.getCartList.GetCartListResItemDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CartItemQueryRepository {
	Page<GetCartListResItemDetail> getPageCartItem(Long cartId, Pageable pageable);
}
