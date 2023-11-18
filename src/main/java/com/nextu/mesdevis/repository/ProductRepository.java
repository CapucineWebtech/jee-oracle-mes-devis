package com.nextu.mesdevis.repository;

import com.nextu.mesdevis.entity.Category;
import com.nextu.mesdevis.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByNameContaining(String name);
    List<Product> findByProductCodeContaining(String productCode);
    List<Product> findByCategory(Category category);
}
