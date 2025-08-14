package org.group4.dvdshopbackend.models.cart.dto.modifyCart;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ModifyCartRes {
	private Long itemId;
	private int itemCount;
}
