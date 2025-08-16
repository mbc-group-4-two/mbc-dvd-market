package org.group4.dvdshopbackend.models.order.dto.sendOrder;

import lombok.Data;

import java.util.List;

@Data
public class SendOrderReq {
	private List<SendOrderReqOrderItemInfo> orderItemInfos;
}
