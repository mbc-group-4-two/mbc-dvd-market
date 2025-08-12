package org.group4.dvdshopbackend.models.item.dto.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.group4.dvdshopbackend.common.entity.Item;
import org.group4.dvdshopbackend.common.entity.ItemImg;
import org.group4.dvdshopbackend.models.item.dto.ItemFormDto;
import org.group4.dvdshopbackend.models.item.dto.ItemImgDto;
import org.group4.dvdshopbackend.models.item.dto.repository.ItemImgRepository;
import org.group4.dvdshopbackend.models.item.dto.repository.ItemRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final ItemImgRepository itemImgRepository;
    private final ItemImgService itemImgService;

    @Override
    public Long saveItem(ItemFormDto itemFormDto, List<MultipartFile> itemImgFileList) throws Exception {
        // 1) 상품 저장
        Item item = itemFormDto.createItem();
        itemRepository.save(item);

        // 2) 이미지 저장 (첫 장 대표)
        if (itemImgFileList != null) {
            for (int i = 0; i < itemImgFileList.size(); i++) {
                MultipartFile file = itemImgFileList.get(i);
                if (file == null || file.isEmpty()) continue;

                ItemImg itemImg = new ItemImg();
                itemImg.setItem(item);
                itemImg.setRepimgYn(i == 0 ? "Y" : "N");

                itemImgService.saveItemImg(itemImg, file);
            }
        }
        return item.getId();
    }

    @Override
    @Transactional(readOnly = true)
    public ItemFormDto getItemDetail(Long itemId) {
        // 이미지 목록
        List<ItemImg> itemImgList = itemImgRepository.findByItemIdOrderByIdAsc(itemId);
        List<ItemImgDto> itemImgDtoList = new ArrayList<>();
        for (ItemImg itemImg : itemImgList) {
            itemImgDtoList.add(ItemImgDto.of(itemImg));
        }

        // 상품
        Item item = itemRepository.findById(itemId)
                .orElseThrow(EntityNotFoundException::new);

        ItemFormDto dto = ItemFormDto.of(item);
        dto.setItemImgDtoList(itemImgDtoList);
        return dto;
    }

    @Override
    public Long updateItem(ItemFormDto itemFormDto, List<MultipartFile> itemImgFileList) throws Exception {
        Item item = itemRepository.findById(itemFormDto.getId())
                .orElseThrow(EntityNotFoundException::new);

        // 엔티티에 update 메서드가 없다면 개별 세팅
        item.setItemNm(itemFormDto.getItemNm());
        item.setPrice(itemFormDto.getPrice());
        item.setStockNumber(itemFormDto.getStockNumber());
        item.setItemDetail(itemFormDto.getItemDetail());
        item.setItemSellStatus(itemFormDto.getItemSellStatus());

        // 이미지 교체(폼에서 넘어온 기존 이미지 id 순서와 업로드 파일 순서가 매핑된다고 가정)
        if (itemImgFileList != null && itemFormDto.getItemImgIds() != null) {
            List<Long> itemImgIds = itemFormDto.getItemImgIds();
            int limit = Math.min(itemImgIds.size(), itemImgFileList.size());
            for (int i = 0; i < limit; i++) {
                MultipartFile file = itemImgFileList.get(i);
                if (file == null || file.isEmpty()) continue;
                itemImgService.updateItemImg(itemImgIds.get(i), file);
            }
        }
        return item.getId();
    }

    @Override
    public void deleteItem(Long itemId) {
        // 이미지부터 정리 (파일+DB)
        List<ItemImg> imgs = itemImgRepository.findByItemIdOrderByIdAsc(itemId);
        for (ItemImg img : imgs) {
            itemImgService.deleteItemImg(img.getId());
        }
        // 상품 삭제
        itemRepository.deleteById(itemId);
    }
}
