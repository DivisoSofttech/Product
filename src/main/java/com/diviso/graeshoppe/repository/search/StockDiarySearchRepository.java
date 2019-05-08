package com.diviso.graeshoppe.repository.search;

import com.diviso.graeshoppe.domain.StockDiary;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the StockDiary entity.
 */
public interface StockDiarySearchRepository extends ElasticsearchRepository<StockDiary, Long> {
}
