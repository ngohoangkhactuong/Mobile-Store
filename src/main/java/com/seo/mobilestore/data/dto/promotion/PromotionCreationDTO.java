package com.seo.mobilestore.data.dto.promotion;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.seo.mobilestore.constant.Constant;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.Date;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class PromotionCreationDTO {

    @Pattern(regexp = "[A-Z0-9]{7}", message = "Discount code must have 7 characters (uppercase letters and digits)")
    @Size(min = 7, max = 7, message = "Discount code must have 7 characters")
    @NotBlank(message = "{error.discountCodeNotNull}")
    private String discountCodeDTO;

    private BigDecimal totalPurchaseDTO;

    private int discountDTO;

    private BigDecimal maxGetDTO;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constant.DATE_FORMAT)
    private Date expireDateDTO;
}
