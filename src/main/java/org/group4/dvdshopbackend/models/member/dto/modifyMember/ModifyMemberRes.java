package org.group4.dvdshopbackend.models.member.dto.modifyMember;

import lombok.*;
import org.group4.dvdshopbackend.common.entity.Member;

@Getter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ModifyMemberRes {

    private Member modified;
}
