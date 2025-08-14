package org.group4.dvdshopbackend.models.order.dto.getOrderList;

import lombok.Builder;
import lombok.Data;
import org.group4.dvdshopbackend.common.entity.Order;

import java.util.List;

@Data
@Builder
public class GetOrderListRes {
	List<GetOrderListResOrderInfo> orderInfos;
}
