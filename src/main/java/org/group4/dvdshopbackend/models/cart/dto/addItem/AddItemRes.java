package org.group4.dvdshopbackend.models.cart.dto.addItem;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AddItemRes {
	private Long cartItemId;
}
