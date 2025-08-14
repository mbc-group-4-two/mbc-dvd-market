package org.group4.dvdshopbackend.models.cart.controller;

import lombok.RequiredArgsConstructor;
import org.group4.dvdshopbackend.core.api.ApiResult;
import org.group4.dvdshopbackend.models.cart.dto.addItem.AddItemReq;
import org.group4.dvdshopbackend.models.cart.dto.addItem.AddItemRes;
import org.group4.dvdshopbackend.models.cart.dto.getCartList.GetCartListReq;
import org.group4.dvdshopbackend.models.cart.dto.getCartList.GetCartListRes;
import org.group4.dvdshopbackend.models.cart.dto.modifyCart.ModifyCartReq;
import org.group4.dvdshopbackend.models.cart.dto.modifyCart.ModifyCartRes;
import org.group4.dvdshopbackend.models.cart.dto.removeAllItems.RemoveAllItemsReq;
import org.group4.dvdshopbackend.models.cart.dto.removeAllItems.RemoveAllItemsRes;
import org.group4.dvdshopbackend.models.cart.dto.removeItem.RemoveItemReq;
import org.group4.dvdshopbackend.models.cart.dto.removeItem.RemoveItemRes;
import org.group4.dvdshopbackend.models.cart.service.CartService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

	private final CartService cartService;

	// 1. 장바구니에 아이템 등록
	@PostMapping("/carts")
	ApiResult<AddItemRes> addItem(@RequestBody AddItemReq req) {

		var res = cartService.addItem(req);

		return new ApiResult<>(res);
	}

	// 2. 장바구니 아이템 목록 조회
	@GetMapping("/carts")
	ApiResult<GetCartListRes> getCartList(@RequestBody GetCartListReq req) {

		var res = cartService.getCartList(req);

		return new ApiResult<>(res);
	}

	// 3. 장바구니 아이템 추가/빼기
	@PutMapping("/carts/items/{itemId}")
	ApiResult<ModifyCartRes> modifyItem(@PathVariable Long itemId, @RequestBody ModifyCartReq req) {

		var res = cartService.modifyCart(itemId, req);

		return new ApiResult<>(res);
	}

	// 4. 장바구니 아이템 삭제
	@DeleteMapping("/carts/items/{itemId}")
	ApiResult<RemoveItemRes> removeItem(@PathVariable Long itemId) {

		var res = cartService.removeItem(itemId);

		return new ApiResult<>(res);
	}

	// 5. 장바구니 아이템 비우기 (전체 삭제)
	@DeleteMapping("/carts/{cartId}")
	ApiResult<RemoveAllItemsRes> removeAllItems(@PathVariable Long cartId) {

		var res = cartService.removeAllItemsRes(cartId);

		return new ApiResult<>(res);
	}
}
