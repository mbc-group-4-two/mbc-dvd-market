package org.group4.dvdshopbackend.models.item.dto.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.group4.dvdshopbackend.common.entity.ItemImg;
import org.group4.dvdshopbackend.models.item.dto.repository.ItemImgRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

// models.item.dto.service.ItemImgService
@Service
@Transactional
@RequiredArgsConstructor
public class ItemImgService {
    private final ItemImgRepository itemImgRepository;
    private final FileService fileService;

    @Value("${itemImgLocation:/uploads}")   // 예: C:/shop/item
    private String uploadPath;

    // 신규 저장
    public void saveItemImg(ItemImg itemImg, MultipartFile file) throws Exception {
        if (file == null || file.isEmpty()) { itemImgRepository.save(itemImg); return; }
        String ori = file.getOriginalFilename();
        String saved = fileService.uploadFile(uploadPath, ori, file.getBytes());
        itemImg.setOriImgName(ori);
        itemImg.setImgName(saved);
        itemImg.setImgUrl("/images/" + saved);
        itemImgRepository.save(itemImg);
    }

    // 업데이트
    public void updateItemImg(Long itemImgId, MultipartFile file) throws Exception {
        if (file == null || file.isEmpty()) return;
        ItemImg img = itemImgRepository.findById(itemImgId)
                .orElseThrow(() -> new EntityNotFoundException("img not found"));
        if (img.getImgName() != null)
            fileService.deleteFile(uploadPath + "/" + img.getImgName());
        String ori = file.getOriginalFilename();
        String saved = fileService.uploadFile(uploadPath, ori, file.getBytes());
        img.setOriImgName(ori);
        img.setImgName(saved);
        img.setImgUrl("/images/" + saved);
    }

    // 삭제
    public void deleteItemImg(Long itemImgId) {
        ItemImg img = itemImgRepository.findById(itemImgId)
                .orElseThrow(() -> new EntityNotFoundException("img not found"));
        if (img.getImgName() != null) {
            try { fileService.deleteFile(uploadPath + "/" + img.getImgName()); } catch (Exception ignore) {}
        }
        itemImgRepository.delete(img);
    }
}

