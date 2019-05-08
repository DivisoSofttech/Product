package com.diviso.graeshoppe.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of StockDiarySearchRepository to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class StockDiarySearchRepositoryMockConfiguration {

    @MockBean
    private StockDiarySearchRepository mockStockDiarySearchRepository;

}
