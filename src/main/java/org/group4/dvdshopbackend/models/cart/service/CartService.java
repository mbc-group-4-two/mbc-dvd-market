package org.group4.dvdshopbackend.models.cart.service;

import org.group4.dvdshopbackend.models.cart.dto.addItem.AddItemReq;
import org.group4.dvdshopbackend.models.cart.dto.addItem.AddItemRes;
import org.group4.dvdshopbackend.models.cart.dto.getCartList.GetCartListReq;
import org.group4.dvdshopbackend.models.cart.dto.getCartList.GetCartListRes;
import org.group4.dvdshopbackend.models.cart.dto.modifyCart.ModifyCartReq;
import org.group4.dvdshopbackend.models.cart.dto.modifyCart.ModifyCartRes;
import org.group4.dvdshopbackend.models.cart.dto.removeAllItems.RemoveAllItemsRes;
import org.group4.dvdshopbackend.models.cart.dto.removeItem.RemoveItemReq;
import org.group4.dvdshopbackend.models.cart.dto.removeItem.RemoveItemRes;

public interface CartService {

	// 1. 장바구니에 아이템 등록
	AddItemRes addItem(AddItemReq req);

	// 2. 장바구니 아이템 목록 조회
	GetCartListRes getCartList(GetCartListReq req);

	// 3. 장바구니 아이템 추가/빼기 시 최신화
	ModifyCartRes modifyCart(Long itemId, ModifyCartReq req);

	// 4. 장바구니 아이템 삭제
	RemoveItemRes removeItem(Long itemId);

	// 5. 장바구니에서 주문하기?

	// 장바구니 비우기
	RemoveAllItemsRes removeAllItemsRes(Long cartId);
}
