package com.nextu.mesdevis.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nextu.mesdevis.dto.MemberDto;
import com.nextu.mesdevis.entity.Category;
import com.nextu.mesdevis.entity.Estimate;
import com.nextu.mesdevis.entity.Member;
import com.nextu.mesdevis.repository.CategoryRepository;
import com.nextu.mesdevis.repository.EstimateRepository;
import com.nextu.mesdevis.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MemberService {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private EstimateRepository estimateRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private MemberAuthenticationService memberAuthenticationService;

    public List<MemberDto> getAllMembers() {
        String roleMember = memberAuthenticationService.findMemberRole();
        if (Objects.equals(roleMember, "ADMIN") || Objects.equals(roleMember, "MEMBER")){
            System.out.println("roleMember: " + roleMember);
        }
        List<Member> members = memberRepository.findAll();
        return members.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    public MemberDto getMemberById(Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Member not found with id: " + id));
        return convertToDto(member);
    }

    public MemberDto createMember(MemberDto memberDto) {
        Member member = convertToEntity(memberDto);
        Member savedMember = memberRepository.save(member);
        return convertToDto(savedMember);
    }

    public MemberDto updateMember(Long id, MemberDto memberDto) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Member not found with id: " + id));

        member.setFirstname(memberDto.getFirstname());
        member.setLastname(memberDto.getLastname());
        member.setRole(memberDto.getRole());
        member.setEmail(memberDto.getEmail());

        if (memberDto.getEstimateIds() != null && !memberDto.getEstimateIds().isEmpty()) {
            List<Estimate> newEstimates = estimateRepository.findAllById(memberDto.getEstimateIds());
            member.getEstimates().addAll(newEstimates);
        }

        if (memberDto.getCategoryIds() != null && !memberDto.getCategoryIds().isEmpty()) {
            List<Category> newCategories = categoryRepository.findAllById(memberDto.getCategoryIds());
            member.getCategories().addAll(newCategories);
        }

        Member updatedMember = memberRepository.save(member);
        return convertToDto(updatedMember);
    }

    public void deleteMember(Long id) {
        memberRepository.deleteById(id);
    }

    private MemberDto convertToDto(Member member) {
        List<Long> estimateIds = Optional.ofNullable(member.getEstimates())
                .orElse(Collections.emptyList())
                .stream()
                .map(Estimate::getIdEstimate)
                .collect(Collectors.toList());

        List<Long> categoryIds = Optional.ofNullable(member.getCategories())
                .orElse(Collections.emptyList())
                .stream()
                .map(Category::getIdCategory)
                .collect(Collectors.toList());

        return new MemberDto(
                member.getIdMember(),
                member.getFirstname(),
                member.getLastname(),
                member.getRole(),
                member.getEmail(),
                estimateIds,
                categoryIds
        );
    }

    private Member convertToEntity(MemberDto memberDto) {
        Member member = new Member(
                memberDto.getFirstname(),
                memberDto.getLastname(),
                memberDto.getRole(),
                memberDto.getEmail()
        );

        if (memberDto.getEstimateIds() != null && !memberDto.getEstimateIds().isEmpty()) {
            List<Estimate> estimates = estimateRepository.findAllById(memberDto.getEstimateIds());
            member.setEstimates(estimates);
        }

        if (memberDto.getCategoryIds() != null && !memberDto.getCategoryIds().isEmpty()) {
            List<Category> categories = categoryRepository.findAllById(memberDto.getCategoryIds());
            member.setCategories(categories);
        }

        return member;
    }

}
