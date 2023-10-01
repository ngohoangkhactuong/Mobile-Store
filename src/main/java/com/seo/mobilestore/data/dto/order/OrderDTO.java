package com.seo.mobilestore.data.dto.order;

import com.seo.mobilestore.data.dto.StatusDTO;
import com.seo.mobilestore.data.dto.paymentMethod.PaymentMethodDTO;
import com.seo.mobilestore.data.dto.user.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderDTO {
    private long id;
    private BigDecimal total;
    private Date receiveDate;
    private boolean paymentStatus;
    private PaymentMethodDTO paymentMethodDTO;
    private UserDTO userDTO;
    private StatusDTO statusDTO;
}
