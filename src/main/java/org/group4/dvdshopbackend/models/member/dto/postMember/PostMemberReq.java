package org.group4.dvdshopbackend.models.member.dto.postMember;

import lombok.*;

@Getter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostMemberReq {

    private String name;

    private String email;

    private String password;

    private String address;

}
