package org.group4.dvdshopbackend.models.order.dto.getOrderList;

import lombok.Builder;
import lombok.Data;
import org.group4.dvdshopbackend.common.entity.Order;
import org.group4.dvdshopbackend.core.api.PagedRes;
import org.group4.dvdshopbackend.models.cart.dto.getCartList.GetCartListResItemDetail;

import java.util.List;

@Data
@Builder
public class GetOrderListRes {
	private PagedRes<GetOrderListResOrderInfo> orderPage;
}
