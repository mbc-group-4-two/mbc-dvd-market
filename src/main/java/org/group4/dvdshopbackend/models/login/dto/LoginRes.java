package org.group4.dvdshopbackend.models.login.dto;

import lombok.*;

@Getter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginRes {

    private Long id;

    private String email;

    private String name;

    private String role;

    private String token;

}
