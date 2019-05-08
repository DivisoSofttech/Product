package com.diviso.graeshoppe.repository.search;

import com.diviso.graeshoppe.domain.StockCurrent;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the StockCurrent entity.
 */
public interface StockCurrentSearchRepository extends ElasticsearchRepository<StockCurrent, Long> {
}
