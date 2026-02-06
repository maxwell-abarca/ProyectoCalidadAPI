package com.project.demo.logic.entity.userBuyer;

import com.project.demo.logic.entity.avatar.Avatar;
import com.project.demo.logic.entity.cart.Cart;
import com.project.demo.logic.entity.design.Design;
import com.project.demo.logic.entity.rateBrand.RateBrand;
import com.project.demo.logic.entity.rateOrder.RateOrder;
import com.project.demo.logic.entity.rateProduct.RateProduct;
import com.project.demo.logic.entity.user.User;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "user_buyer")
public class UserBuyer extends User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "picture", nullable = true)
    private String picture;

    @Column(name = "genre", nullable = true)
    private String genre;

    @Column(name = "delivery_location", nullable = true)
    private String deliveryLocation;

    @Column(name = "phone_number", nullable = true)
    private String phoneNumber;

    @CreationTimestamp
    @Column(updatable = false, name = "created_at")
    private Date createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Date updatedAt;

    @OneToOne(mappedBy = "userBuyer", fetch = FetchType.LAZY)
    private Avatar avatar;

    @OneToMany(mappedBy = "userBuyer", fetch = FetchType.LAZY)
    private List<RateBrand> rateBrand;

    @OneToMany(mappedBy = "userBuyer", fetch = FetchType.LAZY)
    private List<RateProduct> rateProduct;

    @OneToMany(mappedBy = "userBuyer", fetch = FetchType.LAZY)
    private List<RateOrder> rateOrder;

    @OneToMany(mappedBy = "userBuyer", fetch = FetchType.LAZY)
    private List<Cart> carts;

    @OneToMany(mappedBy = "userBuyer", fetch = FetchType.LAZY)
    private List<Design> designs;

    public UserBuyer() {
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getDeliveryLocation() {
        return deliveryLocation;
    }

    public void setDeliveryLocation(String deliveryLocation) {
        this.deliveryLocation = deliveryLocation;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public Date getCreatedAt() {
        return createdAt;
    }

    @Override
    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public Date getUpdatedAt() {
        return updatedAt;
    }

    @Override
    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }
}
