package org.group4.dvdshopbackend.models.item.dto.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.group4.dvdshopbackend.common.enums.ItemSellStatus;

@Builder
@Data @NoArgsConstructor @AllArgsConstructor
public class ItemListRes {
    private Long id;
    private String itemNm;
    private Integer price;
    private String thumbnailUrl;
    private Integer stockNumber;
    private ItemSellStatus itemSellStatus;
}