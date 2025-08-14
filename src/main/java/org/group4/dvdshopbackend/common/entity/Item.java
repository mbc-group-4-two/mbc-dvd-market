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
    @GeneratedValue(strategy = GenerationType.AUTO)
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