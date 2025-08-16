package org.group4.dvdshopbackend.security.auth.service;

import org.group4.dvdshopbackend.security.auth.dto.PerformLoginReq;
import org.group4.dvdshopbackend.security.auth.dto.PerformLoginRes;

public interface AuthService {
	PerformLoginRes performLogin(PerformLoginReq req);
}
