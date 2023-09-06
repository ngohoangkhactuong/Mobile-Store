package com.seo.mobilestore.data.entity;

import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "product_tech")
public class ProductTech extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "technical_id", nullable = false)
    private Technical technical;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "info", nullable = false, columnDefinition = "NVARCHAR(100)")
    private String info;

    @Column(name = "is_primary")
    private boolean isPrimary;

    @Column(name = "status")
    private boolean status = true;



}
