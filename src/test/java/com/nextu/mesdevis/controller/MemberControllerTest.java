package com.nextu.mesdevis.controller;

import com.nextu.mesdevis.dto.MemberDto;
import com.nextu.mesdevis.service.MemberAuthenticationService;
import com.nextu.mesdevis.service.MemberService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MemberController.class)
public class MemberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MemberService memberService;

    @MockBean
    private MemberAuthenticationService memberAuthenticationService;

    @Test
    public void testGetAllMembers() throws Exception {
        // Mocking the behavior of your service
        MemberDto memberDto = new MemberDto();
        memberDto.setFirstname("testjee");
        memberDto.setLastname("testjee");
        memberDto.setRole("ADMIN");
        memberDto.setEmail("testjee@gmail.c");

        Mockito.when(memberService.getAllMembers()).thenReturn(List.of(memberDto));

        // Mocking the behavior of your authentication service
        Mockito.when(memberAuthenticationService.findMemberRole()).thenReturn("ADMIN");

        // Performing the request and asserting the response
        mockMvc.perform(get("/api/members")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].firstname").value("testjee"))
                .andExpect(jsonPath("$[0].lastname").value("testjee"))
                .andExpect(jsonPath("$[0].role").value("ADMIN"))
                .andExpect(jsonPath("$[0].email").value("testjee@gmail.c"));
    }

    @Test
    public void testGetAllMembersForbidden() throws Exception {
        // Mocking the behavior of your authentication service
        Mockito.when(memberAuthenticationService.findMemberRole()).thenReturn("SOME_OTHER_ROLE");

        // Performing the request and asserting the response
        mockMvc.perform(get("/api/members")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }
}
