package com.seo.mobilestore.data.dto.order;

import com.seo.mobilestore.data.dto.StatusDTO;
import com.seo.mobilestore.data.dto.product.ProductOrderDTO;
import com.seo.mobilestore.data.dto.product.ShowProductOrderDTO;
import com.seo.mobilestore.data.dto.user.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShowOrderDTO {
    private long id;
    private BigDecimal total;
    private Date receiveDate;
    private StatusDTO statusDTO;
    private List<ShowProductOrderDTO> productOrderDTO;
    private long quantity;
    private UserDTO userDTO;
}
