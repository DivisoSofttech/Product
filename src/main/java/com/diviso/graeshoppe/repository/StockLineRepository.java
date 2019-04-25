package com.diviso.graeshoppe.repository;

import com.diviso.graeshoppe.domain.StockLine;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the StockLine entity.
 */
@SuppressWarnings("unused")
@Repository
public interface StockLineRepository extends JpaRepository<StockLine, Long> {

}
