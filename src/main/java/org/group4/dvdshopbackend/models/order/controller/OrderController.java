package org.group4.dvdshopbackend.models.order.controller;

import lombok.RequiredArgsConstructor;
import org.group4.dvdshopbackend.core.api.ApiResponse;
import org.group4.dvdshopbackend.core.api.ApiResult;
import org.group4.dvdshopbackend.models.cart.dto.addItem.AddItemRes;
import org.group4.dvdshopbackend.models.order.dto.cancelOrder.CancelOrderRes;
import org.group4.dvdshopbackend.models.order.dto.getOrderList.GetOrderListReq;
import org.group4.dvdshopbackend.models.order.dto.getOrderList.GetOrderListRes;
import org.group4.dvdshopbackend.models.order.dto.sendOrder.SendOrderReq;
import org.group4.dvdshopbackend.models.order.dto.sendOrder.SendOrderRes;
import org.group4.dvdshopbackend.models.order.service.OrderService;
import org.group4.dvdshopbackend.security.auth.record.LoginUser;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

	private final OrderService orderService;

	// 1. 주문 추가
	@PostMapping()
	ResponseEntity<?> sendOrder(@RequestBody SendOrderReq req,
	                            @AuthenticationPrincipal LoginUser loginUser) {
		var res = orderService.sendOrder(loginUser.id(), req);
		return ApiResponse.created(res);
	}

	// 2. 주문 이력 목록 조회
	@GetMapping()
	ResponseEntity<?> getOrderList(
			@RequestParam(name = "page", defaultValue = "1", required = false) Integer page,
			@RequestParam(name = "size", defaultValue = "10", required = false) Integer size,
			@AuthenticationPrincipal LoginUser loginUser) {
		var pageable = PageRequest.of(page - 1, size, Sort.by("orderDate").descending());

		var res = orderService.getOrderList(loginUser.id(), pageable);
		return ApiResponse.ok(res);
	}

	// 3. 주문 취소
	@PutMapping("/{orderId}")
	ResponseEntity<?> cancelOrder(@PathVariable Long orderId,
	                              @AuthenticationPrincipal LoginUser loginUser) {
		var res = orderService.cancelOrder(loginUser.id(), orderId);
		return ApiResponse.ok(res);
	}
}
