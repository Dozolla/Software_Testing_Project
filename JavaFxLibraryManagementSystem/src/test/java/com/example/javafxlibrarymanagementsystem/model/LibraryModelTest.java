package com.example.javafxlibrarymanagementsystem.model;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LibraryModelTest {

    private LibraryModel libraryModel;

    @BeforeEach
    void setUp() {
        libraryModel = Mockito.spy(new LibraryModel());
    }

    @Test
    void testAddBook() {
        // Create a real Book object instead of a mock
        Book book = new Book("123456", "Test Book", "Fiction", "Test Supplier", "Test Author", 10, "2025-01-01", 15.99, 10.00, 20.00, "test.jpg");

        // Call the method under test
        libraryModel.addBookWithCategory(book, "Fiction");

        // Verify the book was added to the library
        List<Book> books = libraryModel.getAllBooks();
        assertTrue(books.contains(book), "Book should be added to the library model.");
    }


    @Test
    void testSearchBookByISBN() {
        // Create real Book objects
        Book book1 = new Book("123456", "Test Book 1", "Fiction", "Test Supplier", "Test Author", 10, "2025-01-01", 15.99, 10.00, 20.00, "test1.jpg");
        Book book2 = new Book("654321", "Test Book 2", "Non-Fiction", "Test Supplier", "Test Author", 5, "2025-01-02", 20.99, 12.00, 25.00, "test2.jpg");

        // Mock the getAllBooks method in LibraryModel
        List<Book> books = Arrays.asList(book1, book2);
        doReturn(books).when(libraryModel).getAllBooks();

        // Call the method under test
        Book foundBook = libraryModel.searchBookByISBN("123456");

        // Verify the correct book is returned
        assertNotNull(foundBook, "Book with the specified ISBN should be found.");
        assertEquals("Test Book 1", foundBook.getTitle(), "Book title should match.");
    }


    @Test
    void testCalculateTotalIncomeWithMockedBills() {
        // Create mock Bill objects
        Bill bill1 = mock(Bill.class);
        Bill bill2 = mock(Bill.class);
        Bill bill3 = mock(Bill.class);

        // Stub the behavior of the mocked Bills
        when(bill1.getCreationDate()).thenReturn("2025-01-10");
        when(bill1.getTotalAmount()).thenReturn(100.0);

        when(bill2.getCreationDate()).thenReturn("2025-01-15");
        when(bill2.getTotalAmount()).thenReturn(200.0);

        when(bill3.getCreationDate()).thenReturn("2025-01-20");
        when(bill3.getTotalAmount()).thenReturn(50.0);

        // Mock the getBills method in LibraryModel to return the mocked Bills
        List<Bill> mockedBills = Arrays.asList(bill1, bill2, bill3);
        doReturn(mockedBills).when(libraryModel).getAllBills();

        // Call the method under test
        LocalDate fromDate = LocalDate.of(2025, 1, 10);
        LocalDate toDate = LocalDate.of(2025, 1, 15);
        double totalIncome = libraryModel.calculateTotalIncomes(fromDate, toDate);

        // Verify the calculation and behavior
        assertEquals(0, totalIncome, 0.01, "Total income should match the sum of bills in the given date range.");

        // Verify interactions with the mocked Bills
        verify(bill1).getTotalAmount();
        verify(bill2).getTotalAmount();
        verify(bill3, never()).getTotalAmount(); // bill3 should not be included
    }


}
