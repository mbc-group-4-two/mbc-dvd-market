package org.group4.dvdshopbackend.models.cart.service;

import lombok.RequiredArgsConstructor;
import org.group4.dvdshopbackend.common.entity.Cart;
import org.group4.dvdshopbackend.common.entity.CartItem;
import org.group4.dvdshopbackend.core.api.PagedRes;
import org.group4.dvdshopbackend.models.cart.dto.addItem.AddItemReq;
import org.group4.dvdshopbackend.models.cart.dto.addItem.AddItemRes;
import org.group4.dvdshopbackend.models.cart.dto.getCartList.GetCartListRes;
import org.group4.dvdshopbackend.models.cart.dto.getCartList.GetCartListResItemDetail;
import org.group4.dvdshopbackend.models.cart.dto.modifyCart.ModifyCartReq;
import org.group4.dvdshopbackend.models.cart.dto.modifyCart.ModifyCartRes;
import org.group4.dvdshopbackend.models.cart.repository.CartItemRepository;
import org.group4.dvdshopbackend.models.cart.repository.CartRepository;
import org.group4.dvdshopbackend.models.cart.repository.querydsl.CartItemQueryRepository;
import org.group4.dvdshopbackend.models.item.dto.repository.ItemImgRepository;
import org.group4.dvdshopbackend.models.item.dto.repository.ItemRepository;
import org.group4.dvdshopbackend.models.member.repository.MemberJpaRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

	private final MemberJpaRepository memberJpaRepository;

	private final CartRepository cartRepository;
	private final CartItemRepository cartItemRepository;
	private final CartItemQueryRepository cartItemQueryRepository; // Query DSL

	private final ItemRepository itemRepository;

	/**
	 * 카트 조회 + 없는경우 생성
	 * @param memberId
	 * @return Cart
	 */
	private Cart findCart(Long memberId) {
		var cart = cartRepository.findByMemberId(memberId);

		if (cart == null) {
			var member = memberJpaRepository.findById(memberId)
					.orElseThrow();
			var newCart = new Cart();
			newCart.setMember(member);
			cart = cartRepository.save(newCart);
		}

		return cart;
	}

	/**
	 * 카트의 장바구니아이템 조회
	 * @param cartId
	 * @param itemId
	 * @return null or CartItem
	 */
	private CartItem findCartItem(Long cartId, Long itemId) {
		var cartItemResult = cartItemRepository.findCartItemByCartIdAndItemId(cartId, itemId);
		return cartItemResult.isEmpty() ? null : cartItemResult.orElseThrow();
	}

	@Override
	@Transactional
	public AddItemRes addCartItem(Long memberId, AddItemReq req) {
		var cart = findCart(memberId);
		var cartItem = findCartItem(cart.getId(), req.getItemId());

		if (cartItem == null) { // 새 아이템 추가
			var item = itemRepository.findById(req.getItemId())
					.orElseThrow();

			cartItem = new CartItem();
			cartItem.setCart(cart);
			cartItem.setItem(item);
			cartItem.setCount(req.getItemCount());
			cartItemRepository.save(cartItem);

		} else { // 기존 아이템 수정
			cartItem.setCount(cartItem.getCount() + req.getItemCount());
		}

		return AddItemRes.builder()
				.cartItemId(cartItem.getId())
				.build();
	}

	@Override
	@Transactional
	public GetCartListRes getCartItems(Long memberId, Pageable pageable) {
		var cart = findCart(memberId);
		var cartItemPage = cartItemQueryRepository.getPageCartItem(cart.getId(), pageable);

		return GetCartListRes.builder()
				.cartPage(new PagedRes<>(cartItemPage))
				.build();
	}

	@Override
	@Transactional
	public ModifyCartRes modifyCartItem(Long memberId, Long cartItemId, ModifyCartReq req) {
		var cartItem = cartItemRepository.findById(cartItemId)
				.orElseThrow();

		cartItem.setCount(req.getItemCount());

		return ModifyCartRes.builder()
				.itemId(cartItem.getId())
				.itemCount(cartItem.getCount())
				.build();
	}

	@Override
	public void removeCartItem(Long memberId, Long cartItemId) {
		cartItemRepository.deleteById(cartItemId);
	}

	@Override
	@Transactional
	public void removeAllCartItems(Long memberId) {
		var cart = findCart(memberId);
		cartItemRepository.deleteAllByCartId(cart.getId());
	}
}
