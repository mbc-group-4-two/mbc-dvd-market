package org.group4.dvdshopbackend.models.cart.service;

import org.group4.dvdshopbackend.models.cart.dto.addItem.AddItemReq;
import org.group4.dvdshopbackend.models.cart.dto.addItem.AddItemRes;
import org.group4.dvdshopbackend.models.cart.dto.getCartList.GetCartListRes;
import org.group4.dvdshopbackend.models.cart.dto.modifyCart.ModifyCartReq;
import org.group4.dvdshopbackend.models.cart.dto.modifyCart.ModifyCartRes;
import org.group4.dvdshopbackend.models.cart.dto.removeAllItems.RemoveAllItemsRes;
import org.group4.dvdshopbackend.models.cart.dto.removeItem.RemoveItemRes;
import org.springframework.data.domain.Pageable;

public interface CartService {

	// 1. 장바구니에 아이템 등록
	AddItemRes addCartItem(Long memberId, AddItemReq req);

	// 2. 장바구니 아이템 목록 조회
	GetCartListRes getCartItems(Long memberId, Pageable pageable);

	// 3. 장바구니 아이템 추가/빼기 시 최신화
	ModifyCartRes modifyCartItem(Long memberId, Long itemId, ModifyCartReq req);

	// 4. 장바구니 아이템 삭제
	void removeCartItem(Long memberId, Long itemId);

	// 5. 장바구니에서 주문하기?

	// 장바구니 비우기
	void removeAllCartItems(Long memberId);
}
