package org.group4.dvdshopbackend.models.item.dto.service;

import org.group4.dvdshopbackend.models.item.dto.ItemFormDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

// ItemService.java (인터페이스)
public interface ItemService {
    Long saveItem(ItemFormDto dto, List<MultipartFile> images) throws Exception;
    ItemFormDto getItemDetail(Long id);
    Long updateItem(ItemFormDto dto, List<MultipartFile> images) throws Exception;
    void deleteItem(Long id);
}

