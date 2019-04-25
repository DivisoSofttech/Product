package com.diviso.graeshoppe.repository.search;

import com.diviso.graeshoppe.domain.Barcode;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Barcode entity.
 */
public interface BarcodeSearchRepository extends ElasticsearchRepository<Barcode, Long> {
}
