package com.example.javafxlibrarymanagementsystem.model;

import java.io.Serializable;
import javafx.beans.property.*;

public class Book implements Serializable {
    // Attributes
    private String ISBN;
    private String title;
    private String category;
    private String supplier;
    private String author;
    private int stock;
    private String purchasedDate;
    private double purchasedPrice;
    private double originalPrice;
    private double sellingPrice;
    // Assuming book cover is represented as an image path
    private String bookCoverImagePath;

    // Constructors
    public Book(String ISBN, String title, String category, String supplier, String author, int stock,
                String purchasedDate, double purchasedPrice, double originalPrice, double sellingPrice, String bookCoverImagePath) {
        this.ISBN = ISBN;
        this.title = title;
        this.category = category;
        this.supplier = supplier;
        this.author = author;
        this.stock = stock;
        this.purchasedDate = purchasedDate;
        this.purchasedPrice = purchasedPrice;
        this.originalPrice = originalPrice;
        this.sellingPrice = sellingPrice;
        this.bookCoverImagePath = bookCoverImagePath;
    }

    // Getters and Setters
    public String getISBN() {
        return ISBN;
    }

    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getPurchasedDate() {
        return purchasedDate;
    }

    public void setPurchasedDate(String purchasedDate) {
        this.purchasedDate = purchasedDate;
    }

    public double getPurchasedPrice() {
        return purchasedPrice;
    }

    public void setPurchasedPrice(double purchasedPrice) {
        this.purchasedPrice = purchasedPrice;
    }

    public double getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(double originalPrice) {
        this.originalPrice = originalPrice;
    }

    public double getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(double sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    public String getBookCoverImagePath() {
        return bookCoverImagePath;
    }

    public void setBookCoverImagePath(String bookCoverImagePath) {
        this.bookCoverImagePath = bookCoverImagePath;
    }

    // Additional Methods (based on the requirements)
    public boolean isOutOfStock() {
        return stock <= 0;
    }

    public boolean isSameBook(String ISBN) {
        return this.ISBN.equals(ISBN);
    }
    // Method to decrease stock
    public void decreaseStock(int quantity) {
        if (quantity > 0) {
            System.out.println(quantity + " "+ this.stock);
            this.stock -= quantity;
            System.out.println(quantity + " "+ this.stock);

            if (this.stock < 0) {
                this.stock = 0; // Ensure stock is non-negative
            }
        }
    }
    @Override
    public String toString() {
        return "Book{" +
                "ISBN='" + ISBN + '\'' +
                ", title='" + title + '\'' +
                ", category='" + category + '\'' +
                ", supplier='" + supplier + '\'' +
                ", author='" + author + '\'' +
                ", stock=" + stock +
                ", purchasedDate='" + purchasedDate + '\'' +
                ", purchasedPrice=" + purchasedPrice +
                ", originalPrice=" + originalPrice +
                ", sellingPrice=" + sellingPrice +
                ", bookCoverImagePath='" + bookCoverImagePath + '\'' +
                '}';
    }
}
