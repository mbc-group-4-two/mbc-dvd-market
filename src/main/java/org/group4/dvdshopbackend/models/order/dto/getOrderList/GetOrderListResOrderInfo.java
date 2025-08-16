package org.group4.dvdshopbackend.models.order.dto.getOrderList;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.group4.dvdshopbackend.common.enums.OrderStatus;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetOrderListResOrderInfo {
	private Long orderId;
	private List<GetOrderListResOrderItemInfo> itemInfos;
	private int totalPrice;
	private LocalDateTime buyDate;
	private OrderStatus orderStatus;
}
