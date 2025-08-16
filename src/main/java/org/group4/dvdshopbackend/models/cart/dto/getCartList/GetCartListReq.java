package org.group4.dvdshopbackend.models.cart.dto.getCartList;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetCartListReq {

	@Builder.Default
	private int page = 1;

	@Builder.Default
	private int size = 5;
}
