package org.group4.dvdshopbackend.models.cart.dto.removeItem;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RemoveItemRes {
	Long deletedItemId;
}
