package com.diviso.graeshoppe.repository.search;

import com.diviso.graeshoppe.domain.StockLine;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the StockLine entity.
 */
public interface StockLineSearchRepository extends ElasticsearchRepository<StockLine, Long> {
}
