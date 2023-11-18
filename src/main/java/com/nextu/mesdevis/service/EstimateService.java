package com.nextu.mesdevis.service;

import com.nextu.mesdevis.dto.EstimateDto;
import com.nextu.mesdevis.entity.Client;
import com.nextu.mesdevis.entity.Estimate;
import com.nextu.mesdevis.entity.Member;
import com.nextu.mesdevis.entity.ProductXEstimate;
import com.nextu.mesdevis.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
@Service
public class EstimateService {
    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private EstimateRepository estimateRepository;

    @Autowired
    private ProductXEstimateRepository productXEstimateRepository;

    @Autowired
    private MemberRepository memberRepository;

    public List<EstimateDto> getAllEstimates() {
        List<Estimate> estimates = estimateRepository.findAll();
        return estimates.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    public EstimateDto getEstimateById(Long id) {
        Estimate estimate = estimateRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Estimate not found with id: " + id));
        return convertToDto(estimate);
    }

    public EstimateDto createEstimate(EstimateDto estimateDto) {
        Estimate estimate = convertToEntity(estimateDto);
        estimate.setCreationDate(LocalDate.now());
        Estimate savedEstimate = estimateRepository.save(estimate);
        return convertToDto(savedEstimate);
    }

    public EstimateDto updateEstimate(Long id, EstimateDto estimateDto) {
        Estimate estimate = estimateRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Estimate not found with id: " + id));

        estimate.setCreationDate(estimateDto.getCreationDate());
        estimate.setValidationDate(estimateDto.getValidationDate());
        estimate.setPaymentDate(estimateDto.getPaymentDate());
        estimate.setCancellationDate(estimateDto.getCancellationDate());

        if (estimateDto.getClientId() != 0) {
            Client client = clientRepository.findById(estimateDto.getClientId())
                    .orElseThrow(() -> new RuntimeException("Client not found with id: " + estimateDto.getClientId()));
            estimate.setClient(client);
        }

        if (estimateDto.getProductXEstimateIds() != null && !estimateDto.getProductXEstimateIds().isEmpty()) {
            List<ProductXEstimate> newProductsXEstimates = productXEstimateRepository.findAllById(estimateDto.getProductXEstimateIds());
            estimate.getProductsXEstimates().addAll(newProductsXEstimates);
        }

        if (estimateDto.getMemberId() != 0) {
            Member member = memberRepository.findById(estimateDto.getMemberId())
                    .orElseThrow(() -> new RuntimeException("Member not found with id: " + estimateDto.getMemberId()));
            estimate.setMember(member);
        }

        Estimate updatedEstimate = estimateRepository.save(estimate);
        return convertToDto(updatedEstimate);
    }

    public void deleteEstimate(Long id) {
        estimateRepository.deleteById(id);
    }

    private EstimateDto convertToDto(Estimate estimate) {
        List<Long> productXEstimateIds = new ArrayList<>();

        if (estimate.getProductsXEstimates() != null) {
            productXEstimateIds = estimate.getProductsXEstimates()
                    .stream()
                    .map(ProductXEstimate::getIdProductXEstimate)
                    .collect(Collectors.toList());
        }

        return new EstimateDto(
                estimate.getIdEstimate(),
                estimate.getCreationDate(),
                estimate.getValidationDate(),
                estimate.getPaymentDate(),
                estimate.getCancellationDate(),
                estimate.getClient().getIdClient(),
                productXEstimateIds,
                estimate.getMember().getIdMember()
        );
    }


    private Estimate convertToEntity(EstimateDto estimateDto) {
        Estimate estimate = new Estimate(
                estimateDto.getCreationDate(),
                estimateDto.getValidationDate(),
                estimateDto.getPaymentDate(),
                estimateDto.getCancellationDate()
        );

        if (estimateDto.getClientId() != 0) {
            Client client = clientRepository.findById(estimateDto.getClientId())
                    .orElseThrow(() -> new RuntimeException("Client not found with id: " + estimateDto.getClientId()));
            estimate.setClient(client);
        }

        if (estimateDto.getMemberId() != 0) {
            Member member = memberRepository.findById(estimateDto.getMemberId())
                    .orElseThrow(() -> new RuntimeException("Member not found with id: " + estimateDto.getMemberId()));
            estimate.setMember(member);
        }

        if (estimateDto.getProductXEstimateIds() != null && !estimateDto.getProductXEstimateIds().isEmpty()) {
            List<ProductXEstimate> productsXEstimates = productXEstimateRepository.findAllById(estimateDto.getProductXEstimateIds());
            estimate.setProductsXEstimates(productsXEstimates);
        }

        return estimate;
    }

}
