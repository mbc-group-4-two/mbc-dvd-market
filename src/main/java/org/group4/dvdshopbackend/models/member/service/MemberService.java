package org.group4.dvdshopbackend.models.member.service;

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

public interface MemberService {

    PostMemberRes postMember(PostMemberReq request);

    GetMemberListRes getMemberList(GetMemberListReq request);

    GetMemberDetailRes getMemberDetail(GetMemberDetailReq request);

    ModifyMemberRes modifyMember(ModifyMemberReq request);

    DeleteMemberRes deleteMember(DeleteMemberReq request);

}
