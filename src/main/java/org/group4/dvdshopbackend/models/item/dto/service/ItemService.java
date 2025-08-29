package org.group4.dvdshopbackend.models.item.dto.service;

import org.group4.dvdshopbackend.models.item.dto.dto.ItemFormDto;
import org.group4.dvdshopbackend.models.item.dto.dto.ItemListRes;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ItemService {
    Long saveItem(ItemFormDto dto, List<MultipartFile> images) throws Exception;
    ItemFormDto getItemDetail(Long id);
    Long updateItem(ItemFormDto dto, List<MultipartFile> images) throws Exception;
    void deleteItem(Long id);
    Page<ItemListRes> getItemPage(int page, int size);


}

