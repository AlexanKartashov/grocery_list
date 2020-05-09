package com.example.grocerylisting.Models;

public class Uom {

    String uomName;
    String uomKey;

    public Uom(String uomName) {
        this.uomName = uomName;
    }

    public String getUomName() {
        return uomName;
    }

    public void setUomName(String uomName) {
        this.uomName = uomName;
    }

    public String getUomKey() {
        return uomKey;
    }

    public void setUomKey(String uomKey) {
        this.uomKey = uomKey;
    }
}
