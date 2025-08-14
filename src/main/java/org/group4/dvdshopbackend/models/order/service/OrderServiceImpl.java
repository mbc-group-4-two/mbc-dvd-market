package org.group4.dvdshopbackend.models.order.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.group4.dvdshopbackend.common.entity.Cart;
import org.group4.dvdshopbackend.common.entity.Order;
import org.group4.dvdshopbackend.common.entity.OrderItem;
import org.group4.dvdshopbackend.common.enums.OrderStatus;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

	private static final Logger log = LogManager.getLogger(OrderServiceImpl.class);
	private final MemberJpaRepository memberJpaRepository;
	private final ItemRepository itemRepository;
	private final OrderRepository orderRepository;
	private final CartRepository cartRepository;
	private final CartItemRepository cartItemRepository;
	private final ItemImgRepository itemImgRepository;

	@Override
	@Transactional
	public SendOrderRes sendOrder(SendOrderReq req) {

		// 1. 오더 생성
		var newOrder = new Order();
		newOrder.setOrderDate(LocalDateTime.now());
		newOrder.setOrderStatus(OrderStatus.ORDER);

		// 2. 멤버 조회 및 오더에 정보 추가
		var memberEmail = req.getMemberEmail();
		var member = memberJpaRepository.findByEmailAndDeletedYn(memberEmail, "N")
				.orElseThrow();
		newOrder.setMember(member);

		// 3. 아이템 목록 조회 및 오더에 정보 추가
		var orderItemList = new ArrayList<OrderItem>();

		// total price
		int totalPrice = 0;

		for (var orderItemInfo : req.getOrderItemInfos()) {
			var itemId = orderItemInfo.getItemId();

			var item = itemRepository.findById(itemId)
					.orElseThrow();

			var buyCount = orderItemInfo.getCount();
			var buyPrice = item.getPrice();
			totalPrice += (buyPrice * buyCount);

			orderItemList.add(OrderItem.builder()
					.item(item)
					.count(buyCount)
					.orderPrice(buyPrice)
					.build());

			// 아이템 개수 판매 반영하기
			item.onOrderEvent(buyCount);

			itemRepository.save(item);
		}

		// newOrder에 orderItem Attach
		orderItemList.forEach(newOrder::addOrderItem);

		// newOrder Insert
		var order = orderRepository.save(newOrder);


		// 4. 카트에 해당 아이템 비우기
		// 카트 Id 가져오기
		var cart = cartRepository.findByMemberId(member.getId());

		if (cart == null) {
			var generateCart = new Cart();
			generateCart.setMember(member);
			cart = cartRepository.save(generateCart);
		}

		for (var item : orderItemList) {
			cartItemRepository.deleteByCartIdAndItemId(cart.getId(), item.getId());
		}

		// Response에 표시될 데이터
		var orderItemInfoList = new ArrayList<SendOrderResOrderItemInfo>();
		for (OrderItem orderItem : order.getOrderItems()) {
			var orderItemInfo = SendOrderResOrderItemInfo.builder()
					.itemId(orderItem.getItem().getId())
					.itemName(orderItem.getItem().getItemNm())
					.itemPrice(orderItem.getItem().getPrice())
					.buyCount(orderItem.getCount())
					.build();

			orderItemInfoList.add(orderItemInfo);
		}

		return SendOrderRes.builder()
				.orderId(order.getId())
				.orderItemInfos(orderItemInfoList)
				.totalPrice(totalPrice)
				.build();
	}

	@Override
	public GetOrderListRes getOrderList(GetOrderListReq req) {

		// 1. 멤버 조회
		var memberEmail = req.getMemberEmail();
		var member = memberJpaRepository.findByEmailAndDeletedYn(memberEmail, "N")
				.orElseThrow();

		// 2. 오더 목록 조회
		var orders = orderRepository.findAllByMemberId(member.getId());

		if (orders.isEmpty()) {
			return GetOrderListRes.builder()
					.orderInfos(null)
					.build();
		}

		// response 표시하기 위한 데이터
		var orderInfos = new ArrayList<GetOrderListResOrderInfo>();

		// 각 주문 당
		for (Order order : orders) {

			var orderItems = order.getOrderItems();

			List<GetOrderListResOrderItemInfo> itemInfos = new ArrayList<>();
			int totalPrice = 0;

			for (var orderItem : orderItems) {

				var buyCount = orderItem.getCount();
				var price = orderItem.getOrderPrice();
				totalPrice += (price * buyCount);

				var item = orderItem.getItem();
				var itemImg = itemImgRepository.findByItemIdAndRepimgYn(item.getId(), "Y");
				var itemInfo = new GetOrderListResOrderItemInfo();
				itemInfo.setItemName(item.getItemNm());
				itemInfo.setItemPrice(item.getPrice());
				itemInfo.setBuyCount(orderItem.getCount());
				itemInfo.setItemImgUrl(itemImg.getImgUrl());
				itemInfos.add(itemInfo);
			}

			var orderInfo = GetOrderListResOrderInfo.builder()
					.itemInfos(itemInfos)
					.totalPrice(totalPrice)
					.buyDate(order.getOrderDate())
					.orderStatus(order.getOrderStatus())
					.build();
			orderInfos.add(orderInfo);
		}

		return GetOrderListRes.builder()
				.orderInfos(orderInfos)
				.build();
	}

	@Override
	@Transactional
	public CancelOrderRes cancelOrder(Long orderId) {

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
