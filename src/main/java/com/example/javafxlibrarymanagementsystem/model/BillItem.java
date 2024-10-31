package com.example.javafxlibrarymanagementsystem.model;

import java.io.Serializable;

public class BillItem implements Serializable {
    // Attributes
    private String bookISBN;
    private String bookTitle;
    private int quantity;
    private double unitPrice;
    private double subtotal;

    // Constructors
    public BillItem(String bookISBN, String bookTitle, int quantity, double unitPrice) {
        this.bookISBN = bookISBN;
        this.bookTitle = bookTitle;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.subtotal = calculateSubtotal();
    }

    // Getters and Setters
    public String getBookISBN() {
        return bookISBN;
    }

    public void setBookISBN(String bookISBN) {
        this.bookISBN = bookISBN;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
        this.subtotal = calculateSubtotal();
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
        this.subtotal = calculateSubtotal();
    }

    public double getSubtotal() {
        return subtotal;
    }

    // Additional Methods
    private double calculateSubtotal() {
        return quantity * unitPrice;
    }

    @Override
    public String toString() {
        return "BillItem{" +
                "bookISBN=" + bookISBN +
                ", bookTitle='" + bookTitle + '\'' +
                ", quantity=" + quantity +
                ", unitPrice=" + unitPrice +
                ", subtotal=" + subtotal +
                '}';
    }
}
