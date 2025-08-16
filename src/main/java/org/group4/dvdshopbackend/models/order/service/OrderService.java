package org.group4.dvdshopbackend.models.order.service;

import org.group4.dvdshopbackend.models.cart.dto.addItem.AddItemReq;
import org.group4.dvdshopbackend.models.cart.dto.addItem.AddItemRes;
import org.group4.dvdshopbackend.models.cart.dto.getCartList.GetCartListRes;
import org.group4.dvdshopbackend.models.cart.dto.modifyCart.ModifyCartReq;
import org.group4.dvdshopbackend.models.cart.dto.modifyCart.ModifyCartRes;
import org.group4.dvdshopbackend.models.cart.dto.removeItem.RemoveItemReq;
import org.group4.dvdshopbackend.models.cart.dto.removeItem.RemoveItemRes;
import org.group4.dvdshopbackend.models.order.dto.cancelOrder.CancelOrderReq;
import org.group4.dvdshopbackend.models.order.dto.cancelOrder.CancelOrderRes;
import org.group4.dvdshopbackend.models.order.dto.getOrderList.GetOrderListReq;
import org.group4.dvdshopbackend.models.order.dto.getOrderList.GetOrderListRes;
import org.group4.dvdshopbackend.models.order.dto.sendOrder.SendOrderReq;
import org.group4.dvdshopbackend.models.order.dto.sendOrder.SendOrderRes;
import org.springframework.data.domain.Pageable;

public interface OrderService {

	// 1. 주문 추가
	SendOrderRes sendOrder(Long userId, SendOrderReq req);

	// 2. 주문 이력 목록 조회
	GetOrderListRes getOrderList(Long userId, Pageable pageable);

	// 3. 주문 취소
	CancelOrderRes cancelOrder(Long userId, Long orderId);
}
