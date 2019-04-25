package com.diviso.graeshoppe.repository.search;

import com.diviso.graeshoppe.domain.Tax;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Tax entity.
 */
public interface TaxSearchRepository extends ElasticsearchRepository<Tax, Long> {
}
