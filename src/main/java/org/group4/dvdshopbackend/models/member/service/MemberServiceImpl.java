package org.group4.dvdshopbackend.models.member.service;

import lombok.RequiredArgsConstructor;
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
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberJpaRepository memberJpaRepository;

    @Override
    public PostMemberRes postMember(PostMemberReq request) {
        return null;
    }

    @Override
    public GetMemberListRes getMemberList(GetMemberListReq request) {
        return null;
    }

    @Override
    public GetMemberDetailRes getMemberDetail(GetMemberDetailReq request) {
        return null;
    }

    @Override
    public ModifyMemberRes modifyMember(ModifyMemberReq request) {
        return null;
    }

    @Override
    public DeleteMemberRes deleteMember(DeleteMemberReq request) {
        return null;
    }
}
