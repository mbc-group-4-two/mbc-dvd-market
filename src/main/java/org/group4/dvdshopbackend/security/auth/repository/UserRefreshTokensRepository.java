package org.group4.dvdshopbackend.security.auth.repository;

import jakarta.persistence.LockModeType;
import org.group4.dvdshopbackend.security.auth.domain.UserRefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRefreshTokensRepository extends JpaRepository<UserRefreshToken, Long> {

//	UserRefreshToken findByJti(String jti);

//	@Lock(LockModeType.PESSIMISTIC_WRITE) // native query 와 혼용 불가능함
	//Resolved [org.springframework.dao.InvalidDataAccessApiUsageException: Illegal attempt to set lock mode for a native query]
	@Query(value = "select * from user_refresh_tokens where jti = :jti for update", nativeQuery = true)
	Optional<UserRefreshToken> findByJtiForUpdate(@Param("jti") String jti);

}
