package com.example.javafxlibrarymanagementsystem.model;

import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

///  Testing the getTopNbillsByTotalAmount method in the Bill class and achieved 100% coverage for that method.

public class getTopNBillsByTotalAmountTest {

    @Test
    public void testGetTopNBillsByTotalAmount_NullList() {
        List<Bill> bills = null;
        int n = 3;
        List<Bill> result = Bill.getTopNBillsByTotalAmount(bills, n);
        assertNotNull(result, "Expected an empty list when bills list is null.");
        assertTrue(result.isEmpty(), "Expected an empty list when bills list is null.");
    }

    @Test
    public void testGetTopNBillsByTotalAmount_EmptyList() {
        List<Bill> bills = new ArrayList<>();
        int n = 3;
        List<Bill> result = Bill.getTopNBillsByTotalAmount(bills, n);
        assertNotNull(result, "Expected an empty list when bills list is empty.");
        assertTrue(result.isEmpty(), "Expected an empty list when bills list is empty.");
    }

    @Test
    public void testGetTopNBillsByTotalAmount_NegativeN() {
        List<Bill> bills = createSampleBills();
        int n = -1;
        List<Bill> result = Bill.getTopNBillsByTotalAmount(bills, n);
        assertNotNull(result, "Expected an empty list when n is negative.");
        assertTrue(result.isEmpty(), "Expected an empty list when n is negative.");
    }

    @Test
    public void testGetTopNBillsByTotalAmount_ZeroN() {
        List<Bill> bills = createSampleBills();
        int n = 0;
        List<Bill> result = Bill.getTopNBillsByTotalAmount(bills, n);
        assertNotNull(result, "Expected an empty list when n is zero.");
        assertTrue(result.isEmpty(), "Expected an empty list when n is zero.");
    }

    @Test
    public void testGetTopNBillsByTotalAmount_NGreaterThanListSize() {
        List<Bill> bills = createSampleBills();
        int n = 10;
        List<Bill> result = Bill.getTopNBillsByTotalAmount(bills, n);
        assertEquals(5, result.size(), "Expected list size to be equal to the number of bills available.");
        assertEquals(105, result.get(0).getBillNumber(), "Expected bill number 105 to be first.");
        assertEquals(104, result.get(1).getBillNumber(), "Expected bill number 104 to be second.");
        assertEquals(103, result.get(2).getBillNumber(), "Expected bill number 103 to be third.");
        assertEquals(102, result.get(3).getBillNumber(), "Expected bill number 102 to be fourth.");
        assertEquals(101, result.get(4).getBillNumber(), "Expected bill number 101 to be fifth.");
    }

    @Test
    public void testGetTopNBillsByTotalAmount_NLessThanListSize() {
        List<Bill> bills = createSampleBills();
        int n = 3;
        List<Bill> result = Bill.getTopNBillsByTotalAmount(bills, n);
        assertEquals(3, result.size(), "Expected list size to be equal to n.");
        assertEquals(105, result.get(0).getBillNumber(), "Expected bill number 105 to be first.");
        assertEquals(104, result.get(1).getBillNumber(), "Expected bill number 104 to be second.");
        assertEquals(103, result.get(2).getBillNumber(), "Expected bill number 103 to be third.");
    }

    @Test
    public void testGetTopNBillsByTotalAmount_NEqualToListSize() {
        List<Bill> bills = createSampleBills();
        int n = 5;
        List<Bill> result = Bill.getTopNBillsByTotalAmount(bills, n);
        assertEquals(5, result.size(), "Expected list size to be equal to n.");
        assertEquals(105, result.get(0).getBillNumber(), "Expected bill number 105 to be first.");
        assertEquals(104, result.get(1).getBillNumber(), "Expected bill number 104 to be second.");
        assertEquals(103, result.get(2).getBillNumber(), "Expected bill number 103 to be third.");
        assertEquals(102, result.get(3).getBillNumber(), "Expected bill number 102 to be fourth.");
        assertEquals(101, result.get(4).getBillNumber(), "Expected bill number 101 to be fifth.");
    }

    @Test
    public void testGetTopNBillsByTotalAmount_DuplicateTotalAmounts() {
        List<Bill> bills = new ArrayList<>();
        bills.add(new Bill(101, "2025-01-01", 150.0));
        bills.add(new Bill(102, "2025-01-02", 200.0));
        bills.add(new Bill(103, "2025-01-03", 150.0));
        bills.add(new Bill(104, "2025-01-04", 250.0));
        bills.add(new Bill(105, "2025-01-05", 200.0));
        int n = 3;
        List<Bill> result = Bill.getTopNBillsByTotalAmount(bills, n);
        assertEquals(3, result.size(), "Expected list size to be equal to n.");
        assertEquals(104, result.get(0).getBillNumber(), "Expected bill number 104 to be first.");
        assertTrue(result.get(1).getBillNumber() == 102 || result.get(1).getBillNumber() == 105,
                "Expected bill number 102 or 105 to be second.");
        assertTrue(result.get(2).getBillNumber() == 102 || result.get(2).getBillNumber() == 105,
                "Expected bill number 102 or 105 to be third.");
    }

    private List<Bill> createSampleBills() {
        List<Bill> bills = new ArrayList<>();
        bills.add(new Bill(101, "2025-01-01", 150.0));
        bills.add(new Bill(104, "2025-01-04", 300.0));
        bills.add(new Bill(105, "2025-01-05", 350.0));
        bills.add(new Bill(102, "2025-01-02", 200.0));
        bills.add(new Bill(103, "2025-01-03", 250.0));

        return bills;
    }
}