package com.nextu.mesdevis.repository;

import com.nextu.mesdevis.entity.Estimate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface EstimateRepository extends JpaRepository<Estimate, Long> {
    @Query("SELECT e FROM Estimate e WHERE e.idEstimate IN :ids")
    List<Estimate> findByIds(@Param("ids") List<Long> ids);
}
