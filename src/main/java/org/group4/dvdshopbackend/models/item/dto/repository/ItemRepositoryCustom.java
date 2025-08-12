// ItemRepositoryCustom.java
package org.group4.dvdshopbackend.models.item.dto.repository;

import org.group4.dvdshopbackend.common.entity.Item;
import org.group4.dvdshopbackend.common.enums.ItemSellStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ItemRepositoryCustom {
    Page<Item> search(String keyword, ItemSellStatus status, Pageable pageable);
}
