package com.awe.onlinestore.model;

import java.io.Serializable;

public enum OrderStatus implements Serializable {
    PENDING,
    CONFIRMED,
    PAID,
    CANCELLED,
    SHIPPED,
    DELIVERED
}