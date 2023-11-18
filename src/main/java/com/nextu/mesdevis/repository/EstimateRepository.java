package com.nextu.mesdevis.repository;

import com.nextu.mesdevis.entity.Estimate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface EstimateRepository extends JpaRepository<Estimate, Long> {

}
