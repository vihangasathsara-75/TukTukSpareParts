package com.tuktuk.tuktukspareparts;

public class Part {
    private String code;
    private String name;
    private String brand;
    private double price;
    private int quantity;
    private String category;
    private String date;
    private String imageFile;

    public Part(String code, String name, String brand, double price, int quantity, String category, String date, String imagefile) {
        this.code = code;
        this.name = name;
        this.brand = brand;
        this.price = price;
        this.quantity = quantity;
        this.category = category;
        this.date = date;
        this.imageFile = imageFile;

    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getBrand() {
        return brand;
    }

    public double getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getCategory() {
        return category;
    }

    public String getDate() {
        return date;
    }

    public String getImageFile() {
        return imageFile;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setImageFile(String imageFile) {
        this.imageFile = imageFile;
    }
}