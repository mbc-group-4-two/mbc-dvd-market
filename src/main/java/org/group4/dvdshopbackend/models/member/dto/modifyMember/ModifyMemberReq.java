package org.group4.dvdshopbackend.models.member.dto.modifyMember;

import lombok.*;

@Getter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ModifyMemberReq {

    private Long id;

    private String password;

    private String address;

}
