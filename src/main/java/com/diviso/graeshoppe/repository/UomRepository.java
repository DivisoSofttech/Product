package com.diviso.graeshoppe.repository;

import com.diviso.graeshoppe.domain.Uom;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Uom entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UomRepository extends JpaRepository<Uom, Long> {

}
