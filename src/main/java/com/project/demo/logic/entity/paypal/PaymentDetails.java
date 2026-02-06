package com.project.demo.logic.entity.paypal;

import com.paypal.api.payments.Transaction;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PaymentDetails {
    private  String intent;
    private  String method;
    private List<Transaction> transactions;
}
