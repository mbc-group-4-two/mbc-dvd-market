package org.group4.dvdshopbackend.models.cart.dto.getCartList;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class GetCartListRes {


	private List<GetCartListResItemDetail> itemDetails;
}