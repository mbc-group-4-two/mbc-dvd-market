package org.group4.dvdshopbackend.models.cart.service;

import lombok.RequiredArgsConstructor;
import org.group4.dvdshopbackend.common.entity.Cart;
import org.group4.dvdshopbackend.common.entity.CartItem;
import org.group4.dvdshopbackend.models.cart.dto.addItem.AddItemReq;
import org.group4.dvdshopbackend.models.cart.dto.addItem.AddItemRes;
import org.group4.dvdshopbackend.models.cart.dto.getCartList.GetCartListRes;
import org.group4.dvdshopbackend.models.cart.dto.modifyCart.ModifyCartReq;
import org.group4.dvdshopbackend.models.cart.dto.modifyCart.ModifyCartRes;
import org.group4.dvdshopbackend.models.cart.dto.removeItem.RemoveItemReq;
import org.group4.dvdshopbackend.models.cart.dto.removeItem.RemoveItemRes;
import org.group4.dvdshopbackend.models.cart.repository.CartItemRepository;
import org.group4.dvdshopbackend.models.cart.repository.CartRepository;
import org.group4.dvdshopbackend.models.member.repository.MemberJpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

	private final CartRepository cartRepository;
	private final CartItemRepository cartItemRepository;

	private final MemberJpaRepository memberJpaRepository;

	@Override
	@Transactional
	public AddItemRes addItem(AddItemReq req) {
		var memberEmail = req.getMemberEmail();
		var itemId = req.getItemId();

		// 1. 멤버 조회
		var member = memberJpaRepository.findByEmailAndDeletedYn(memberEmail, "N")
				.orElseThrow();

		// 2. 카트 조회 후 없으면 생성
		var cart = cartRepository.findByMemberId(member.getId());

		if (cart == null) {
			var newCart = new Cart();
			newCart.setMember(member);

			cart = cartRepository.save(newCart);
		}

		// 3. cart item 추가
		// cart ID + item ID로 cart 조회

		CartItem cartItem = null;
		var cartItemResult = cartItemRepository.findCartItemByCartIdAndItemId(cart.getId(), itemId);

		if (cartItemResult.isEmpty()) {
			// 추가
			cartItem = new CartItem();
			cartItem.setCart(cart);
			cartItem.setCount(req.getItemCount());
			cartItemRepository.save(cartItem);
		} else {
			// 수정
			cartItem = cartItemResult.orElseThrow();
			cartItem.setCount(cartItem.getCount() + 1);
		}

		return AddItemRes.builder()
				.cartItemId(cartItem.getId())
				.cartId(cart.getId())
				.itemId(itemId)
				.itemCount(cartItem.getCount())
				.build();
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
