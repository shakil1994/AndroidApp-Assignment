package com.example.assignment.Model;

public class ProductModel {
    private String key, name, image, id, description;
    private Long price;
    private Double ratingValue;
    private Long ratingCount;

    public ProductModel() {
    }

    public ProductModel(String key, String name, String image, String id, String description, Long price, Double ratingValue, Long ratingCount) {
        this.key = key;
        this.name = name;
        this.image = image;
        this.id = id;
        this.description = description;
        this.price = price;
        this.ratingValue = ratingValue;
        this.ratingCount = ratingCount;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public Double getRatingValue() {
        return ratingValue;
    }

    public void setRatingValue(Double ratingValue) {
        this.ratingValue = ratingValue;
    }

    public Long getRatingCount() {
        return ratingCount;
    }

    public void setRatingCount(Long ratingCount) {
        this.ratingCount = ratingCount;
    }
}
