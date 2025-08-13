package org.group4.dvdshopbackend.models.member.dto.postMember;

import lombok.*;
import org.group4.dvdshopbackend.common.entity.Member;

@Getter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostMemberRes {

    private Member memberDetail;

}
