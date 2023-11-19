package com.nextu.mesdevis.repository;

import com.nextu.mesdevis.entity.ProductXEstimate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductXEstimateRepository extends JpaRepository<ProductXEstimate, Long> {

}
