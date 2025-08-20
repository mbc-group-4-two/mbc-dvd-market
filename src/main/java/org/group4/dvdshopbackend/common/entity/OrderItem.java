package org.group4.dvdshopbackend.common.entity;

import jakarta.persistence.*;
import lombok.*;
import org.group4.dvdshopbackend.core.BaseEntity;

@Entity
@Getter @Setter
@Builder // 빌더 땜에 빌드할때 경고뜸
@AllArgsConstructor
@NoArgsConstructor
public class OrderItem extends BaseEntity {

    @Id
    @SequenceGenerator(
            name = "order_item_seq",
            sequenceName = "order_item_seq_tbl",
            allocationSize = 1 // sequence 캐싱 처리, 배포시 50정도 -> 병목시 늘리기
    )
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "order_item_seq")
    @Column(name = "order_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    private int orderPrice;

    private int count;

    public void attachOrder(Order order) {
        this.order = order;
    }
}