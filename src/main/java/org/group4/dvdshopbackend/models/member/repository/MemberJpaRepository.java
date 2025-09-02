package org.group4.dvdshopbackend.models.member.repository;

import org.group4.dvdshopbackend.common.entity.Member;
import org.group4.dvdshopbackend.common.enums.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MemberJpaRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByEmailAndDeletedYn(String email, String deletedYn);


    boolean existsByEmailAndDeletedYn(String email, String deletedYn);


    Optional<Member> findByIdAndDeletedYn(Long id, String deletedYn);


    @Query("select m from Member m where (:includeDeleted = true or m.deletedYn = 'N')")
    Page<Member> searchAll(@Param("includeDeleted") boolean includeDeleted, Pageable pageable);

    Optional<Member> findByEmail(String memberEmail);

    /**
     * 토큰 버전 조회
     * @param id
     * @return
     */
    @Query("select m.tokenVersion from Member m where m.id = :id")
    Long findTokenVersionById(@Param("id") Long id);


    /**
     * 토큰 버전 원자적 증가
     * @param id
     * @param cur
     * @return
     */
    @Modifying
    @Query("update Member m set m.tokenVersion = m.tokenVersion + 1 where m.id = :id and m.tokenVersion = :cur")
    int bumpVersionIfMatch(@Param("id") Long id, @Param("cur") int cur);

    /**
     * 멤버 권한 조회
     * @param id
     * @return
     */
    @Query("select m.role from Member m where m.id = :id")
    Role findRoleById(@Param("id") Long id);
}
