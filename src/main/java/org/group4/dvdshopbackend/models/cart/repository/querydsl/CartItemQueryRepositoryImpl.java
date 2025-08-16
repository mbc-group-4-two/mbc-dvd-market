package org.group4.dvdshopbackend.models.cart.repository.querydsl;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.group4.dvdshopbackend.common.entity.QCartItem;
import org.group4.dvdshopbackend.common.entity.QItem;
import org.group4.dvdshopbackend.common.entity.QItemImg;
import org.group4.dvdshopbackend.models.cart.dto.getCartList.GetCartListResItemDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CartItemQueryRepositoryImpl implements CartItemQueryRepository {

	private final JPAQueryFactory queryFactory;

	@Override
	public Page<GetCartListResItemDetail> getPageCartItem(Long cartId, Pageable pageable) {
		QCartItem qCartItem = QCartItem.cartItem;
		QItem qItem = QItem.item;
		QItemImg qItemImg = QItemImg.itemImg;

		var content = queryFactory
				.select(Projections.constructor(GetCartListResItemDetail.class,
						qCartItem.id.as("cartItemId"),
						qItem.id.as("itemId"),
						qItem.itemNm.as("itemName"),
						qItem.price.as("price"),
						qCartItem.count.as("count"),
						qItemImg.imgUrl.as("imgUrl")))
				.from(qCartItem)

				.join(qCartItem.item, qItem)
				.leftJoin(qItemImg).on(qItemImg.item.id.eq(qItem.id).and(qItemImg.repimgYn.eq("Y")))

				.where(qCartItem.cart.id.eq(cartId))

				.offset(pageable.getOffset())
				.limit(pageable.getPageSize())

				.orderBy(qCartItem.id.desc())
				.fetch();

		Long count = queryFactory
				.select(qCartItem.count())
				.from(qCartItem)
				.where(qCartItem.cart.id.eq(cartId))
				.fetchOne();

		return new PageImpl<>(content, pageable, count == null ? 0 : count);
	}
}
