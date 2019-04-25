package com.diviso.graeshoppe.repository;

import com.diviso.graeshoppe.domain.Barcode;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Barcode entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BarcodeRepository extends JpaRepository<Barcode, Long> {

}
