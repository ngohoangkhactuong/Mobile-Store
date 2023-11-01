package com.seo.mobilestore.data.dto.product.memory;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemoryProductDTO implements Serializable {
    private String name;
}
