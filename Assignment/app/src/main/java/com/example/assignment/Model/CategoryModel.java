package com.example.assignment.Model;

import java.util.List;

public class CategoryModel {
    private String menu_id, name, image;
    List<ProductModel> products;

    public CategoryModel() {
    }

    public CategoryModel(String menu_id, String name, String image, List<ProductModel> products) {
        this.menu_id = menu_id;
        this.name = name;
        this.image = image;
        this.products = products;
    }

    public String getMenu_id() {
        return menu_id;
    }

    public void setMenu_id(String menu_id) {
        this.menu_id = menu_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public List<ProductModel> getProducts() {
        return products;
    }

    public void setProducts(List<ProductModel> products) {
        this.products = products;
    }
}
