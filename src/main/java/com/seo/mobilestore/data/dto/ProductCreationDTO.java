package com.seo.mobilestore.data.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductCreationDTO {
    private long category_id;
    private long manufacturer_id;
    private String name;
    private String description;
    private BigDecimal price;
    private int stocks;

    private List<Integer> product_tech_id;
    private List<Integer> series_id;
    private List<Integer> colors_id;
    private List<Integer> memories_id;
    private List<Integer> reviews_id;
    private List<Integer> images_id;
//    private List<ProductTech> productTechs;
//    private List<Seri> series;
//    private List<Color> colors;
//    private List<Memory> memories;
//    private List<Review> reviews;
//    private List<Image> images;
}
