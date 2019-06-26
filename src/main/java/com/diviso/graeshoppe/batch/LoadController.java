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

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.MultipartConfigElement;

import org.jfree.util.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.MultiResourceItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartResolver;

import com.diviso.graeshoppe.service.dto.ProductDetailDTO;


/**
 * TODO Provide a detailed description here
 * 
 * @author MayaSanjeev mayabytatech, maya.k.k@lxisoft.com
 */
@RestController
@RequestMapping("/api")
public class LoadController {

	@Autowired
	JobLauncher jobLauncher;

	@Autowired
	JobBuilderFactory jobBuilderFactory;

	@Autowired
	StepBuilderFactory stepBuilderFactory;

	@Autowired
	ItemProcessor<ProductDetailDTO, ProductDetailDTO> itemProcessor;

	@Autowired
	ItemWriter<ProductDetailDTO> itemWriter;


    private final Logger log = LoggerFactory.getLogger(LoadController.class);
    
    
 /*   @Bean
    public MultipartConfigElement multipartConfigElement() {
        return new MultipartConfigElement("");
    }

    @Bean
    public MultipartResolver multipartResolver() {
        org.springframework.web.multipart.commons.CommonsMultipartResolver multipartResolver = new org.springframework.web.multipart.commons.CommonsMultipartResolver();
        multipartResolver.setMaxUploadSize(1000000);
        return multipartResolver;
    }*/

	
	/*
	 * @Autowired Job job;
	 */

	@PostMapping("/load-products")
	public BatchStatus load(@RequestParam("file") MultipartFile file) throws JobParametersInvalidException,
			JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException, IOException {

		log.info("...............file................:   "+file);
		
		Map<String, JobParameter> maps = new HashMap<>();

		maps.put("time", new JobParameter(System.currentTimeMillis()));

		JobParameters parameters = new JobParameters(maps);  

		ByteArrayResource[] resources = null;

		JobExecution jobExecution = null;

		for (int i = 0; i < 1; i++) {

			resources = new ByteArrayResource[1];
     
			resources[i] = new ByteArrayResource(file.getBytes());

			System.out.println("file resource   " + i + "  " + resources[i]);

			Job job = createJob(jobBuilderFactory, stepBuilderFactory, multiResourceItemReader(resources),
					itemProcessor, itemWriter);
			jobExecution = jobLauncher.run(job, parameters);

			System.out.println("JobExecution: " + jobExecution.getStatus());

			System.out.println("Batch is Running..." + jobExecution.isRunning());

		}
		while (jobExecution.isRunning()) {
			System.out.println("...");
		}
		return jobExecution.getStatus();
	}

	public Job createJob(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory,
			ItemReader<ProductDetailDTO> itemReader, ItemProcessor<ProductDetailDTO, ProductDetailDTO> itemProcessor,
			ItemWriter<ProductDetailDTO> itemWriter) {

		Step step = stepBuilderFactory.get("ETL-file-load").<ProductDetailDTO, ProductDetailDTO>chunk(1000)
				.reader(itemReader).processor(itemProcessor).writer(itemWriter).build();

		return jobBuilderFactory.get("ETL-Load").incrementer(new RunIdIncrementer()).start(step).build();
	}

	public MultiResourceItemReader<ProductDetailDTO> multiResourceItemReader(ByteArrayResource[] inputResources) {
		MultiResourceItemReader<ProductDetailDTO> resourceItemReader = new MultiResourceItemReader<ProductDetailDTO>();
		resourceItemReader.setResources(inputResources);
		resourceItemReader.setDelegate(itemReader());
		return resourceItemReader;
	}

	public FlatFileItemReader<ProductDetailDTO> itemReader() {

		FlatFileItemReader<ProductDetailDTO> flatFileItemReader = new FlatFileItemReader<>();

		flatFileItemReader.setName("CSV-Reader");
		flatFileItemReader.setLinesToSkip(1);
		flatFileItemReader.setLineMapper(lineMapper());

		return flatFileItemReader;
	}

	public LineMapper<ProductDetailDTO> lineMapper() {

		DefaultLineMapper<ProductDetailDTO> defaultLineMapper = new DefaultLineMapper<>();
		DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();

		lineTokenizer.setDelimiter(",");
		lineTokenizer.setStrict(false);
		lineTokenizer.setNames(new String[] { "id", "name", "price" });

		BeanWrapperFieldSetMapper<ProductDetailDTO> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
		fieldSetMapper.setTargetType(ProductDetailDTO.class);

		defaultLineMapper.setLineTokenizer(lineTokenizer);
		defaultLineMapper.setFieldSetMapper(fieldSetMapper);

		return defaultLineMapper;
	}

	@PostMapping("/upload-file")
	public void uploadFile(@RequestPart("file") MultipartFile file) throws IOException {
		System.out.println("file        ............        :" + file);
		byte[] bytes = file.getBytes();
		Path path = Paths.get("src/main/resources/" + file.getOriginalFilename());
		Files.write(path, bytes);
	}

}
