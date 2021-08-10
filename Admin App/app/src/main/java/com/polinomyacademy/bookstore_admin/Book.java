package com.polinomyacademy.bookstore_admin;

public class Book {

    private String id;

    private String title;
    private String authorName;
    private String edition;
    private String price;
    private String category;
    private String imageUrl;
    private String sellerId;
    private boolean isBooked;
    private boolean isApproved;
    private boolean isSold;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isBooked() {
        return isBooked;
    }

    public void setBooked(boolean booked) {
        isBooked = booked;
    }

    public void setApproved(boolean approved) {
        isApproved = approved;
    }

    public void setSold(boolean sold) {
        isSold = sold;
    }

    public Book() {
        isApproved = false;
    }

    public boolean isApproved() {
        return isApproved;
    }

    public boolean isSold() {
        return isSold;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getAuthorName() {
        return authorName;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getTitle() {
        return title;
    }

    public String getEdition() {
        return edition;
    }

    public void setEdition(String edition) {
        this.edition = edition;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }
}
