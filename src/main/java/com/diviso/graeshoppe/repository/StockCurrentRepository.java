package com.diviso.graeshoppe.repository;

import com.diviso.graeshoppe.domain.StockCurrent;
import com.diviso.graeshoppe.service.dto.StockCurrentDTO;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the StockCurrent entity.
 */
@SuppressWarnings("unused")
@Repository
public interface StockCurrentRepository extends JpaRepository<StockCurrent, Long> {

	Optional<StockCurrent> findByProductId(Long id);

}
