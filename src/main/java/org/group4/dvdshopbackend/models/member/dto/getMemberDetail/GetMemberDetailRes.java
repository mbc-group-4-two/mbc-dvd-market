package org.group4.dvdshopbackend.models.member.dto.getMemberDetail;

import lombok.*;
import org.group4.dvdshopbackend.common.entity.Member;

@Getter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetMemberDetailRes {

    private Member detail;
}
