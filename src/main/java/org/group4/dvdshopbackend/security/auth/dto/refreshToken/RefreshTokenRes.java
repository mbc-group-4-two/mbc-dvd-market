package org.group4.dvdshopbackend.security.auth.dto.refreshToken;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RefreshTokenRes {
	String accessToken;
	String refreshToken;
}
