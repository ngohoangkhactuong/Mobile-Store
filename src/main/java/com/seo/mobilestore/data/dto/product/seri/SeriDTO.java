package com.seo.mobilestore.data.dto.product.seri;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SeriDTO implements Serializable {

    private long id;
    private String name;
    private boolean status;
}

