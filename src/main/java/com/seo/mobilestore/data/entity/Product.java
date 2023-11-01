package com.seo.mobilestore.data.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "product")
public class Product extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Categories category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manufacturer_id", nullable = false)
    private Manufacturer manufacturer;

    @Column(name = "name", nullable = false, columnDefinition = "NVARCHAR(25)")
    private String name;

    @Column(name = "description", columnDefinition = "NVARCHAR(700)", nullable = false)
    private String description;

    @Column(name = "price", nullable = false)
    private BigDecimal price;

    @Column(name = "stocks", nullable = false, columnDefinition = "tinyint(7)")
    private int stocks;

    @Column(name = "status")
    private boolean status;

    @Column(name = "views")
    private int views = 0;

//    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
//    @Fetch(FetchMode.SUBSELECT)
//    private List<ProductTech> productTechs;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    @Fetch(FetchMode.SUBSELECT)
    private List<Seri> series;

//    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
//    @Fetch(FetchMode.SUBSELECT)
//    private List<Color> colors;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    @Fetch(FetchMode.SUBSELECT)
    private List<Memory> memories;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    @Fetch(FetchMode.SUBSELECT)
    private List<Review> reviews;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    @Fetch(FetchMode.SUBSELECT)
    private List<Image> images;

    public String toString()
    {
        return "Product [id=" + id + ", name=" + name + ", ...]";

    }

}
