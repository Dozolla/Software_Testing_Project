package com.example.javafxlibrarymanagementsystem.model;

import java.util.ArrayList;
import java.util.List;

public class Manager extends User {
    // Additional attributes specific to Manager
    private List<Book> stock;

    // Constructor
    public Manager(String name, String username, String password, String birthday,
                   String phone, String email, double salary, Boolean CanAddBooksPermission) {
        super(name, username, password, birthday, phone, email, salary, AccessLevel.MANAGER,CanAddBooksPermission);
        this.stock = new ArrayList<>();
    }

    // Getters and Setters
    public List<Book> getStock() {
        return stock;
    }

    public void setStock(List<Book> stock) {
        this.stock = stock;
    }

    // Additional Methods
    public void addBookToStock(Book book) {
        stock.add(book);
    }

    public void removeBookFromStock(Book book) {
        stock.remove(book);
    }

    @Override
    public String toString() {
        return "Manager{" +
                "name='" + getName() + '\'' +
                ", username='" + getUsername() + '\'' +
                ", password='" + getPassword() + '\'' +
                ", birthday='" + getBirthday() + '\'' +
                ", phone='" + getPhone() + '\'' +
                ", email='" + getEmail() + '\'' +
                ", salary=" + getSalary() +
                ", accessLevel=" + getAccessLevel() +
                ", stock=" + stock +
                '}';
    }
}
