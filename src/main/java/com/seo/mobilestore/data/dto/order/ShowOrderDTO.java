package com.seo.mobilestore.data.dto.order;

import com.seo.mobilestore.data.dto.StatusDTO;
import com.seo.mobilestore.data.dto.product.ProductOrderDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShowOrderDTO {
    private long id;
    private BigDecimal total;
    private Date receiveDate;
    private StatusDTO statusDTO;
    private ProductOrderDTO productOrderDTO;
    private  long quantity;

}
