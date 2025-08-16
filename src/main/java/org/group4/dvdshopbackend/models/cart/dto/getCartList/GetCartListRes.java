package org.group4.dvdshopbackend.models.cart.dto.getCartList;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import org.group4.dvdshopbackend.core.api.PagedRes;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GetCartListRes {
	private PagedRes<GetCartListResItemDetail> cartPage;
	private List<GetCartListResItemDetail> itemDetails;
}