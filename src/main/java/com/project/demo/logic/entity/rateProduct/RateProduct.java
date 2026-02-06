package com.project.demo.logic.entity.rateProduct;

import com.project.demo.logic.entity.product.Product;
import com.project.demo.logic.entity.userBuyer.UserBuyer;
import jakarta.persistence.*;

@Table(name = "rate_product")
@Entity
public class RateProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "rate", nullable = false)
    private Integer rate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id", referencedColumnName = "id", nullable = false)
    private Product product;

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

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public UserBuyer getUserBuyer() {
        return userBuyer;
    }

    public void setUserBuyer(UserBuyer userBuyer) {
        this.userBuyer = userBuyer;
    }

    public RateProduct(){}

}
