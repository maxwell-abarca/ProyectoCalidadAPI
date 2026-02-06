package com.project.demo.logic.entity.rateOrder;

import com.project.demo.logic.entity.order.Order;
import com.project.demo.logic.entity.userBuyer.UserBuyer;
import jakarta.persistence.*;

@Table(name = "rate_order")
@Entity
public class RateOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "rate", nullable = false)
    private Integer rate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "order_id", referencedColumnName = "id", nullable = false)
    private Order order;

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

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public UserBuyer getUserBuyer() {
        return userBuyer;
    }

    public void setUserBuyer(UserBuyer userBuyer) {
        this.userBuyer = userBuyer;
    }

    public RateOrder(){}

}
