package com.nextu.mesdevis.service;

import com.nextu.mesdevis.dto.ProductDto;
import com.nextu.mesdevis.entity.Category;
import com.nextu.mesdevis.entity.Product;
import com.nextu.mesdevis.repository.CategoryRepository;
import com.nextu.mesdevis.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Service gérant les opérations liées aux produits.
 */
@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private PriceService priceService;

    /**
     * Récupère tous les produits en fonction des filtres spécifiés.
     *
     * @param startStr  L'ID de début pour la plage d'ID.
     * @param endStr    L'ID de fin pour la plage d'ID.
     * @param name      Le nom du produit à filtrer.
     * @param code      Le code du produit à filtrer.
     * @param category  L'ID de la catégorie à filtrer.
     * @return La liste des produits filtrés sous forme de DTO avec les prix récupérer avec l'API.
     */
    public List<ProductDto> getAllProductsFilter(String startStr, String endStr, String name, String code, String category) {
        List<Product> products = productRepository.findAll();
        List<Long> productIds = products.stream()
                .map(Product::getIdProduct)
                .filter(id -> filterByIdRange(id, startStr, endStr))
                .filter(id -> filterByName(products, id, name))
                .filter(id -> filterByCode(products, id, code))
                .filter(id -> filterByCategory(products, id, category))
                .collect(Collectors.toList());

        Map<Long, Float> productPricesMap = priceService.findProductsPrices(productIds);

        return products.stream()
                .filter(product -> productIds.contains(product.getIdProduct()))
                .map(product -> convertToDtoWithPrice(product, productPricesMap.get(product.getIdProduct())))
                .collect(Collectors.toList());
    }

    /**
     * Filtre les produits par plage d'identifiants.
     *
     * @param id         L'identifiant du produit à filtrer.
     * @param startStr   L'ID de début pour la plage d'ID.
     * @param endStr     L'ID de fin pour la plage d'ID.
     * @return true si le produit passe le filtre, sinon false.
     */
    private boolean filterByIdRange(Long id, String startStr, String endStr) {
        if (startStr != null && !startStr.isEmpty()) {
            Long startId = Long.parseLong(startStr);
            if (id < startId) {
                return false;
            }
        }
        if (endStr != null && !endStr.isEmpty()) {
            Long endId = Long.parseLong(endStr);
            return id <= endId;
        }
        return true;
    }

    /**
     * Filtre les produits par nom.
     *
     * @param products   La liste des produits.
     * @param id         L'identifiant du produit à filtrer.
     * @param name       Le nom du produit à filtrer.
     * @return true si le produit passe le filtre, sinon false.
     */
    private boolean filterByName(List<Product> products, long id, String name) {
        if (name != null && !name.isEmpty()) {
            for (Product product : products) {
                if (product.getIdProduct() == id && product.getName().toLowerCase().contains(name.toLowerCase())) {
                    return true;
                }
            }
            return false;
        }
        return true;
    }

    /**
     * Filtre les produits par code.
     *
     * @param products   La liste des produits.
     * @param id         L'identifiant du produit à filtrer.
     * @param code       Le code du produit à filtrer.
     * @return true si le produit passe le filtre, sinon false.
     */
    private boolean filterByCode(List<Product> products, long id, String code) {
        if (code != null && !code.isEmpty()) {
            for (Product product : products) {
                if (product.getIdProduct() == id && product.getProductCode().contains(code)) {
                    return true;
                }
            }
            return false;
        }
        return true;
    }

    /**
     * Filtre les produits par catégorie.
     *
     * @param products   La liste des produits.
     * @param id         L'identifiant du produit à filtrer.
     * @param categoryId L'identifiant de la catégorie à filtrer.
     * @return true si le produit passe le filtre, sinon false.
     */
    private boolean filterByCategory(List<Product> products, long id, String categoryId) {
        if (categoryId != null && !categoryId.isEmpty()) {
            long category = Long.parseLong(categoryId);
            for (Product product : products) {
                if (product.getIdProduct() == id && product.getCategory().getIdCategory() == category) {
                    return true;
                }
            }
            return false;
        }
        return true;
    }

    /**
     * Récupère un produit par son identifiant.
     *
     * @param id L'identifiant du produit.
     * @return Le produit sous forme de DTO.
     * @throws RuntimeException si le produit n'est pas trouvé.
     */
    public ProductDto getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
        ProductDto productDto = convertToDto(product);
        productDto.setPrice(priceService.findProductPrice(product.getIdProduct()));
        return productDto;
    }

    /**
     * Crée un nouveau produit.
     *
     * @param productDto Les informations du produit à créer.
     * @return Le produit créé sous forme de DTO.
     */
    public ProductDto createProduct(ProductDto productDto) {
        Product product = convertToEntity(productDto);
        Product savedProduct = productRepository.save(product);
        ProductDto savedProductDto = convertToDto(savedProduct);
        savedProductDto.setPrice(priceService.findProductPrice(product.getIdProduct()));
        return savedProductDto;
    }

    /**
     * Met à jour un produit existant.
     *
     * @param id         L'identifiant du produit à mettre à jour.
     * @param productDto Les nouvelles informations du produit.
     * @return Le produit mis à jour sous forme de DTO.
     * @throws RuntimeException si le produit n'est pas trouvé.
     */
    public ProductDto updateProduct(Long id, ProductDto productDto) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));

        product.setName(productDto.getName());
        product.setProductCode(productDto.getProductCode());
        product.setInventory(productDto.getInventory());

        if (productDto.getCategoryId() != 0) {
            Category category = categoryRepository.findById(productDto.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("Category not found with id: " + productDto.getCategoryId()));
            product.setCategory(category);
        }

        Product updatedProduct = productRepository.save(product);
        ProductDto updatedProductDto = convertToDto(updatedProduct);
        updatedProductDto.setPrice(priceService.findProductPrice(product.getIdProduct()));
        return updatedProductDto;
    }

    /**
     * Supprime un produit par son identifiant.
     *
     * @param id L'identifiant du produit à supprimer.
     */
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    private ProductDto convertToDto(Product product) {
        return new ProductDto(
                product.getIdProduct(),
                product.getName(),
                product.getProductCode(),
                product.getInventory(),
                product.getCategory().getIdCategory()
        );
    }

    private ProductDto convertToDtoWithPrice(Product product, Float price) {
        ProductDto productDto = new ProductDto(
                product.getIdProduct(),
                product.getName(),
                product.getProductCode(),
                product.getInventory(),
                product.getCategory().getIdCategory()
        );
        productDto.setPrice(price);
        return productDto;
    }

    private Product convertToEntity(ProductDto productDto) {
        Product product = new Product(
                productDto.getName(),
                productDto.getProductCode(),
                productDto.getInventory()
        );

        if (productDto.getCategoryId() != 0) {
            Category category = categoryRepository.findById(productDto.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("Category not found with id: " + productDto.getCategoryId()));
            product.setCategory(category);
        }

        return product;
    }

}
