package org.group4.dvdshopbackend.models.member.dto.deleteMember;

import lombok.*;
import org.group4.dvdshopbackend.common.entity.Member;

@Getter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DeleteMemberRes {

    private Member deleted;

}
