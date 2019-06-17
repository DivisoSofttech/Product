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
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.diviso.graeshoppe.domain.Product;
import com.diviso.graeshoppe.domain.StockCurrent;
import com.diviso.graeshoppe.repository.ProductRepository;
import com.diviso.graeshoppe.repository.StockCurrentRepository;
import com.diviso.graeshoppe.service.dto.ProductDetailDTO;

/**
 * TODO Provide a detailed description here
 * 
 * @author MayaSanjeev mayabytatech, maya.k.k@lxisoft.com
 */
@Component
public class DBWriter implements ItemWriter<ProductDetailDTO> {

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	StockCurrentRepository stockCurrentRepo;

	@Override
	public void write(List<? extends ProductDetailDTO> products) throws Exception {

		System.out.println("Data Saved for Users:>>>>>>>>>>" + products);
		Long i=0L;
		for (ProductDetailDTO p : products) {
			
			Product dto = new Product();
			dto.setId(i++);
			dto.setName(p.getName());
			
			dto.setSearchkey("");
			
			dto.setReference("");
			
			System.out.println("............... before save........... ");
			
			Product pro = productRepository.save(dto);

			System.out.println("............... saved product:  " + pro);

			dto.setStockCurrent(new StockCurrent());
			
			dto.getStockCurrent().setId(i++);
			
			dto.getStockCurrent().setSellPrice(p.getPrice());

			stockCurrentRepo.save(dto.getStockCurrent());

			System.out.println(".............. saved stock: " + stockCurrentRepo.save(dto.getStockCurrent()));

		}
	}

}