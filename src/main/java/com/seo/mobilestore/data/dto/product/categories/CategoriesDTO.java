package com.seo.mobilestore.data.dto.product.categories;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoriesDTO implements Serializable {

    private long id;
    private String name;
}
