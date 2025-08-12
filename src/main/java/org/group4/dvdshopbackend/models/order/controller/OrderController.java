package org.group4.dvdshopbackend.models.order.controller;

import lombok.RequiredArgsConstructor;
import org.group4.dvdshopbackend.core.api.ApiResult;
import org.group4.dvdshopbackend.models.order.dto.cancelOrder.CancelOrderReq;
import org.group4.dvdshopbackend.models.order.dto.cancelOrder.CancelOrderRes;
import org.group4.dvdshopbackend.models.order.dto.getOrderList.GetOrderListReq;
import org.group4.dvdshopbackend.models.order.dto.getOrderList.GetOrderListRes;
import org.group4.dvdshopbackend.models.order.dto.sendOrder.SendOrderReq;
import org.group4.dvdshopbackend.models.order.dto.sendOrder.SendOrderRes;
import org.group4.dvdshopbackend.models.order.service.OrderService;
import org.springframework.web.bind.annotation.*;

@RestController("/api/order")
@RequiredArgsConstructor
public class OrderController {

	private final OrderService orderService;

	// 1. 주문 추가
	@PostMapping("/orders")
	public ApiResult<SendOrderRes> sendOrder(@RequestBody SendOrderReq req) {
		return new ApiResult<>(null);
	}

	// 2. 주문 이력 목록 조회
	@GetMapping("/orders")
	public ApiResult<GetOrderListRes> getOrderList(@RequestBody GetOrderListReq req) {
		return new ApiResult<>(null);
	}

	// 3. 주문 취소
	@DeleteMapping("/orders")
	public ApiResult<CancelOrderRes> cancelOrder(@RequestBody CancelOrderReq req) {
		return new ApiResult<>(null);
	}
}
