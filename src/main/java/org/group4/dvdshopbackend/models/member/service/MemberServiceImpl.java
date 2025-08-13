package org.group4.dvdshopbackend.models.member.service;

import lombok.RequiredArgsConstructor;
import org.group4.dvdshopbackend.common.entity.Member;
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
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberJpaRepository memberJpaRepository;

    private final PasswordEncoder passwordEncoder;

    @Override
    public PostMemberRes postMember(PostMemberReq request) {

        ModelMapper modelMapper = new ModelMapper();

        if (memberJpaRepository.existsByEmailAndDeletedYn(request.getEmail(), "N")) {
            throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
        }

        var member = modelMapper.map(request, Member.class);

        member.setPassword(passwordEncoder.encode(request.getPassword()));

        member = memberJpaRepository.save(member);

        return PostMemberRes.builder()
                .id(member.getId())
                .name(member.getName())
                .email(member.getEmail())
                .address(member.getAddress())
                .build();
    }

    @Override
    public GetMemberListRes getMemberList(GetMemberListReq request) {
        return null;
    }

    @Override
    public GetMemberDetailRes getMemberDetail(GetMemberDetailReq request) {

        var member = memberJpaRepository.findById(request.getId())
                .orElseThrow(() -> {
                    throw new RuntimeException();
                });

        return GetMemberDetailRes.builder()
                .detail(member)
                .build();
    }

    @Override
    public ModifyMemberRes modifyMember(ModifyMemberReq request) {

        var member = memberJpaRepository.findById(request.getId())
                .orElseThrow(() -> {
                    throw new RuntimeException();
                });

        if (request.getAddress() != null)
            member.setAddress(request.getAddress());

        if (request.getPassword() != null)
            member.setPassword(request.getPassword());

        return ModifyMemberRes.builder()
                .modified(member)
                .build();
    }

    @Override
    public DeleteMemberRes deleteMember(DeleteMemberReq request) {

        Member member = memberJpaRepository.findByEmailAndDeletedYn(request.getEmail(), "n")
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않거나 이미 삭제된 회원입니다."));

        if (!passwordEncoder.matches(request.getPassword(), member.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        member.setDeletedYn("Y");

        return DeleteMemberRes.builder()
                .deleted(member)
                .build();
    }
}
