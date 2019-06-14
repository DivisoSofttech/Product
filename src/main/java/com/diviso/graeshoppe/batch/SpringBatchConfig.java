 /*
 * Copyright 2002-2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.diviso.graeshoppe.batch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.MultiResourceItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import com.diviso.graeshoppe.service.dto.ProductDetailDTO;





/**
 * TODO Provide a detailed description here 
 * @author MayaSanjeev
 * mayabytatech, maya.k.k@lxisoft.com
 */
@Configuration
@EnableBatchProcessing
public class SpringBatchConfig {

	
	@Value("classpath:*.csv")
	Resource[] inputResources;
	
    @Bean
    public Job job(JobBuilderFactory jobBuilderFactory,
                   StepBuilderFactory stepBuilderFactory,
                   ItemReader<ProductDetailDTO> itemReader,
                   ItemProcessor<ProductDetailDTO, ProductDetailDTO> itemProcessor,
                   ItemWriter<ProductDetailDTO> itemWriter
    ) {

        Step step = stepBuilderFactory.get("ETL-file-load")
                .<ProductDetailDTO, ProductDetailDTO>chunk(100)
                .reader(multiResourceItemReader())
                .processor(itemProcessor)
                .writer(itemWriter)
                .build();


        return jobBuilderFactory.get("ETL-Load")
                .incrementer(new RunIdIncrementer())
                .start(step)
                .build();
    }

    
    
    @Bean
    public MultiResourceItemReader<ProductDetailDTO> multiResourceItemReader()
    {
        MultiResourceItemReader<ProductDetailDTO> resourceItemReader = new MultiResourceItemReader<ProductDetailDTO>();
        resourceItemReader.setResources(inputResources);
        resourceItemReader.setDelegate(itemReader());
        return resourceItemReader;
    }
    
    
    @Bean
    public FlatFileItemReader<ProductDetailDTO> itemReader() {

        FlatFileItemReader<ProductDetailDTO> flatFileItemReader = new FlatFileItemReader<>();
        
        flatFileItemReader.setName("CSV-Reader");
        flatFileItemReader.setLinesToSkip(1);
        flatFileItemReader.setLineMapper(lineMapper());
        
        return flatFileItemReader;
    }
    
    @Bean
    public LineMapper<ProductDetailDTO> lineMapper() {

    	
        DefaultLineMapper<ProductDetailDTO> defaultLineMapper = new DefaultLineMapper<>();
        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();

        lineTokenizer.setDelimiter(",");
        lineTokenizer.setStrict(false);
      lineTokenizer.setNames(new String[]{"name", "price"});
  
        BeanWrapperFieldSetMapper<ProductDetailDTO> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
        fieldSetMapper.setTargetType(ProductDetailDTO.class);
       
        defaultLineMapper.setLineTokenizer(lineTokenizer);
        defaultLineMapper.setFieldSetMapper(fieldSetMapper);

        return defaultLineMapper;
    }

}