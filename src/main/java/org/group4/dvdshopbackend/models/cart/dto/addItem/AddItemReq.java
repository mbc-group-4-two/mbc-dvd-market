package org.group4.dvdshopbackend.models.cart.dto.addItem;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AddItemReq {
	private Long itemId;
	private int itemCount;
}
