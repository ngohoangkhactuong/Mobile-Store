package com.seo.mobilestore.data.dto.product.review;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShowReviewDTO {
    private long id;
    private String user_name;
    private long product_id;
    private String comment;
    private int rating;
    private boolean status;
}
