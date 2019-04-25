package com.diviso.graeshoppe.repository;

import com.diviso.graeshoppe.domain.BarcodeType;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the BarcodeType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BarcodeTypeRepository extends JpaRepository<BarcodeType, Long> {

}
