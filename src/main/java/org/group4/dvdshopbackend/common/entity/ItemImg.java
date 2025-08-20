package org.group4.dvdshopbackend.common.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.group4.dvdshopbackend.core.BaseEntity;

@Entity
@Table(name="item_img")
@Getter @Setter
public class ItemImg extends BaseEntity {

    @Id
    @Column(name="item_img_id")
    @SequenceGenerator(
            name = "item_img_seq",
            sequenceName = "item_img_seq_tbl",
            allocationSize = 1 // sequence 캐싱 처리, 배포시 50정도 -> 병목시 늘리기
    )
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "item_img_seq")
    private Long id;

    private String imgName;

    private String oriImgName;

    private String imgUrl;

    private String repimgYn;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;
}