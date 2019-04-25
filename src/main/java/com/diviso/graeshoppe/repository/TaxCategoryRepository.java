package com.diviso.graeshoppe.repository;

import com.diviso.graeshoppe.domain.TaxCategory;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the TaxCategory entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TaxCategoryRepository extends JpaRepository<TaxCategory, Long> {

}
