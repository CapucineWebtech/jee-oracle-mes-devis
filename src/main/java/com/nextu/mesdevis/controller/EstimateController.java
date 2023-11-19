package com.nextu.mesdevis.controller;

import com.nextu.mesdevis.dto.DevelopedEstimateDto;
import com.nextu.mesdevis.dto.EstimateDto;
import com.nextu.mesdevis.dto.ProductXEstimateDto;
import com.nextu.mesdevis.service.ClientAuthenticationService;
import com.nextu.mesdevis.service.EstimateService;
import com.nextu.mesdevis.service.MemberAuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
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
    public ResponseEntity<List<DevelopedEstimateDto>> getAllEstimates(
            @RequestParam(name = "validated", required = false) boolean validated,
            @RequestParam(name = "paid", required = false) boolean paid,
            @RequestParam(name = "canceled", required = false) boolean canceled,
            @RequestParam(name = "before_date", required = false) LocalDate before_date,
            @RequestParam(name = "after_date", required = false) LocalDate after_date,
            @RequestParam(name = "all", required = false) boolean allEstimateMember) {

        String roleMember = memberAuthenticationService.findMemberRole();
        long idMember = memberAuthenticationService.findMemberId();
        if (Objects.equals(roleMember, "ADMIN") || Objects.equals(roleMember, "MEMBER")) {
            List<DevelopedEstimateDto> estimates = estimateService.getAllEstimates(validated, paid, canceled,before_date, after_date, idMember, roleMember.equals("ADMIN"), allEstimateMember);
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
    public ResponseEntity<EstimateDto> createEstimate(@RequestBody EstimateAndListProductXEstimate estimateRequest) {
        EstimateDto createdEstimate = estimateService.createCompleteEstimate(estimateRequest.getEstimateDto(), estimateRequest.getProductXEstimateDtos());
        return new ResponseEntity<>(createdEstimate, HttpStatus.CREATED);
    }

    @PostMapping("/{id}")
    public ResponseEntity<EstimateDto> updateEstimate(@PathVariable Long id, @RequestBody NewProductXEstimateAndDeleteProductXEstimate newProductXEstimateAndDeleteProductXEstimate) {
        String roleMember = memberAuthenticationService.findMemberRole();
        if (Objects.equals(roleMember, "ADMIN") || Objects.equals(roleMember, "MEMBER")) {
            EstimateDto updatedEstimate = estimateService.updateEstimate(id, newProductXEstimateAndDeleteProductXEstimate.getNewProductXEstimateDtos(), newProductXEstimateAndDeleteProductXEstimate.getOldProductXEstimateId());
            return new ResponseEntity<>(updatedEstimate, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @PostMapping("/validate")
    public ResponseEntity<List<EstimateDto>> validateEstimate(@RequestBody List<Long> estimateIds) {
        String roleMember = memberAuthenticationService.findMemberRole();
        if (Objects.equals(roleMember, "ADMIN") || Objects.equals(roleMember, "MEMBER")) {
            List<EstimateDto> updatedEstimates = estimateService.validateEstimates(estimateIds);
            return new ResponseEntity<>(updatedEstimates, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @PostMapping("/payed")
    public ResponseEntity<List<EstimateDto>> payedEstimate(@RequestBody List<Long> estimateIds) {
        String roleMember = memberAuthenticationService.findMemberRole();
        if (Objects.equals(roleMember, "ADMIN") || Objects.equals(roleMember, "MEMBER")) {
            List<EstimateDto> updatedEstimates = estimateService.paymentEstimates(estimateIds);
            return new ResponseEntity<>(updatedEstimates, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @PostMapping("/cancel")
    public ResponseEntity<List<EstimateDto>> cancelEstimate(@RequestBody List<Long> estimateIds) {
        String roleMember = memberAuthenticationService.findMemberRole();
        if (Objects.equals(roleMember, "ADMIN") || Objects.equals(roleMember, "MEMBER")) {
            List<EstimateDto> updatedEstimates = estimateService.cancelEstimates(estimateIds);
            return new ResponseEntity<>(updatedEstimates, HttpStatus.OK);
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

    public static class EstimateAndListProductXEstimate {
        private EstimateDto estimateDto;
        private List<ProductXEstimateDto> productXEstimateDtos;

        public EstimateAndListProductXEstimate() {
        }

        public EstimateAndListProductXEstimate(EstimateDto estimateDto, List<ProductXEstimateDto> productXEstimateDtos) {
            this.estimateDto = estimateDto;
            this.productXEstimateDtos = productXEstimateDtos;
        }

        public EstimateDto getEstimateDto() {
            return estimateDto;
        }

        public void setEstimateDto(EstimateDto estimateDto) {
            this.estimateDto = estimateDto;
        }

        public List<ProductXEstimateDto> getProductXEstimateDtos() {
            return productXEstimateDtos;
        }

        public void setProductXEstimateDtos(List<ProductXEstimateDto> productXEstimateDtos) {
            this.productXEstimateDtos = productXEstimateDtos;
        }
    }

    public static class NewProductXEstimateAndDeleteProductXEstimate {
        private List<ProductXEstimateDto> newProductXEstimateDtos;
        private List<Long> oldProductXEstimateId;

        public NewProductXEstimateAndDeleteProductXEstimate() {
        }

        public NewProductXEstimateAndDeleteProductXEstimate(List<ProductXEstimateDto> newProductXEstimateDtos, List<Long> oldProductXEstimateId) {
            this.newProductXEstimateDtos = newProductXEstimateDtos;
            this.oldProductXEstimateId = oldProductXEstimateId;
        }

        public List<ProductXEstimateDto> getNewProductXEstimateDtos() {
            return newProductXEstimateDtos;
        }

        public void setNewProductXEstimateDtos(List<ProductXEstimateDto> newProductXEstimateDtos) {
            this.newProductXEstimateDtos = newProductXEstimateDtos;
        }

        public List<Long> getOldProductXEstimateId() {
            return oldProductXEstimateId;
        }

        public void setOldProductXEstimateId(List<Long> oldProductXEstimateId) {
            this.oldProductXEstimateId = oldProductXEstimateId;
        }
    }
}
