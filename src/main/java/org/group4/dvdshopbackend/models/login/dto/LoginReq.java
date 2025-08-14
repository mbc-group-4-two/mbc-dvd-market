package org.group4.dvdshopbackend.models.login.dto;

import lombok.*;

@Getter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginReq {

    private String email;

    private String password;

}
