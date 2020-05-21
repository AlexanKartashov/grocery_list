package com.example.grocerylisting.Models;

public class CheckboxName {
    String name;
    Boolean checked;

    public CheckboxName() {
    }

    public CheckboxName(String name, Boolean checked) {
        this.name = name;
        this.checked = checked;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getChecked() {
        return checked;
    }

    public void setChecked(Boolean checked) {
        this.checked = checked;
    }
}
