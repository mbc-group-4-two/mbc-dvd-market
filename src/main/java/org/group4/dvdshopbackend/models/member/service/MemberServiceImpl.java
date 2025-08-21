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
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

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

        var members = new ArrayList<Member>();

        var pageable = request.getPageable("id");

        Page<Member> pageResult = memberJpaRepository.findAll(pageable);

        long totalElementCnt = pageResult.getTotalElements();
        int totalPageCnt = pageResult.getTotalPages();
        int pageNum = pageResult.getNumber();
        int pageSize = pageResult.getSize();

        boolean pageHasPrevious = pageResult.hasPrevious();
        boolean pageHasNext = pageResult.hasNext();
        boolean pageIsFirst = pageResult.isFirst();
        boolean pageIsLast = pageResult.isLast();

        pageResult.forEach(members::add);

        return GetMemberListRes.builder()
                .contents(members)
                .totalElementCnt(totalElementCnt)
                .totalPageCnt(totalPageCnt)
                .pageNum(pageNum)
                .pageSize(pageSize)
                .pageHasPrevious(pageHasPrevious)
                .pageHasNext(pageHasNext)
                .pageIsFirst(pageIsFirst)
                .pageIsLast(pageIsLast)
                .build();
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
    @Transactional
    public ModifyMemberRes modifyMember(ModifyMemberReq request) {

        var member = memberJpaRepository.findById(request.getId())
                .orElseThrow(() -> {
                    throw new RuntimeException();
                });

        if (request.getAddress() != null)
            member.setAddress(request.getAddress());

        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            member.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        return ModifyMemberRes.builder()
                .modified(member)
                .build();
    }

    @Override
    @Transactional
    public DeleteMemberRes deleteMember(DeleteMemberReq request) {

        Member member = memberJpaRepository
                .findByEmailAndDeletedYn(request.getEmail(), "N")
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않거나 이미 삭제된 회원입니다."));

        String password = request.getPassword();
        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("비밀번호를 입력해주세요.");
        }
        if (!passwordEncoder.matches(password, member.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        // 3) 소프트 삭제
        member.setDeletedYn("Y");

        return DeleteMemberRes.builder()
                .deleted(member)
                .build();
    }
}