package com.seo.mobilestore.data.entity;

import lombok.*;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "memory")
public class Memory extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(name = "name", nullable = false, columnDefinition = "NVARCHAR(50)")
    private String name;

    @Column(name = "status")
    private boolean status = true;

}
