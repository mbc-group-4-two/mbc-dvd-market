package org.group4.dvdshopbackend.common.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.group4.dvdshopbackend.common.enums.ItemSellStatus;
import org.group4.dvdshopbackend.core.BaseEntity;

@Entity
@Table(name="item")
@Getter
@Setter
@ToString
public class Item extends BaseEntity {

    @Id
    @Column(name="item_id")
    @SequenceGenerator(
            name = "item_seq",
            sequenceName = "item_seq_tbl",
            allocationSize = 1 // sequence 캐싱 처리, 배포시 50정도 -> 병목시 늘리기
    )
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "item_seq")
    private Long id;

    @Column(nullable = false, length = 50)
    private String itemNm;

    @Column(name="price", nullable = false)
    private int price;

    @Column(nullable = false)
    private int stockNumber;

    @Lob
    @Column(nullable = false)
    private String itemDetail;

    @Enumerated(EnumType.STRING)
    private ItemSellStatus itemSellStatus;

    public void onOrderEvent(int buyCount) {
        stockNumber -= buyCount;

        if (0 > stockNumber) {
            throw new RuntimeException("재고 부족");
        }
    }
}