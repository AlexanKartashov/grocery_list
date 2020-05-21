package com.example.grocerylisting.Models;

import androidx.annotation.Nullable;

public class Product implements Comparable<Product> {

    String productName;

    public Product(String productName) {
        this.productName = productName;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    @Override
    public boolean equals(@Nullable Object obj) {

        if (obj == this) {
            return true;
        }

        if (!(obj instanceof Product)) {
            return false;
        }

        Product c = (Product) obj;

        return productName.toLowerCase().trim().equals(c.productName.toLowerCase().trim());
    }

    @Override
    public int compareTo(Product u) {
        if (getProductName() == null || u.getProductName() == null) {
            return 0;
        }
        return getProductName().compareTo(u.getProductName());
    }
}
