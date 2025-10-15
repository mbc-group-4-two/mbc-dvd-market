package org.group4.dvdshopbackend.models.cart.controller;

import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.group4.dvdshopbackend.common.enums.Role;
import org.group4.dvdshopbackend.core.api.ApiResponse;
import org.group4.dvdshopbackend.core.api.ApiResult;
import org.group4.dvdshopbackend.models.cart.dto.addItem.AddItemReq;
import org.group4.dvdshopbackend.models.cart.dto.addItem.AddItemRes;
import org.group4.dvdshopbackend.models.cart.dto.getCartList.GetCartListRes;
import org.group4.dvdshopbackend.models.cart.dto.modifyCart.ModifyCartReq;
import org.group4.dvdshopbackend.models.cart.dto.modifyCart.ModifyCartRes;
import org.group4.dvdshopbackend.models.cart.service.CartService;
import org.group4.dvdshopbackend.security.auth.record.LoginUser;
import org.group4.dvdshopbackend.security.jwt.JwtAuthFilter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

	private static final Logger log = LogManager.getLogger(CartController.class);
	private final CartService cartService;

	// 1. 장바구니에 아이템 등록
	@PostMapping("/items")
	ResponseEntity<?> addCartItem(@RequestBody AddItemReq req,
	                              @AuthenticationPrincipal LoginUser loginUser) {
		var res = cartService.addCartItem(loginUser.id(), req);

		return ApiResponse.created(res);
	}

	// 2. 장바구니 아이템 목록 조회
	@GetMapping("/items")
	ResponseEntity<?> getCartItems(
			@RequestParam(name = "page", defaultValue = "1", required = false) Integer page,
			@RequestParam(name = "size", defaultValue = "10", required = false) Integer size,
			@AuthenticationPrincipal LoginUser loginUser) {

		log.info("user id : " + loginUser.id());
		log.info("user role : " + loginUser.role());

		var pageable = PageRequest.of(page - 1, size, Sort.by("updateTime").descending());

		var res = cartService.getCartItems(loginUser.id(), pageable);

		return ApiResponse.ok(res);
	}


	// 3. 장바구니 아이템 추가/빼기
	@PutMapping("/items/{cartItemId}")
	ResponseEntity<?> modifyCartItem(@PathVariable Long cartItemId,
	                                 @RequestBody ModifyCartReq req,
	                                 @AuthenticationPrincipal LoginUser loginUser) {

		var res = cartService.modifyCartItem(loginUser.id(), cartItemId, req);

		return ApiResponse.ok(res);
	}

	// 4. 장바구니 아이템 삭제
	@DeleteMapping("/items/{cartItemId}")
	ResponseEntity<?> removeCartItem(@PathVariable Long cartItemId,
	                                 @AuthenticationPrincipal LoginUser loginUser) {

		cartService.removeCartItem(loginUser.id(), cartItemId);

		return ApiResponse.noContent();
	}

	// 5. 장바구니 아이템 비우기 (전체 삭제)
	@DeleteMapping("/items")
	ResponseEntity<?> removeAllCartItems(
			@AuthenticationPrincipal LoginUser loginUser) {

		cartService.removeAllCartItems(loginUser.id());

		return ApiResponse.noContent();
	}
}
