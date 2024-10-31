package com.example.javafxlibrarymanagementsystem.model;

import java.util.ArrayList;
import java.util.List;

public class Librarian extends User {
    // Additional attributes specific to Librarian
    private List<Bill> bills;

    // Constructor
    public Librarian(String name, String username, String password, String birthday,
                     String phone, String email, double salary, Boolean CanAddBooksPermission) {
        super(name, username, password, birthday, phone, email, salary, AccessLevel.LIBRARIAN,CanAddBooksPermission);
        this.bills = new ArrayList<>();
    }

    // Getters and Setters
    public List<Bill> getBills() {
        return bills;
    }

    public void setBills(List<Bill> bills) {
        this.bills = bills;
    }

    // Additional Methods
    public void addBill(Bill bill) {
        bills.add(bill);
    }

    public void removeBill(Bill bill) {
        bills.remove(bill);
    }

    @Override
    public String toString() {
        return "Librarian{" +
                "name='" + getName() + '\'' +
                ", username='" + getUsername() + '\'' +
                ", password='" + getPassword() + '\'' +
                ", birthday='" + getBirthday() + '\'' +
                ", phone='" + getPhone() + '\'' +
                ", email='" + getEmail() + '\'' +
                ", salary=" + getSalary() +
                ", accessLevel=" + getAccessLevel() +
                ", bills=" + bills +
                '}';
    }
}
