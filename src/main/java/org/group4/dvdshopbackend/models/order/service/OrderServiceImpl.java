package org.group4.dvdshopbackend.models.order.service;

import lombok.RequiredArgsConstructor;
import org.group4.dvdshopbackend.models.order.dto.cancelOrder.CancelOrderReq;
import org.group4.dvdshopbackend.models.order.dto.cancelOrder.CancelOrderRes;
import org.group4.dvdshopbackend.models.order.dto.getOrderList.GetOrderListReq;
import org.group4.dvdshopbackend.models.order.dto.getOrderList.GetOrderListRes;
import org.group4.dvdshopbackend.models.order.dto.sendOrder.SendOrderReq;
import org.group4.dvdshopbackend.models.order.dto.sendOrder.SendOrderRes;
import org.group4.dvdshopbackend.models.order.repository.OrderRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

	private final OrderRepository orderRepository;

	@Override
	public SendOrderRes sendOrder(SendOrderReq req) {
		return null;
	}

	@Override
	public GetOrderListRes getOrderList(GetOrderListReq req) {
		return null;
	}

	@Override
	public CancelOrderRes cancelOrder(CancelOrderReq req) {
		return null;
	}
}
