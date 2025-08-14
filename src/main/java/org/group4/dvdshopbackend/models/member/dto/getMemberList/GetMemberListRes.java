package org.group4.dvdshopbackend.models.member.dto.getMemberList;

import lombok.*;
import org.group4.dvdshopbackend.common.entity.Member;

import java.util.List;

@Getter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetMemberListRes {

    private Long totalElementCnt;
    private int totalPageCnt;
    private int pageNum;
    private int pageSize;

    private boolean pageHasPrevious;
    private boolean pageHasNext;
    private boolean pageIsFirst;
    private boolean pageIsLast;

    private List<Member> contents;

}
