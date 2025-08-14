package org.group4.dvdshopbackend.models.order.dto.sendOrder;

import lombok.Data;

@Data
public class SendOrderReqOrderItemInfo {
	private Long itemId;
	private int count;
}
