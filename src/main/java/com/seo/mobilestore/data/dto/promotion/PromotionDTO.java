package com.seo.mobilestore.data.dto.promotion;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PromotionDTO {

    private long id;
    private String discountCodeDTO;
    private BigDecimal totalPurchaseDTO;
    private int discountDTO;
    private BigDecimal maxGetDTO;
    private Date expireDateDTO;
    private boolean campagnDTO;
    private boolean statusDTO;
}
