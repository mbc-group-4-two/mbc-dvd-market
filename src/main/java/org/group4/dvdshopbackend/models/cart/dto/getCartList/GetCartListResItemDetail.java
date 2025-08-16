package org.group4.dvdshopbackend.models.cart.dto.getCartList;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetCartListResItemDetail {

	private Long cartItemId; //장바구니 아이디

	private Long itemId; //장바구니 상품 아이디

	private String itemName; //상품명

	private Integer price; //상품 금액

	private Integer count; //수량

	private String imgUrl; //상품 이미지 경로
}
