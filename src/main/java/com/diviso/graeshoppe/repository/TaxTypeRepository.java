package com.diviso.graeshoppe.repository;

import com.diviso.graeshoppe.domain.TaxType;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the TaxType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TaxTypeRepository extends JpaRepository<TaxType, Long> {

}
