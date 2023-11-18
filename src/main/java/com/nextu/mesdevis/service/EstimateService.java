package com.nextu.mesdevis.service;

import com.nextu.mesdevis.dto.EstimateDto;
import com.nextu.mesdevis.dto.ProductXEstimateDto;
import com.nextu.mesdevis.entity.*;
import com.nextu.mesdevis.repository.*;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
@Service
public class EstimateService {
    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private EstimateRepository estimateRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductXEstimateRepository productXEstimateRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductXEstimateService productXEstimateService;

    @Autowired
    private PriceService priceService;

    @Autowired
    private MemberAuthenticationService memberAuthenticationService;

    public List<EstimateDto> getAllEstimates() {
        List<Estimate> estimates = estimateRepository.findAll();
        return estimates.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    public EstimateDto getEstimateById(Long id) {
        Estimate estimate = estimateRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Estimate not found with id: " + id));
        return convertToDto(estimate);
    }

    public EstimateDto createCompleteEstimate(EstimateDto estimateDto, List<ProductXEstimateDto> productXEstimateDtos) {
        long categoryId = validateProductAvailability(productXEstimateDtos);
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + categoryId));

        Estimate estimate = convertToEntity(estimateDto);
        estimate.setCreationDate(LocalDate.now());
        estimate.setMember(category.getMember());
        Estimate savedEstimate = estimateRepository.save(estimate);

        EstimateDto savedEstimateDto = convertToDto(savedEstimate);
        float totalPrice = 0;
        for (ProductXEstimateDto productXEstimateDto : productXEstimateDtos) {
            productXEstimateDto.setEstimateId(savedEstimate.getIdEstimate());
            productXEstimateDto.setPrice(priceService.findProductPrice(productXEstimateDto.getProductId()));
            ProductXEstimateDto newProductXEstimateDto = productXEstimateService.createProductXEstimate(productXEstimateDto);

            savedEstimateDto.getProductXEstimateIds().add(newProductXEstimateDto.getIdProductXEstimate());

            Product product = productRepository.findById(productXEstimateDto.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found with id: " + productXEstimateDto.getProductId()));
            product.setInventory(product.getInventory() - newProductXEstimateDto.getInventory());
            productRepository.save(product);
            totalPrice += newProductXEstimateDto.getPrice() * newProductXEstimateDto.getInventory();
        }

        if (totalPrice > 10000) {
            Member adminMember = memberRepository.findByRole("ADMIN");

            if (adminMember == null) {
                throw new RuntimeException("Admin member not found");
            }

            savedEstimate.setMember(adminMember);
            estimateRepository.save(savedEstimate);
            savedEstimateDto.setMemberId(adminMember.getIdMember());
        }

        return savedEstimateDto;
    }

    private Long validateProductAvailability(List<ProductXEstimateDto> productXEstimateDtos) {
        Map<Long, Long> categoryInventorySum = new HashMap<>();
        Long maxCategoryId = null;
        Long maxInventorySum = Long.MIN_VALUE;

        for (ProductXEstimateDto productXEstimateDto : productXEstimateDtos) {
            Product product = productRepository.findById(productXEstimateDto.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found with id: " + productXEstimateDto.getProductId()));

            if (product == null || product.getInventory() < productXEstimateDto.getInventory()) {
                throw new RuntimeException("Product not available with sufficient stock.");
            }

            Long categoryId = product.getCategory().getIdCategory();
            Long inventory = productXEstimateDto.getInventory();
            Long currentInventorySum = categoryInventorySum.getOrDefault(categoryId, 0L) + inventory;
            categoryInventorySum.put(categoryId, currentInventorySum);

            if (currentInventorySum > maxInventorySum) {
                maxInventorySum = currentInventorySum;
                maxCategoryId = categoryId;
            }
        }
        return maxCategoryId;
    }


    public List<EstimateDto> validateEstimates(List<Long> estimateIds) {
        List<Estimate> estimates = estimateRepository.findByIds(estimateIds);
        if (estimates.isEmpty()) {
            throw new RuntimeException("Estimates not found with ids: " + estimateIds);
        }

        String roleMember = memberAuthenticationService.findMemberRole();
        Long idMember = memberAuthenticationService.findMemberId();
        for (Estimate estimate : estimates) {
            if ((Objects.equals(roleMember, "ADMIN") || idMember.equals(estimate.getMember().getIdMember())) && estimate.getValidationDate() == null) {
                estimate.setValidationDate(LocalDate.now());
                estimateRepository.save(estimate);
            }
        }

        return convertToDtoList(estimates);
    }

    public List<EstimateDto> paymentEstimates(List<Long> estimateIds) {
        List<Estimate> estimates = estimateRepository.findByIds(estimateIds);
        if (estimates.isEmpty()) {
            throw new RuntimeException("Estimates not found with ids: " + estimateIds);
        }

        String roleMember = memberAuthenticationService.findMemberRole();
        Long idMember = memberAuthenticationService.findMemberId();
        for (Estimate estimate : estimates) {
            if ((Objects.equals(roleMember, "ADMIN") || idMember.equals(estimate.getMember().getIdMember())) && estimate.getPaymentDate() == null && estimate.getValidationDate() != null) {
                estimate.setPaymentDate(LocalDate.now());
                estimateRepository.save(estimate);
            }
        }

        return convertToDtoList(estimates);
    }

    public List<EstimateDto> cancelEstimates(List<Long> estimateIds) {
        List<Estimate> estimates = estimateRepository.findByIds(estimateIds);
        if (estimates.isEmpty()) {
            throw new RuntimeException("Estimates not found with ids: " + estimateIds);
        }

        String roleMember = memberAuthenticationService.findMemberRole();
        Long idMember = memberAuthenticationService.findMemberId();
        for (Estimate estimate : estimates) {
            if ((Objects.equals(roleMember, "ADMIN") || idMember.equals(estimate.getMember().getIdMember())) && estimate.getCancellationDate() == null) {
                estimate.setCancellationDate(LocalDate.now());
                estimateRepository.save(estimate);
            }
        }

        return convertToDtoList(estimates);
    }


    public EstimateDto updateEstimate(Long id, List<ProductXEstimateDto> newProductXEstimateDtos, List<Long> oldProductXEstimateIds) {
        Estimate estimate = estimateRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Estimate not found with id: " + id));

        List<Long> currentProductXEstimateIds = estimate.getProductsXEstimates()
                .stream()
                .map(ProductXEstimate::getIdProductXEstimate)
                .toList();

        List<Long> validOldProductXEstimateIds = oldProductXEstimateIds
                .stream()
                .filter(currentProductXEstimateIds::contains)
                .toList();

        for (Long validOldProductXEstimateId : validOldProductXEstimateIds) {
            ProductXEstimate productXEstimate = productXEstimateRepository.findById(validOldProductXEstimateId)
                    .orElseThrow(() -> new RuntimeException("ProductXEstimate not found with id: " + validOldProductXEstimateId));
            Product product = productXEstimate.getProduct();
            product.setInventory(product.getInventory() + productXEstimate.getInventory());
            productXEstimateRepository.deleteById(validOldProductXEstimateId);
            productRepository.save(product);
        }

        for (ProductXEstimateDto newProductXEstimateDto : newProductXEstimateDtos) {
            Long idProductXEstimate = newProductXEstimateDto.getIdProductXEstimate();
            if (currentProductXEstimateIds.contains(idProductXEstimate)) {
                ProductXEstimate existingProductXEstimate = productXEstimateRepository.findById(idProductXEstimate)
                        .orElseThrow(() -> new RuntimeException("ProductXEstimate not found with id: " + idProductXEstimate));
                if (newProductXEstimateDto.getPrice() != 0){
                    existingProductXEstimate.setPrice(newProductXEstimateDto.getPrice());
                }
                Product product = existingProductXEstimate.getProduct();
                if (newProductXEstimateDto.getInventory() != 0){
                    long oldInventoryPXE = existingProductXEstimate.getInventory();
                    long newInventoryPXE = newProductXEstimateDto.getInventory();
                    long productInventory = product.getInventory();
                    product.setInventory(productInventory + (oldInventoryPXE - newInventoryPXE));

                    existingProductXEstimate.setInventory(newProductXEstimateDto.getInventory());
                }
                productXEstimateRepository.save(existingProductXEstimate);
                productRepository.save(product);
            } else {
                if (newProductXEstimateDto.getInventory() != 0){
                    ProductXEstimate newProductXEstimate = new ProductXEstimate();
                    newProductXEstimate.setInventory(newProductXEstimateDto.getInventory());
                    Product product = productRepository.findById(newProductXEstimateDto.getProductId())
                            .orElseThrow(() -> new RuntimeException("Product not found with id: " + newProductXEstimateDto.getProductId()));
                    newProductXEstimate.setProduct(product);
                    newProductXEstimate.setEstimate(estimate);
                    if (newProductXEstimateDto.getPrice() != 0){
                        newProductXEstimate.setPrice(newProductXEstimateDto.getPrice());
                    }else {
                        newProductXEstimate.setPrice(priceService.findProductPrice(product.getIdProduct()));
                    }
                    productXEstimateRepository.save(newProductXEstimate);
                    product.setInventory(product.getInventory() - newProductXEstimateDto.getInventory());
                    productRepository.save(product);
                }else {
                    throw new RuntimeException("No inventory : no productXEstimate.");
                }
            }
        }

        return convertToDto(estimate);
    }

    public void deleteEstimate(Long id) {
        Optional<Estimate> optionalEstimate = estimateRepository.findById(id);
        if (optionalEstimate.isPresent()) {
            Estimate estimate = optionalEstimate.get();
            List<ProductXEstimate> productXEstimates = estimate.getProductsXEstimates();
            for (ProductXEstimate productXEstimate : productXEstimates) {
                productXEstimateRepository.deleteById(productXEstimate.getIdProductXEstimate());
            }
            estimateRepository.deleteById(id);
        } else {
            throw new EntityNotFoundException("Estimate with ID " + id + " not found");
        }
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

    public List<EstimateDto> convertToDtoList(List<Estimate> estimates) {
        return estimates.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
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
