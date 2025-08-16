package org.group4.dvdshopbackend.models.order.repository.querydsl;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.group4.dvdshopbackend.common.entity.QCartItem;
import org.group4.dvdshopbackend.common.entity.QItem;
import org.group4.dvdshopbackend.common.entity.QItemImg;
import org.group4.dvdshopbackend.common.entity.QOrderItem;
import org.group4.dvdshopbackend.models.cart.dto.getCartList.GetCartListResItemDetail;
import org.group4.dvdshopbackend.models.order.dto.getOrderList.GetOrderListResOrderItemInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderItemQueryRepositoryImpl implements OrderItemQueryRepository {

	private final JPAQueryFactory queryFactory;

	@Override
	public List<GetOrderListResOrderItemInfo> getOrderItemInfos(List<Long> orderIds) {

		if (orderIds == null || orderIds.isEmpty()) return List.of();

		QOrderItem qOrderItem = QOrderItem.orderItem;
		QItem qItem = QItem.item;
		QItemImg qItemImg = QItemImg.itemImg;

		return queryFactory.select(Projections.constructor(
						GetOrderListResOrderItemInfo.class,
						qOrderItem.order.id,          // Long
						qItem.itemNm,             // String
						qItem.price,              // Integer/Long/BigDecimal 타입에 맞춰 DTO 조정
						qOrderItem.count,             // Integer
						qItemImg.imgUrl            // String
				))
				.from(qOrderItem)
				.join(qOrderItem.item, qItem)
				.leftJoin(qItemImg).on(qItemImg.item.id.eq(qItem.id)
						.and(qItemImg.repimgYn.eq("Y")))
				.where(qOrderItem.order.id.in(orderIds))
				.fetch();
	}
}
