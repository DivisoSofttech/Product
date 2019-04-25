package com.diviso.graeshoppe.repository.search;

import com.diviso.graeshoppe.domain.BarcodeType;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the BarcodeType entity.
 */
public interface BarcodeTypeSearchRepository extends ElasticsearchRepository<BarcodeType, Long> {
}
