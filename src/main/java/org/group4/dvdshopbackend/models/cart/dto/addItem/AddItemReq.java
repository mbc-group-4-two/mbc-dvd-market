package org.group4.dvdshopbackend.models.cart.dto.addItem;

import lombok.Data;

@Data
public class AddItemReq {
	private String memberEmail;
	private Long itemId;
	private int itemCount;
}
