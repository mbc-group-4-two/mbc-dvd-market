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

    @Value("${itemImgLocation:/uploads}") // 실제 저장 폴더(예: C:/shop/item)
    private String itemImgLocation;

    public void saveItemImg(ItemImg itemImg, MultipartFile file) throws Exception {
        if (file == null || file.isEmpty()) {
            itemImgRepository.save(itemImg);
            return;
        }
        String ori = file.getOriginalFilename();
        String saved = fileService.uploadFile(itemImgLocation, ori, file.getBytes());

        itemImg.setOriImgName(ori);
        itemImg.setImgName(saved);
        itemImg.setImgUrl("/images/item/" + saved);   // 브라우저 경로(정적 리소스 매핑과 일치)

        itemImgRepository.save(itemImg);
    }

    public void updateItemImg(Long itemImgId, MultipartFile file) throws Exception {
        if (file == null || file.isEmpty()) return;

        ItemImg img = itemImgRepository.findById(itemImgId)
                .orElseThrow(() -> new EntityNotFoundException("img not found"));

        // 기존 파일 삭제
        if (img.getImgName() != null) {
            fileService.deleteFile(itemImgLocation + "/" + img.getImgName());
        }

        String ori = file.getOriginalFilename();
        String saved = fileService.uploadFile(itemImgLocation, ori, file.getBytes());

        img.setOriImgName(ori);
        img.setImgName(saved);
        img.setImgUrl("/images/item/" + saved);       // 동일 규칙
        // JPA 더티체킹으로 업데이트 반영
    }

    public void deleteItemImg(Long itemImgId) {
        ItemImg img = itemImgRepository.findById(itemImgId)
                .orElseThrow(() -> new EntityNotFoundException("img not found"));
        if (img.getImgName() != null) {
            try { fileService.deleteFile(itemImgLocation + "/" + img.getImgName()); } catch (Exception ignore) {}
        }
        itemImgRepository.delete(img);
    }
}
