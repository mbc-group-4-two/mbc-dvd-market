package org.group4.dvdshopbackend.models.login.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.group4.dvdshopbackend.models.login.dto.LoginReq;
import org.group4.dvdshopbackend.models.login.dto.LoginRes;

public interface LoginService {

    LoginRes login(LoginReq request, HttpServletRequest httpRequest);

    void logout(HttpServletRequest httpRequest, HttpServletResponse httpResponse);
}
