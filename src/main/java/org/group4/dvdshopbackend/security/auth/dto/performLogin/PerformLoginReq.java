package org.group4.dvdshopbackend.security.auth.dto.performLogin;

import lombok.Data;

@Data
public class PerformLoginReq {
	private String userId;
	private String userPassword;
}
