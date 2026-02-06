package com.project.demo.logic.entity.design;

import com.project.demo.logic.entity.cart.Cart;
import com.project.demo.logic.entity.product.Product;
import com.project.demo.logic.entity.userBuyer.UserBuyer;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "design")
public class Design {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @Column(name = "color", nullable = false)
    private String color;

    @Column(name = "picture")
    private String picture;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id", referencedColumnName = "id", nullable = false)
    private Product product;

    @Column(name = "modified_model", nullable = false)
    private String modifiedModel;

    @Column(name = "selected_size", nullable = false)
    private String selectedSize;

    @OneToMany(mappedBy = "design", fetch = FetchType.LAZY)
    private List<Cart> carts;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_buyer_id", referencedColumnName = "id", nullable = false)
    private UserBuyer userBuyer;

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public String getModifiedModel() {
        return modifiedModel;
    }

    public void setModifiedModel(String modifiedModel) {
        this.modifiedModel = modifiedModel;
    }

    public String getSelectedSize() {
        return selectedSize;
    }

    public void setSelectedSize(String selectedSize) {
        this.selectedSize = selectedSize;
    }

    public UserBuyer getUserBuyer() {
        return userBuyer;
    }

    public void setUserBuyer(UserBuyer userBuyer) {
        this.userBuyer = userBuyer;
    }
}
