package com.example.javafxlibrarymanagementsystem.model;

import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

///  Testing the findBillByNumber method in the Bill class and achieved 100% coverage for that method.

public class FindBillByNumberTest {

    @Test
    public void testFindBillByNumber_NullList() {
        List<Bill> bills = null;
        int billNumber = 101;
        Bill result = Bill.findBillByNumber(bills, billNumber);
        assertNull(result, "Expected null when bills list is null.");
    }

    @Test
    public void testFindBillByNumber_EmptyList() {
        List<Bill> bills = new ArrayList<>();
        int billNumber = 101;
        Bill result = Bill.findBillByNumber(bills, billNumber);
        assertNull(result, "Expected null when bills list is empty.");
    }

    @Test
    public void testFindBillByNumber_BillExists() {
        List<Bill> bills = new ArrayList<>();
        Bill bill1 = new Bill(101, "2025-01-01");
        Bill bill2 = new Bill(102, "2025-01-02");
        bills.add(bill1);
        bills.add(bill2);
        int billNumber = 101;
        Bill result = Bill.findBillByNumber(bills, billNumber);
        assertNotNull(result, "Expected to find a bill with number 101.");
        assertEquals(bill1, result, "Expected to find bill1.");
    }

    @Test
    public void testFindBillByNumber_BillDoesNotExist() {
        List<Bill> bills = new ArrayList<>();
        Bill bill1 = new Bill(101, "2025-01-01");
        Bill bill2 = new Bill(102, "2025-01-02");
        bills.add(bill1);
        bills.add(bill2);
        int billNumber = 103;
        Bill result = Bill.findBillByNumber(bills, billNumber);
        assertNull(result, "Expected null when bill number does not exist.");
    }

    @Test
    public void testFindBillByNumber_MultipleBillsWithSameNumber() {
        List<Bill> bills = new ArrayList<>();
        Bill bill1 = new Bill(101, "2025-01-01");
        Bill bill2 = new Bill(101, "2025-01-02");
        bills.add(bill1);
        bills.add(bill2);
        int billNumber = 101;
        Bill result = Bill.findBillByNumber(bills, billNumber);
        assertNotNull(result, "Expected to find a bill with number 101.");
        assertEquals(bill1, result, "Expected to find the first occurrence of bill with number 101.");
    }
}
