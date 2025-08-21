package org.group4.dvdshopbackend.models.item.dto.controller;


import lombok.RequiredArgsConstructor;
import org.group4.dvdshopbackend.core.api.ApiResult;
import org.group4.dvdshopbackend.models.item.dto.dto.ItemFormDto;
import org.group4.dvdshopbackend.models.item.dto.dto.ItemListRes;
import org.group4.dvdshopbackend.models.item.dto.service.ItemService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

@RestController
@RequestMapping("/api/item")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(consumes = {"multipart/form-data"})
    public ApiResult<Long> createItem(
            @RequestPart("data") ItemFormDto itemFormDto,
            @RequestPart(value = "images", required = false) List<MultipartFile> images) throws Exception {
        var result = itemService.saveItem(itemFormDto, images);
        return new ApiResult<>(result);
    }

    @GetMapping("/{itemId}")
    public ApiResult<ItemFormDto> getItemDetail(@PathVariable Long itemId) {
        var result = itemService.getItemDetail(itemId);
        return new ApiResult<>(result);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping(value = "/{itemId}", consumes = {"multipart/form-data"})
    public ApiResult<Long> updateItem(
            @PathVariable Long itemId,
            @RequestPart("data") ItemFormDto itemFormDto,
            @RequestPart(value = "images", required = false) List<MultipartFile> images) throws Exception {
        itemFormDto.setId(itemId);
        var result = itemService.updateItem(itemFormDto, images);
        return new ApiResult<>(result);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{itemId}")
    public ApiResult<Boolean> deleteItem(@PathVariable Long itemId) {
        itemService.deleteItem(itemId);
        return new ApiResult<>(true);
    }

    @GetMapping
    public ApiResult<Page<ItemListRes>> getPage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size
    ) {
        var result = itemService.getItemPage(page, size);
        return new ApiResult<>(result);
    }


}








