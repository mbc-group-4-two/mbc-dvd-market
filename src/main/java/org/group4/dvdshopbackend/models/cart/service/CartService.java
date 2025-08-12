package org.group4.dvdshopbackend.models.cart.service;

import org.group4.dvdshopbackend.models.cart.dto.addItem.AddItemReq;
import org.group4.dvdshopbackend.models.cart.dto.addItem.AddItemRes;
import org.group4.dvdshopbackend.models.cart.dto.getCartList.GetCartListReq;
import org.group4.dvdshopbackend.models.cart.dto.getCartList.GetCartListRes;
import org.group4.dvdshopbackend.models.cart.dto.modifyCart.ModifyCartReq;
import org.group4.dvdshopbackend.models.cart.dto.modifyCart.ModifyCartRes;
import org.group4.dvdshopbackend.models.cart.dto.removeItem.RemoveItemReq;
import org.group4.dvdshopbackend.models.cart.dto.removeItem.RemoveItemRes;

public interface CartService {

	// 1. 장바구니에 아이템 등록
	AddItemRes addItem(AddItemReq req);

	// 2. 장바구니 아이템 목록 조회
	GetCartListRes getCartList();

	// 3. 장바구니 아이템 추가/빼기
	ModifyCartRes modifyCart(ModifyCartReq req);

	// 4. 장바구니 아이템 삭제
	RemoveItemRes removeItem(RemoveItemReq req);

	// 5. 장바구니에서 주문하기?
}
