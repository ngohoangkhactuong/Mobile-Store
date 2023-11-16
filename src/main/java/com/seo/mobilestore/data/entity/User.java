package com.seo.mobilestore.data.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "user")
public class User extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column( name = "id")
    private long id;

    @Column(name = "email", unique = true, columnDefinition = "NVARCHAR(100)")
    private String email;

    @JsonIgnore
    @Column(name = "password", nullable = false, columnDefinition = "NVARCHAR(100)")
    private String password;

    @Column(name = "full_name", nullable = false, columnDefinition = "NVARCHAR(25)")
    private String fullName;

    @Column(name = "gender", columnDefinition = "tinyint(2)")
    private int gender;

    @Temporal(TemporalType.DATE)
    @Column(name = "birthday")
    private Date birthDay;

    @JsonIgnore
    @Column(name = "auth_provider", columnDefinition = "NVARCHAR(10)")
    private String authProvider;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    @Column(name = "status")
    private boolean status;

    @JsonIgnore
    @Column(name = "otp", columnDefinition = "NVARCHAR(10)")
    private String otp;

    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @Fetch(FetchMode.SUBSELECT)
    private List<Address> addressList = new ArrayList<>();

    @Column(name = "lock_status")
    private boolean lock_status;

}
