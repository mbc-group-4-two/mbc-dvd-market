package org.group4.dvdshopbackend.models.item.dto.repository;

import org.group4.dvdshopbackend.common.entity.ItemImg;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemImgRepository extends JpaRepository<ItemImg, Long> {

    List<ItemImg> findByItemIdOrderByIdAsc(Long itemId);

    ItemImg findByItemIdAndRepimgYn(Long itemId, String repimgYn);
    // 312 추가 상품의 대표이미지를 찾는 쿼리 메서드(구매 이력 페이지에서 주문 상품의 대표 이미지 보여줌)

}
