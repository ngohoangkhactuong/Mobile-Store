//package com.seo.mobilestore.data.entity;
//
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.EqualsAndHashCode;
//import lombok.NoArgsConstructor;
//import org.hibernate.annotations.Fetch;
//import org.hibernate.annotations.FetchMode;
//
//import javax.persistence.*;
//import java.util.List;
//
//
//@AllArgsConstructor
//@NoArgsConstructor
//@Data
//@EqualsAndHashCode(callSuper = true)
//@Entity
//@Table(name = "technical")
//public class Technical extends Auditable{
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private int id;
//
//    @Column(name = "name", nullable = false, columnDefinition = "NVARCHAR(50)")
//    private String name;
//
//    @OneToMany(mappedBy = "technical", cascade = CascadeType.ALL)
//    @Fetch(FetchMode.SUBSELECT)
//    private List<ProductTech> productTeches;
//}
