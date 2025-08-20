package org.group4.dvdshopbackend.security.auth.repository;

import org.group4.dvdshopbackend.common.entity.UserRefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRefreshTokensRepository extends JpaRepository<UserRefreshToken, Long> {

}
