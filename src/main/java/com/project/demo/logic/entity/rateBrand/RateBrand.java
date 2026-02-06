package com.project.demo.logic.entity.rateBrand;

import com.project.demo.logic.entity.userBrand.UserBrand;
import com.project.demo.logic.entity.userBuyer.UserBuyer;
import jakarta.persistence.*;

@Table(name = "rate_brand")
@Entity
public class RateBrand {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "rate", nullable = false)
    private Integer rate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_brand_id", referencedColumnName = "id", nullable = false)
    private UserBrand userBrand;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_buyer_id", referencedColumnName = "id", nullable = false)
    private UserBuyer userBuyer;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getRate() {
        return rate;
    }

    public void setRate(Integer rate) {
        this.rate = rate;
    }

    public UserBrand getUserBrand() {
        return userBrand;
    }

    public void setUserBrand(UserBrand userBrand) {
        this.userBrand = userBrand;
    }

    public UserBuyer getUserBuyer() {
        return userBuyer;
    }

    public void setUserBuyer(UserBuyer userBuyer) {
        this.userBuyer = userBuyer;
    }

    public RateBrand(){}

}
