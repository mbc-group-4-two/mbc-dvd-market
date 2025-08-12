package org.group4.dvdshopbackend.models.item.dto.repository;

import org.group4.dvdshopbackend.common.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Long>, ItemRepositoryCustom {
}
