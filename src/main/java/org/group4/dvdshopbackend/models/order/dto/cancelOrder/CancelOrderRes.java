package org.group4.dvdshopbackend.models.order.dto.cancelOrder;

import lombok.Builder;
import lombok.Data;
import org.group4.dvdshopbackend.common.entity.Order;

@Data
@Builder
public class CancelOrderRes {
	private Long canceledOrderId;
}
