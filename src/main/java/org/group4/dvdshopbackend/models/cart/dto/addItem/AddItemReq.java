package org.group4.dvdshopbackend.models.cart.dto.addItem;

import lombok.Data;

@Data
public class AddItemReq {
	private Long memberId;
	private Long itemId;
	private int itemCount;
}
