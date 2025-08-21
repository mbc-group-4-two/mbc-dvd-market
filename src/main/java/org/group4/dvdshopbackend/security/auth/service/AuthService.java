package org.group4.dvdshopbackend.security.auth.service;

import org.group4.dvdshopbackend.security.auth.dto.ClientInfo;
import org.group4.dvdshopbackend.security.auth.dto.performLogin.PerformLoginReq;
import org.group4.dvdshopbackend.security.auth.dto.performLogin.PerformLoginRes;
import org.group4.dvdshopbackend.security.auth.dto.refreshToken.RefreshTokenReq;
import org.group4.dvdshopbackend.security.auth.dto.refreshToken.RefreshTokenRes;

public interface AuthService {
//	PerformLoginRes performLogin(PerformLoginReq req);
	PerformLoginRes performLogin(PerformLoginReq req, ClientInfo clientInfo);
//	PerformLoginRes performLogin(PerformLoginReq req, String userAgent, String deviceId, String ip);

	RefreshTokenRes refreshToken(RefreshTokenReq req, ClientInfo clientInfo);
}
