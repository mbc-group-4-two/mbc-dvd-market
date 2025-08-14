package org.group4.dvdshopbackend.models.order.dto.getOrderList;

import lombok.Data;

@Data
public class GetOrderListResOrderItemInfo {
	private String itemName;
	private int itemPrice;
	private int buyCount;
	private String itemImgUrl;
}
