package org.group4.dvdshopbackend.models.member.controller;

import lombok.RequiredArgsConstructor;
import org.group4.dvdshopbackend.core.api.ApiResult;
import org.group4.dvdshopbackend.models.member.dto.deleteMember.DeleteMemberReq;
import org.group4.dvdshopbackend.models.member.dto.deleteMember.DeleteMemberRes;
import org.group4.dvdshopbackend.models.member.dto.getMemberDetail.GetMemberDetailReq;
import org.group4.dvdshopbackend.models.member.dto.getMemberDetail.GetMemberDetailRes;
import org.group4.dvdshopbackend.models.member.dto.getMemberList.GetMemberListRes;
import org.group4.dvdshopbackend.models.member.dto.modifyMember.ModifyMemberReq;
import org.group4.dvdshopbackend.models.member.dto.modifyMember.ModifyMemberRes;
import org.group4.dvdshopbackend.models.member.dto.postMember.PostMemberReq;
import org.group4.dvdshopbackend.models.member.dto.postMember.PostMemberRes;
import org.group4.dvdshopbackend.models.member.service.MemberService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    // 회원가입
    @PostMapping("/members")
    public ApiResult<PostMemberRes> postMember(
            @RequestBody PostMemberReq reqBody) {

        var result = memberService.postMember(reqBody);

        return new ApiResult<>(result);

    }

    //  회원 목록 보기(관리자용)
    @GetMapping("/members")
    @ResponseBody
    public ApiResult<GetMemberListRes> getMemberList() {

        return null;
    }

    // 내정보 보기(일반 유저용)
    @GetMapping("/members/{memberId}")
    @ResponseBody
    @Transactional
    public ApiResult<GetMemberDetailRes> getMemberDetail(
            @PathVariable("memberId") Long memberId) {

        var request = GetMemberDetailReq.builder()
                .id(memberId)
                .build();

        var result = memberService.getMemberDetail(request);

        return new ApiResult<>(result);
    }

    // 회원정보 수정하기
    @PutMapping("/members")
    @ResponseBody
    @Transactional
    public ApiResult<ModifyMemberRes> modifyMember(
            @RequestBody ModifyMemberReq request) {

        var result = memberService.modifyMember(request);

        return new ApiResult<>(result);
    }

    // 회원탈퇴
    @DeleteMapping("/members")
    @ResponseBody
    @Transactional
    public ApiResult<DeleteMemberRes> deleteMember(
            @RequestBody DeleteMemberReq request) {

        var result = memberService.deleteMember(request);

        return new ApiResult<>(result);
    }

}
