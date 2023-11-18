package com.nextu.mesdevis.controller;

import com.nextu.mesdevis.dto.EstimateDto;
import com.nextu.mesdevis.service.ClientAuthenticationService;
import com.nextu.mesdevis.service.EstimateService;
import com.nextu.mesdevis.service.MemberAuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/estimates")
public class EstimateController {

    @Autowired
    private EstimateService estimateService;

    @Autowired
    private MemberAuthenticationService memberAuthenticationService;

    @Autowired
    private ClientAuthenticationService clientAuthenticationService;

    @GetMapping
    public ResponseEntity<List<EstimateDto>> getAllEstimates() {
        String roleMember = memberAuthenticationService.findMemberRole();
        if (Objects.equals(roleMember, "ADMIN") || Objects.equals(roleMember, "MEMBER")) {
            List<EstimateDto> estimates = estimateService.getAllEstimates();
            return new ResponseEntity<>(estimates, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<EstimateDto> getEstimateById(@PathVariable Long id) {
        String roleMember = memberAuthenticationService.findMemberRole();
        long authenticatedClient = clientAuthenticationService.isClientExist();
        EstimateDto estimate = estimateService.getEstimateById(id);
        if (Objects.equals(roleMember, "ADMIN") || Objects.equals(roleMember, "MEMBER") || authenticatedClient == estimate.getClientId()) {
            return new ResponseEntity<>(estimate, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @PostMapping
    public ResponseEntity<EstimateDto> createEstimate(@RequestBody EstimateDto estimateDto) {
        String roleMember = memberAuthenticationService.findMemberRole();
        if (Objects.equals(roleMember, "ADMIN") || Objects.equals(roleMember, "MEMBER")) {
            EstimateDto createdEstimate = estimateService.createEstimate(estimateDto);
            return new ResponseEntity<>(createdEstimate, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<EstimateDto> updateEstimate(@PathVariable Long id, @RequestBody EstimateDto estimateDto) {
        String roleMember = memberAuthenticationService.findMemberRole();
        if (Objects.equals(roleMember, "ADMIN") || Objects.equals(roleMember, "MEMBER")) {
            EstimateDto updatedEstimate = estimateService.updateEstimate(id, estimateDto);
            return new ResponseEntity<>(updatedEstimate, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEstimate(@PathVariable Long id) {
        String roleMember = memberAuthenticationService.findMemberRole();
        if (Objects.equals(roleMember, "ADMIN") || Objects.equals(roleMember, "MEMBER")) {
            estimateService.deleteEstimate(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }
}
