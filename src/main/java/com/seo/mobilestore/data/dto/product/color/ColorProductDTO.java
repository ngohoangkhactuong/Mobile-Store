package com.seo.mobilestore.data.dto.product.color;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ColorProductDTO implements Serializable {

    private int id;
    private String name;
    private boolean status;
    private long product_id;
}

