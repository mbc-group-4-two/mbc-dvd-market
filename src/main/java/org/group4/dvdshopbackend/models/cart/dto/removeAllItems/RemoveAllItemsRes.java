package org.group4.dvdshopbackend.models.cart.dto.removeAllItems;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RemoveAllItemsRes {
	Long deletedCartId;
}
