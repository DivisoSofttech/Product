package com.diviso.graeshoppe.repository.search;

import com.diviso.graeshoppe.domain.TaxType;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the TaxType entity.
 */
public interface TaxTypeSearchRepository extends ElasticsearchRepository<TaxType, Long> {
}
