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

import java.util.List;

import org.jfree.util.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.diviso.graeshoppe.domain.Product;
import com.diviso.graeshoppe.domain.StockCurrent;
import com.diviso.graeshoppe.repository.ProductRepository;
import com.diviso.graeshoppe.repository.StockCurrentRepository;
import com.diviso.graeshoppe.service.dto.ProductDTO;
import com.diviso.graeshoppe.service.dto.ProductDetailDTO;
import com.diviso.graeshoppe.service.dto.StockCurrentDTO;
import com.diviso.graeshoppe.service.mapper.ProductMapper;
import com.diviso.graeshoppe.service.mapper.StockCurrentMapper;
import com.diviso.graeshoppe.web.rest.NoteResource;
import com.diviso.graeshoppe.web.rest.ProductResource;
import com.diviso.graeshoppe.web.rest.StockCurrentResource;

/**
 * TODO Provide a detailed description here
 * 
 * @author MayaSanjeev mayabytatech, maya.k.k@lxisoft.com
 */
@Component
public class DBWriter implements ItemWriter<ProductDetailDTO> {

	private final Logger log = LoggerFactory.getLogger(DBWriter.class);

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	StockCurrentRepository stockCurrentRepo;

	@Autowired
	ProductResource productResource;

	@Autowired
	StockCurrentResource stockCurrentResource;

	@Autowired
	ProductMapper productMapper;

	@Autowired
	StockCurrentMapper stockCurrentMapper;

	@Override
	public void write(List<? extends ProductDetailDTO> products) throws Exception {

		System.out.println("Data Saved for Users:>>>>>>>>>>" + products);
		Long i = 0L;
		for (ProductDetailDTO p : products) {

			Product dto = new Product();

			dto.setName(p.getName());

			dto.setId(p.getId());

			log.debug(".................ID: .............." + p.getId());

			dto.setSearchkey("");

			dto.setReference("");

			ProductDTO productDto = productMapper.toDto(dto);

			ProductDTO pro = null;

			if (dto.getId() == null) {

				log.debug("..............CREATE..................");

				pro = productResource.createProduct(productDto).getBody();

				dto.setStockCurrent(new StockCurrent());

				dto.getStockCurrent().setSellPrice(p.getPrice());

				StockCurrentDTO stockCurrentDTO = stockCurrentMapper.toDto(dto.getStockCurrent());

				stockCurrentDTO.setProductId(pro.getId());

				stockCurrentResource.createStockCurrent(stockCurrentDTO);

			} else {
				log.debug("..............UPDATE..................");
				pro = productResource.updateProduct(productDto).getBody();

				StockCurrentDTO stockCurrentDTO = stockCurrentResource.getStockCurrentByProductId(pro.getId())
						.getBody();
				log.debug("............Retrived stock ................." + stockCurrentDTO);

				stockCurrentDTO.setSellPrice(p.getPrice());

				stockCurrentResource.updateStockCurrent(stockCurrentDTO);

			}

		}
	}

}