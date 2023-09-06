package com.seo.mobilestore.data.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "promotion")
public class Promotion extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "total_purchase")
    private BigDecimal totalPurchase;

    @Column(name = "discount", columnDefinition = "tinyint(10)")
    private int discount;

    @Column(name = "max_get")
    private BigDecimal maxGet;

    @Column(name = "expire_date")
    @Temporal(TemporalType.DATE)
    private Date expireDate;

    private boolean campagn;

    @Column(name = "status")
    private boolean status;

    @Column(name = "discount_code", nullable = false)
    @Pattern(regexp = "[A-Z0-9]{7}", message = "Discount code must have 7 characters (uppercase letters and digits)")
    @Size(min = 7, max = 7, message = "Discount code must have 7 characters")
    @NotBlank(message = "Discount Code not null")
    private String discountCode;
}
