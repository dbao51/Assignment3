package com.awe.onlinestore.model;

import java.io.Serializable;

public enum PaymentStatus implements Serializable {
    PENDING,
    AUTHORIZED,
    PROCESSED,
    FAILED,
    REFUNDED
}