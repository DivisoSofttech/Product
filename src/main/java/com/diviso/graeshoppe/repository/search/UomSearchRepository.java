package com.diviso.graeshoppe.repository.search;

import com.diviso.graeshoppe.domain.Uom;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Uom entity.
 */
public interface UomSearchRepository extends ElasticsearchRepository<Uom, Long> {
}
