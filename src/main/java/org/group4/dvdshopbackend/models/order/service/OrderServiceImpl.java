package org.group4.dvdshopbackend.models.order.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.group4.dvdshopbackend.common.entity.Cart;
import org.group4.dvdshopbackend.common.entity.Member;
import org.group4.dvdshopbackend.common.entity.Order;
import org.group4.dvdshopbackend.common.entity.OrderItem;
import org.group4.dvdshopbackend.common.enums.OrderStatus;
import org.group4.dvdshopbackend.core.api.PagedRes;
import org.group4.dvdshopbackend.models.cart.dto.getCartList.GetCartListRes;
import org.group4.dvdshopbackend.models.cart.repository.CartItemRepository;
import org.group4.dvdshopbackend.models.cart.repository.CartRepository;
import org.group4.dvdshopbackend.models.item.dto.repository.ItemImgRepository;
import org.group4.dvdshopbackend.models.item.dto.repository.ItemRepository;
import org.group4.dvdshopbackend.models.member.repository.MemberJpaRepository;
import org.group4.dvdshopbackend.models.order.dto.cancelOrder.CancelOrderReq;
import org.group4.dvdshopbackend.models.order.dto.cancelOrder.CancelOrderRes;
import org.group4.dvdshopbackend.models.order.dto.getOrderList.GetOrderListReq;
import org.group4.dvdshopbackend.models.order.dto.getOrderList.GetOrderListRes;
import org.group4.dvdshopbackend.models.order.dto.getOrderList.GetOrderListResOrderInfo;
import org.group4.dvdshopbackend.models.order.dto.getOrderList.GetOrderListResOrderItemInfo;
import org.group4.dvdshopbackend.models.order.dto.sendOrder.SendOrderReq;
import org.group4.dvdshopbackend.models.order.dto.sendOrder.SendOrderReqOrderItemInfo;
import org.group4.dvdshopbackend.models.order.dto.sendOrder.SendOrderRes;
import org.group4.dvdshopbackend.models.order.dto.sendOrder.SendOrderResOrderItemInfo;
import org.group4.dvdshopbackend.models.order.repository.OrderRepository;
import org.group4.dvdshopbackend.models.order.repository.querydsl.OrderItemQueryRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

	private final MemberJpaRepository memberJpaRepository;
	private final ItemRepository itemRepository;
	private final OrderRepository orderRepository;
	private final CartRepository cartRepository;
	private final CartItemRepository cartItemRepository;

	private final OrderItemQueryRepository orderItemQueryRepository;



	private Order createOrder(Long memberId, List<SendOrderReqOrderItemInfo> orderItemInfos) {
		var newOrder = new Order();

		// 오더에 주문자정보 추가
		newOrder.setMember(memberJpaRepository.findById(memberId)
				.orElseThrow());

		// 구매한 아이템 개수대로 item 조회하여 정보(item 조인, 가격, 개수) 조합
		for (var orderItemInfo : orderItemInfos) {
			var item = itemRepository.findById(orderItemInfo.getItemId())
					.orElseThrow();

			var newOrderItem = OrderItem.builder()
					.item(item)
					.orderPrice(item.getPrice())
					.count(orderItemInfo.getCount())
					.build();

			// 아이템 주문 개수만큼 차감
			item.onOrderEvent(orderItemInfo.getCount());

			// 오더에 해당 정보 추가
			newOrder.addOrderItem(newOrderItem);

			// 오더 상태 변경
			newOrder.setOrderStatus(OrderStatus.ORDER);

			// 오더 주문 체결일자 변경
			newOrder.setOrderDate(LocalDateTime.now());


		}

		return newOrder;
	}

	private List<Order> findOrders(Long userId) {
		return orderRepository.findAllByMemberId(userId);
	}

	@Override
	@Transactional
	public SendOrderRes sendOrder(Long userId, SendOrderReq req) {

		// 1. 오더정보 생성
		var newOrder = createOrder(userId, req.getOrderItemInfos());

		// 2. 오더 저장
		var order = orderRepository.save(newOrder);

		// 3. 카트에 해당 아이템 카운트만큼 비워주기
		var cart = cartRepository.findByMemberId(userId);
		for (var orderItem : order.getOrderItems()) {
			var item = orderItem.getItem();
			cartItemRepository.deleteByCartIdAndItemId(cart.getId(), item.getId());
		}

		return SendOrderRes.builder()
				.orderId(order.getId())
				.build();
	}

	@Override
	public GetOrderListRes getOrderList(Long userId, Pageable pageable) {

		var orderPageResult = orderRepository.findAllByMemberId(userId, pageable);

		// order Page 에서 orderId만 list 추출
		var orderIds = orderPageResult.getContent().stream()
				.map(Order::getId).toList();

		// 각 주문 별 주문정보 join query
		var orderItemInfos = orderItemQueryRepository.getOrderItemInfos(orderIds);

		Map<Long, List<GetOrderListResOrderItemInfo>> itemsMap = new HashMap<>();
		Map<Long, Integer> totalMap = new HashMap<>();

		for (var orderItemInfo : orderItemInfos) {
			var item = GetOrderListResOrderItemInfo.builder()
					.itemName(orderItemInfo.getItemName())
					.itemPrice(orderItemInfo.getItemPrice())
					.buyCount(orderItemInfo.getBuyCount())
					.itemImgUrl(orderItemInfo.getItemImgUrl())
					.build();
			itemsMap.computeIfAbsent(orderItemInfo.getOrderId(), k -> new ArrayList<>()).add(item);
			totalMap.merge(orderItemInfo.getOrderId(), orderItemInfo.getItemPrice() * orderItemInfo.getBuyCount(), Integer::sum);
		}

		List<GetOrderListResOrderInfo> content = new ArrayList<>();
		for (Order o : orderPageResult.getContent()) {
			var orderInfo = GetOrderListResOrderInfo.builder()
					.orderId(o.getId())
					.itemInfos(itemsMap.getOrDefault(o.getId(), List.of()))
					.totalPrice(totalMap.getOrDefault(o.getId(), 0))
					.buyDate(o.getOrderDate())
					.orderStatus(o.getOrderStatus())
					.build();
			content.add(orderInfo);
		}

		Page<GetOrderListResOrderInfo> orderPage =
				new PageImpl<>(content, pageable, orderPageResult.getTotalElements());

		return GetOrderListRes.builder()
				.orderPage(new PagedRes<>(orderPage))
				.build();
	}

	@Override
	@Transactional
	public CancelOrderRes cancelOrder(Long userId, Long orderId) {

		// 1. 오더 조회
		var order = orderRepository.findById(orderId)
				.orElseThrow();

		// 2. 오더 취소
		order.setOrderStatus(OrderStatus.CANCEL);

		return CancelOrderRes.builder()
				.canceledOrderId(order.getId())
				.build();
	}
}
