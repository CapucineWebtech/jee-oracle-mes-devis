package com.nextu.mesdevis.service;

import static org.junit.jupiter.api.Assertions.*;

import com.nextu.mesdevis.dto.MemberDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.List;
@SpringBootTest
public class MemberServiceTest {

    @Autowired
    private MemberService memberService;

    @Test
    void testMember() {
        // Given
        MemberDto memberDto = new MemberDto();
        memberDto.setFirstname("testjee");
        memberDto.setLastname("testjee");
        memberDto.setRole("ADMIN");
        memberDto.setEmail("testjee@gmail.c");

        String newFirstname = "renamed";

        // When
        MemberDto creatResult = memberService.createMember(memberDto);

        MemberDto resultToUse = new MemberDto();
        resultToUse.setIdMember(creatResult.getIdMember());
        resultToUse.setFirstname(creatResult.getFirstname());
        resultToUse.setLastname(creatResult.getLastname());
        resultToUse.setRole(creatResult.getRole());
        resultToUse.setEmail(creatResult.getEmail());

        List<MemberDto> getAllResult = memberService.getAllMembers();

        MemberDto getByIdResult = memberService.getMemberById(resultToUse.getIdMember());

        resultToUse.setFirstname(newFirstname);
        MemberDto updateResult = memberService.updateMember(resultToUse.getIdMember(), resultToUse);

        // Then
        assertNotNull(creatResult);
        assertEquals(memberDto.getFirstname(), creatResult.getFirstname());
        assertNotNull(getAllResult);
        assertNotNull(getByIdResult);
        assertEquals(creatResult.getFirstname(), getByIdResult.getFirstname());
        assertNotNull(updateResult);
        assertEquals(newFirstname, updateResult.getFirstname());
    }
}
