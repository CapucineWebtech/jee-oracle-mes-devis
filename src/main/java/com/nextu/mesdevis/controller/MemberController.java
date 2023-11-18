package com.nextu.mesdevis.controller;

import com.nextu.mesdevis.dto.ClientDto;
import com.nextu.mesdevis.dto.MemberDto;
import com.nextu.mesdevis.service.MemberAuthenticationService;
import com.nextu.mesdevis.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/members")
public class MemberController {

    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberAuthenticationService memberAuthenticationService;

    @GetMapping
    public ResponseEntity<List<MemberDto>> getAllMembers() {
        String roleMember = memberAuthenticationService.findMemberRole();
        if (Objects.equals(roleMember, "ADMIN") || Objects.equals(roleMember, "MEMBER")) {
            List<MemberDto> members = memberService.getAllMembers();
            return new ResponseEntity<>(members, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<MemberDto> getMemberById(@PathVariable Long id) {
        String roleMember = memberAuthenticationService.findMemberRole();
        if (Objects.equals(roleMember, "ADMIN") || Objects.equals(roleMember, "MEMBER")) {
            MemberDto member = memberService.getMemberById(id);
            return new ResponseEntity<>(member, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @PostMapping
    public ResponseEntity<MemberDto> createMember(@RequestBody MemberDto memberDto) {
        String roleMember = memberAuthenticationService.findMemberRole();
        if (Objects.equals(roleMember, "ADMIN") || Objects.equals(roleMember, "MEMBER")) {
            MemberDto createdMember = memberService.createMember(memberDto);
            return new ResponseEntity<>(createdMember, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<MemberDto> updateMember(@PathVariable Long id, @RequestBody MemberDto memberDto) {
        String roleMember = memberAuthenticationService.findMemberRole();
        Long idMember = memberAuthenticationService.findMemberId();
        if (Objects.equals(roleMember, "ADMIN") || (Objects.equals(roleMember, "MEMBER") && idMember.equals(memberDto.getIdMember()))) {
            MemberDto updatedMember = memberService.updateMember(id, memberDto);
            return new ResponseEntity<>(updatedMember, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMember(@PathVariable Long id) {
        String roleMember = memberAuthenticationService.findMemberRole();
        if (Objects.equals(roleMember, "ADMIN")) {
            memberService.deleteMember(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }
}