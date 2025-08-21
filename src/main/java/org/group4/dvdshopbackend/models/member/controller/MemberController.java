package org.group4.dvdshopbackend.models.member.controller;

import lombok.RequiredArgsConstructor;
import org.group4.dvdshopbackend.core.api.ApiResult;
import org.group4.dvdshopbackend.models.member.dto.deleteMember.DeleteMemberReq;
import org.group4.dvdshopbackend.models.member.dto.deleteMember.DeleteMemberRes;
import org.group4.dvdshopbackend.models.member.dto.getMemberDetail.GetMemberDetailReq;
import org.group4.dvdshopbackend.models.member.dto.getMemberDetail.GetMemberDetailRes;
import org.group4.dvdshopbackend.models.member.dto.getMemberList.GetMemberListReq;
import org.group4.dvdshopbackend.models.member.dto.getMemberList.GetMemberListRes;
import org.group4.dvdshopbackend.models.member.dto.modifyMember.ModifyMemberReq;
import org.group4.dvdshopbackend.models.member.dto.modifyMember.ModifyMemberRes;
import org.group4.dvdshopbackend.models.member.dto.postMember.PostMemberReq;
import org.group4.dvdshopbackend.models.member.dto.postMember.PostMemberRes;
import org.group4.dvdshopbackend.models.member.repository.MemberJpaRepository;
import org.group4.dvdshopbackend.models.member.service.MemberService;
import org.group4.dvdshopbackend.security.auth.record.LoginUser;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final MemberJpaRepository memberJpaRepository; // 로그인 id → email 보정용(로그인 정보에 email 없을 수도 있어서)

    /* ===========================
       1) 회원가입 (누구나)
       =========================== */
    @PostMapping("/members")
    @Transactional
    public ApiResult<PostMemberRes> postMember(@RequestBody PostMemberReq reqBody) {
        var result = memberService.postMember(reqBody);
        return new ApiResult<>(result);
    }

    /* ===========================
       2) 회원 목록 (ADMIN 전용)
       =========================== */
    @GetMapping("/members")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResult<GetMemberListRes> getMemberList(
            @RequestParam(name = "page", required = false) Integer page,
            @RequestParam(name = "size", required = false) Integer size
    ) {
        page = (page == null || page < 1) ? 1 : page;
        size = (size == null || size < 1) ? 10 : size;

        var req = GetMemberListReq.builder()
                .page(page)
                .size(size)
                .build();

        var res = memberService.getMemberList(req);
        return new ApiResult<>(res);
    }

    /* ===========================
       3) 내 정보 보기 (로그인 필요)
       =========================== */
    @GetMapping("/me")
    @Transactional(readOnly = true)
    public ApiResult<GetMemberDetailRes> getMyInfo(@AuthenticationPrincipal LoginUser loginUser) {
        var req = GetMemberDetailReq.builder()
                .id(loginUser.id())
                .build();

        var res = memberService.getMemberDetail(req);
        return new ApiResult<>(res);
    }

    /* ===========================
       4) 내 정보 수정 (로그인 필요)
       - address / password
       =========================== */
    @PutMapping("/me")
    @Transactional
    public ApiResult<ModifyMemberRes> modifyMyInfo(
            @RequestBody ModifyMemberReq body,
            @AuthenticationPrincipal LoginUser loginUser
    ) {
        var req = ModifyMemberReq.builder()
                .id(loginUser.id())            // ★ 반드시 서버에서 본인 id로 덮어쓰기
                .address(body.getAddress())
                .password(body.getPassword())
                .build();

        var res = memberService.modifyMember(req);
        return new ApiResult<>(res);
    }

    /* ===========================
       5) 회원 탈퇴 (로그인 필요)
       - 비밀번호 확인 필수
       - 이메일은 서버가 주입 (프론트에서 조작 불가)
       =========================== */
    @DeleteMapping("/me")
    @Transactional
    public ApiResult<DeleteMemberRes> deleteMe(
            @AuthenticationPrincipal LoginUser loginUser,
            @RequestBody DeleteMemberReq body
    ) {
        String email = null;
        try {
            var emailGetter = LoginUser.class.getMethod("email");
            Object v = emailGetter.invoke(loginUser);
            if (v instanceof String ev && !ev.isBlank()) email = ev;
        } catch (Exception ignore) {}

        if (email == null) {
            email = memberJpaRepository.findById(loginUser.id())
                    .orElseThrow(() -> new IllegalArgumentException("회원 정보를 찾을 수 없습니다."))
                    .getEmail();
        }

        var req = DeleteMemberReq.builder()
                .email(email)
                .password(body.getPassword())
                .build();

        var res = memberService.deleteMember(req);
        return new ApiResult<>(res);
    }
}