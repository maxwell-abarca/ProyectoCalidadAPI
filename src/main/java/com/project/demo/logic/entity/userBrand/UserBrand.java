package com.project.demo.logic.entity.userBrand;

import com.project.demo.logic.entity.rateBrand.RateBrand;
import com.project.demo.logic.entity.product.Product;
import com.project.demo.logic.entity.user.User;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;
import java.util.List;


@Entity
@Table(name = "user_brand")
public class UserBrand extends User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "legal_id", nullable = false, unique = true, length = 9)
    private Long legalId;

    @Column(name = "logo_type", nullable = false)
    private String logoType;

    @Column(name = "brand_name", nullable = false, unique = true)
    private String brandName;

    @Column(name = "legal_representative_name", nullable = false)
    private String legalRepresentativeName;

    @Column(name = "main_location_address", nullable = false)
    private String mainLocationAddress;

    @Lob
    @Column(name = "legal_documents")
    private String legalDocuments;

    @Column(name = "brand_categories", nullable = false)
    private String brandCategories;

    @Column(name = "rate", nullable = true)
    private Integer rate;

    @CreationTimestamp
    @Column(updatable = false, name = "created_at")
    private Date createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Date updatedAt;

    public UserBrand() {}

    @OneToMany(mappedBy = "userBrand", fetch = FetchType.LAZY)
    private List<Product> products;

    @OneToMany(mappedBy = "userBrand", fetch = FetchType.LAZY)
    private List<RateBrand> rateBrand;




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
    public String getUsername() {
        return getEmail();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getLegalId() {
        return legalId;
    }

    public void setLegalId(Long legalId) {
        this.legalId = legalId;
    }

    public String getLogoType() {
        return logoType;
    }

    public void setLogoType(String logoType) {
        this.logoType = logoType;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getLegalRepresentativeName() {
        return legalRepresentativeName;
    }

    public void setLegalRepresentativeName(String legalRepresentativeName) {
        this.legalRepresentativeName = legalRepresentativeName;
    }

    public String getMainLocationAddress() {
        return mainLocationAddress;
    }

    public void setMainLocationAddress(String mainLocationAddress) {
        this.mainLocationAddress = mainLocationAddress;
    }

    public String getLegalDocuments() {
        return legalDocuments;
    }

    public void setLegalDocuments(String legalDocuments) {
        this.legalDocuments = legalDocuments;
    }

    public String getBrandCategories() {
        return brandCategories;
    }

    public void setBrandCategories(String brandCategories) {
        this.brandCategories = brandCategories;
    }

    @Override
    public void setEmail(String email) {
        super.setEmail(email);
    }

    @Override
    public void setPassword(String password) {
        super.setPassword(password);
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

    public Integer getRate() {
        return rate;
    }

    public void setRate(Integer rate) {
        this.rate = rate;
    }
}