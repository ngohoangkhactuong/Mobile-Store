package com.seo.mobilestore.data.dto.order;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.seo.mobilestore.constant.Constant;
import com.seo.mobilestore.data.dto.StatusDTO;
import com.seo.mobilestore.data.dto.product.ProductOrderDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderCreationDTO {
//    private long id_user;  // cho biết người mua
    private long id_promotion; //  voucher áp dụng
    private String payment_method; // phương thức thanh toán
    private StatusDTO statusDTO;
    List<ProductOrderDTO> orderProductDTOList;
    private long id_address;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constant.DATE_FORMAT)
    private Date receiveDate;
}
