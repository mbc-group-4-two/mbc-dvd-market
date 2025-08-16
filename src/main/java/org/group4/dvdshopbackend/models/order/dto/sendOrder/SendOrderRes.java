package org.group4.dvdshopbackend.models.order.dto.sendOrder;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;
import lombok.Data;
import org.group4.dvdshopbackend.common.entity.Order;
import org.group4.dvdshopbackend.common.entity.OrderItem;

import java.util.List;

@Data
@Builder
@JsonPropertyOrder({ "orderId", "orderItemInfos", "totalPrice" })
public class SendOrderRes {
	private Long orderId;
}
