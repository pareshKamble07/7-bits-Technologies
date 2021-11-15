package com.example.mobileauthentication.model;

public class Products {

    String image;
    Long id;
    Boolean isSelected = false;

    public String getImage() {
        return image;
    }

    public Long getId() {
        return id;
    }

    public Boolean getSelected() {
        return isSelected;
    }

    public void setSelected(Boolean selected) {
        isSelected = selected;
    }
}
