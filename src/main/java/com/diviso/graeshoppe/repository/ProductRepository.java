package com.diviso.graeshoppe.repository;

import com.diviso.graeshoppe.domain.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the Product entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query(value = "select distinct product from Product product left join fetch product.labels left join fetch product.categories",
        countQuery = "select count(distinct product) from Product product")
    Page<Product> findAllWithEagerRelationships(Pageable pageable);

    @Query(value = "select distinct product from Product product left join fetch product.labels left join fetch product.categories")
    List<Product> findAllWithEagerRelationships();

    @Query("select product from Product product left join fetch product.labels left join fetch product.categories where product.id =:id")
    Optional<Product> findOneWithEagerRelationships(@Param("id") Long id);

}
