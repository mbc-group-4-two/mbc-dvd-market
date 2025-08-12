package org.group4.dvdshopbackend.models.cart.service;

import lombok.RequiredArgsConstructor;
import org.group4.dvdshopbackend.models.cart.dto.addItem.AddItemReq;
import org.group4.dvdshopbackend.models.cart.dto.addItem.AddItemRes;
import org.group4.dvdshopbackend.models.cart.dto.getCartList.GetCartListRes;
import org.group4.dvdshopbackend.models.cart.dto.modifyCart.ModifyCartReq;
import org.group4.dvdshopbackend.models.cart.dto.modifyCart.ModifyCartRes;
import org.group4.dvdshopbackend.models.cart.dto.removeItem.RemoveItemReq;
import org.group4.dvdshopbackend.models.cart.dto.removeItem.RemoveItemRes;
import org.group4.dvdshopbackend.models.cart.repository.CartItemRepository;
import org.group4.dvdshopbackend.models.cart.repository.CartRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

	private final CartRepository cartRepository;
	private final CartItemRepository cartItemRepository;

	@Override
	public AddItemRes addItem(AddItemReq req) {
		return null;
	}

	@Override
	public GetCartListRes getCartList() {
		return null;
	}

	@Override
	public ModifyCartRes modifyCart(ModifyCartReq req) {
		return null;
	}

	@Override
	public RemoveItemRes removeItem(RemoveItemReq req) {
		return null;
	}
}
