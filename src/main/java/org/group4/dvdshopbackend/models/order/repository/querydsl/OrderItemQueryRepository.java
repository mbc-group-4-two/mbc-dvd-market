package org.group4.dvdshopbackend.models.order.repository.querydsl;

import org.group4.dvdshopbackend.models.order.dto.getOrderList.GetOrderListResOrderItemInfo;

import java.util.List;

public interface OrderItemQueryRepository {
	List<GetOrderListResOrderItemInfo> getOrderItemInfos(List<Long> orderIds);
}
