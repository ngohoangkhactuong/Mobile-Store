package com.seo.mobilestore.data.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "address")
public class Address extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "location", nullable = false, columnDefinition = "NVARCHAR(70)")
    private String location;

    @Column(name = "phone_receiver", nullable = false, columnDefinition = "NVARCHAR(20)")
    private String phoneReceiver;

    @Column(name = "name_receiver", nullable = false, columnDefinition = "NVARCHAR(40)")
    private String nameReceiver;

    @Column(name = "defaults", nullable = false)
    private boolean defaults;

    @Column(name = "status", nullable = false,columnDefinition = "BOOLEAN DEFAULT true")
    private boolean status;

    @Column(name = "type", nullable = false)
    private String type;

}
