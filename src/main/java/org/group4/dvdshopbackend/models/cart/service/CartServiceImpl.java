package org.group4.dvdshopbackend.models.cart.service;

import lombok.RequiredArgsConstructor;
import org.group4.dvdshopbackend.common.entity.Cart;
import org.group4.dvdshopbackend.common.entity.CartItem;
import org.group4.dvdshopbackend.models.cart.dto.addItem.AddItemReq;
import org.group4.dvdshopbackend.models.cart.dto.addItem.AddItemRes;
import org.group4.dvdshopbackend.models.cart.dto.getCartList.GetCartListReq;
import org.group4.dvdshopbackend.models.cart.dto.getCartList.GetCartListRes;
import org.group4.dvdshopbackend.models.cart.dto.getCartList.GetCartListResItemDetail;
import org.group4.dvdshopbackend.models.cart.dto.modifyCart.ModifyCartReq;
import org.group4.dvdshopbackend.models.cart.dto.modifyCart.ModifyCartRes;
import org.group4.dvdshopbackend.models.cart.dto.removeAllItems.RemoveAllItemsRes;
import org.group4.dvdshopbackend.models.cart.dto.removeItem.RemoveItemReq;
import org.group4.dvdshopbackend.models.cart.dto.removeItem.RemoveItemRes;
import org.group4.dvdshopbackend.models.cart.repository.CartItemRepository;
import org.group4.dvdshopbackend.models.cart.repository.CartRepository;
import org.group4.dvdshopbackend.models.item.dto.repository.ItemImgRepository;
import org.group4.dvdshopbackend.models.item.dto.repository.ItemRepository;
import org.group4.dvdshopbackend.models.member.repository.MemberJpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

	private final CartRepository cartRepository;
	private final CartItemRepository cartItemRepository;

	private final MemberJpaRepository memberJpaRepository;
	private final ItemRepository itemRepository;
	private final ItemImgRepository itemImgRepository;

	@Override
	@Transactional
	public AddItemRes addItem(AddItemReq req) {
		var memberEmail = req.getMemberEmail();
		var itemId = req.getItemId();

		// 1. 멤버 조회
		var member = memberJpaRepository.findByEmailAndDeletedYn(memberEmail, "N")
				.orElseThrow();

		// 2. item 조회
		var item = itemRepository.findById(itemId)
				.orElseThrow();



		// 3. 카트 조회 후 없으면 생성
		var cart = cartRepository.findByMemberId(member.getId());

		if (cart == null) {
			var newCart = new Cart();
			newCart.setMember(member);

			cart = cartRepository.save(newCart);
		}

		// 3. cart item 추가
		// cart ID + item ID로 cart 조회

		CartItem cartItem = null;
		var cartItemResult = cartItemRepository.findCartItemByCartIdAndItemId(cart.getId(), item.getId());

		if (cartItemResult.isEmpty()) {
			// 추가
			cartItem = new CartItem();
			cartItem.setCart(cart);
			cartItem.setItem(item);
			cartItem.setCount(req.getItemCount());
			cartItemRepository.save(cartItem);
		} else {
			// 수정
			cartItem = cartItemResult.orElseThrow();
      
			cartItem.setCount(cartItem.getCount() + req.getItemCount());
		}

		return AddItemRes.builder()
				.cartItemId(cartItem.getId())
				.cartId(cart.getId())
				.itemId(itemId)
				.itemCount(cartItem.getCount())
				.build();
	}

	@Override
	@Transactional
	public GetCartListRes getCartList(GetCartListReq req) {
		String memberEmail = req.getMemberEmail();

		// 1. 멤버 조회
		var member = memberJpaRepository.findByEmailAndDeletedYn(memberEmail, "N")
				.orElseThrow();

		// 2. 카트 조회
		var cart = cartRepository.findByMemberId(member.getId());

		if (cart == null) {
			var newCart = new Cart();
			newCart.setMember(member);

			cart = cartRepository.save(newCart);
		}

		// 3. 카트 아이템 목록 조회


		var itemDetails = new ArrayList<GetCartListResItemDetail>();
		Long cartId = cart.getId(); // api 호출 멤버의 cart id
		var cartItemResult = cartItemRepository.findAllByCartId(cartId);

		// 카트에 아이템 없는 경우
		if (cartItemResult.isEmpty()) {
			return GetCartListRes.builder()
					.itemDetails(null)
					.build();

		} else {
			// 카트에 아이템 있는 경우
			cartItemResult.forEach(cartItem -> {
				var cartItemId = cartItem.getId();

				var item = itemRepository.findById(cartItemId)
						.orElseThrow();

				var itemImgResult = itemImgRepository.findByItemIdAndRepimgYn(item.getId(), "Y");

				String itemImgUrl = "";

				if (itemImgResult != null) {
					itemImgUrl = itemImgResult.getImgUrl();
				}

				var itemDetail = GetCartListResItemDetail.builder()
						.cartItemId(cartItemId)
						.itemName(item.getItemNm())
						.price(item.getPrice())
						.count(cartItem.getCount())
						.imgUrl(itemImgUrl)
						.build();
				itemDetails.add(itemDetail);
			});
		}

		return GetCartListRes.builder()
				.itemDetails(itemDetails)
				.build();
	}

	@Override
	@Transactional
	public ModifyCartRes modifyCart(Long itemId, ModifyCartReq req) {

		var cartItem = cartItemRepository.findById(itemId)
				.orElseThrow();

		cartItem.setCount(req.getItemCount());

		return ModifyCartRes.builder()
				.itemId(cartItem.getId())
				.itemCount(cartItem.getCount())
				.build();
	}

	@Override
	public RemoveItemRes removeItem(Long itemId) {
		cartItemRepository.deleteById(itemId);

		return RemoveItemRes.builder()
				.deletedItemId(itemId)
				.build();
	}

	@Override
	@Transactional
	public RemoveAllItemsRes removeAllItemsRes(Long cartId) {

		cartItemRepository.deleteAllByCartId(cartId);

		return RemoveAllItemsRes.builder()
				.deletedCartId(cartId)
				.build();
	}

}
