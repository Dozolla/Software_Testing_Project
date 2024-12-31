package com.example.javafxlibrarymanagementsystem.model;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Bill implements Serializable {
    private int billNumber;
    private List<BillItem> billItems;
    private String creationDate;
    private double totalAmount;

    public Bill(int billNumber, String creationDate) {
        this.billNumber = billNumber;
        this.billItems = new ArrayList<>();
        this.creationDate = creationDate;
        this.totalAmount = 0.0;
    }

    public int getBillNumber() {
        return billNumber;
    }

    public void setBillNumber(int billNumber) {
        this.billNumber = billNumber;
    }

    public List<BillItem> getBillItems() {
        return billItems;
    }

    public void setBillItems(List<BillItem> billItems) {
        this.billItems = billItems;
        updateTotalAmount();
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void addBillItem(BillItem billItem) {
        billItems.add(billItem);
        updateTotalAmount();
    }

    public void removeBillItem(BillItem billItem) {
        billItems.remove(billItem);
        updateTotalAmount();
    }

    private void updateTotalAmount() {
        totalAmount = billItems.stream().mapToDouble(BillItem::getSubtotal).sum();
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        // Additional logic for versioning if needed
    }

    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        // Additional logic for versioning if needed
    }

    @Override
    public String toString() {
        return "Bill{" +
                "billNumber=" + billNumber +
                ", billItems=" + billItems +
                ", creationDate='" + creationDate + '\'' +
                ", totalAmount=" + totalAmount +
                '}';
    }
}
