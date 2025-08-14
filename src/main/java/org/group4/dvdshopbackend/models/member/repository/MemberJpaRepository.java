package org.group4.dvdshopbackend.models.member.repository;

import org.group4.dvdshopbackend.common.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MemberJpaRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmailAndDeletedYn(String email, String deletedYn);

    boolean existsByEmailAndDeletedYn(String email, String deletedYn);


    Page<Member> findAll(Pageable pageable);

    @Query("select m from Member m where (:includeDeleted = true or m.deletedYn = 'N')")
    Page<Member> searchAll(@Param("includeDeleted") boolean includeDeleted, Pageable pageable);

    Long id(Long id);

    Optional<Member> findByEmail(String memberEmail);
}
