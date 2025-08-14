package org.group4.dvdshopbackend.models.order.dto.getOrderList;

import lombok.Builder;
import lombok.Data;
import org.group4.dvdshopbackend.common.enums.OrderStatus;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class GetOrderListResOrderInfo {
	private List<GetOrderListResOrderItemInfo> itemInfos;
	private int totalPrice;
	private LocalDateTime buyDate;
	private OrderStatus orderStatus;
}
