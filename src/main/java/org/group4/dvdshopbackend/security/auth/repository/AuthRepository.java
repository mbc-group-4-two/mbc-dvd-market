package org.group4.dvdshopbackend.security.auth.repository;

import org.group4.dvdshopbackend.common.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthRepository extends JpaRepository<Member, Long> {
}
