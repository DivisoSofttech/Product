package com.diviso.graeshoppe.repository;

import com.diviso.graeshoppe.domain.StockDiary;
import com.diviso.graeshoppe.service.dto.StockDiaryDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the StockDiary entity.
 */
@SuppressWarnings("unused")
@Repository
public interface StockDiaryRepository extends JpaRepository<StockDiary, Long> {

	Page<StockDiary> findByProductId(Long id,Pageable pageable);

}
