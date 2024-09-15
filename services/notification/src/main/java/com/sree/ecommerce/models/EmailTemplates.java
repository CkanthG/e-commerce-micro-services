package com.sree.ecommerce.models;

import lombok.Getter;

public enum EmailTemplates {
    PAYMENT_CONFIRMATION_TEMPLATE("payment-confirmation.html", "Payment successfully processed"),
    ORDER_CONFIRMATION_TEMPLATE("order-confirmation.html", "Order successfully placed")
    ;
    @Getter
    private final String template;
    @Getter
    private final String subject;

    EmailTemplates(String template, String subject) {
        this.template = template;
        this.subject = subject;
    }
}
