package org.group4.dvdshopbackend.models.order.dto.getOrderList;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GetOrderListResOrderItemInfo {
	private Long orderId;
	private String itemName;
	private int itemPrice;
	private int buyCount;
	private String itemImgUrl;
}
