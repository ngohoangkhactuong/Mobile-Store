package com.seo.mobilestore.data.dto.product.seri;


import com.seo.mobilestore.data.dto.product.ProductOrderDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SeriOrderDTO implements Serializable {

    private long id;

    private ProductOrderDTO productOrderDTO;

    private String name;
}
