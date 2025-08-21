package org.group4.dvdshopbackend.security.auth.dto.performLogin;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PerformLoginRes {
	String accessToken;
	String refreshToken;
}
