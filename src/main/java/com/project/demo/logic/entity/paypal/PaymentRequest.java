package com.project.demo.logic.entity.paypal;

import java.util.List;

public class PaymentRequest {
    private List<ItemDto> items;
    private String currency;

    public List<ItemDto> getItems() {
        return items;
    }

    public void setItems(List<ItemDto> items) {
        this.items = items;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}
