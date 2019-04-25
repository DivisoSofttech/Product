package com.diviso.graeshoppe.repository.search;

import com.diviso.graeshoppe.domain.TaxCategory;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the TaxCategory entity.
 */
public interface TaxCategorySearchRepository extends ElasticsearchRepository<TaxCategory, Long> {
}
