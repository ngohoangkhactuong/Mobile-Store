package com.seo.mobilestore.data.dto.product.seri;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SeriProductDTO implements Serializable {
    private long id;
    private String name;
    private long product_id;
    private boolean status;
}


