package org.group4.dvdshopbackend.models.order.dto.sendOrder;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SendOrderResOrderItemInfo {
	private Long itemId;
	private String itemName;
	private int itemPrice;
	private int buyCount;
}
