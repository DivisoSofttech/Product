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

import java.util.HashMap;
import java.util.Map;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import com.diviso.graeshoppe.domain.Product;
import com.diviso.graeshoppe.service.dto.ProductDetailDTO;



/**
 * TODO Provide a detailed description here 
 * @author MayaSanjeev
 * mayabytatech, maya.k.k@lxisoft.com
 */
@Component
public class Processor implements ItemProcessor<ProductDetailDTO, ProductDetailDTO> {

    private static final Map<String, String> DEPT_NAMES =
            new HashMap<>();

    public Processor() {
   	 System.out.println("........pppppppppppppppp>>>>>>>>>>>>>>>>>>>>>>>>>>>");
    }

    @Override
    public ProductDetailDTO process(ProductDetailDTO product) throws Exception {
    	 System.out.println("........||||||||||||||||||||||||||||||||||||||||>>>>>>>");
        return product;
    }
}


