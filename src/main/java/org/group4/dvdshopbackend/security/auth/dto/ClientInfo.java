package org.group4.dvdshopbackend.security.auth.dto;

import jakarta.servlet.http.HttpServletRequest;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter @Setter
public class ClientInfo {

	private String userAgent;
	private String deviceId;
	private String ip;

	private String getClientId(HttpServletRequest request) {
		var header = request.getHeader("X-Forwarded-For");
		if (header != null && !header.isBlank()) return header.split(",")[0].trim();
		return request.getRemoteAddr();
	}

	public ClientInfo(HttpServletRequest httpRequest) {
		userAgent = httpRequest.getHeader("User-Agent");
		deviceId = httpRequest.getHeader("X-Device-Id");
		ip = getClientId(httpRequest);
	}
}
