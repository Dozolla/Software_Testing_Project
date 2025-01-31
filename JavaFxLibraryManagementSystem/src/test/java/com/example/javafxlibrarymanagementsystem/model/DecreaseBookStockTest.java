package com.example.javafxlibrarymanagementsystem.model;

import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

///  Testing the decreaseBookStock method in the Book class and achieved 100% coverage for that method.

public class DecreaseBookStockTest {

    @Test
    public void testDecreaseBookStock_NullBooksList() {
        List<Book> books = null;
        String ISBN = "1234567890";
        int quantity = 1;
        boolean result = Book.decreaseBookStock(books, ISBN, quantity);
        assertFalse(result, "Expected false when books list is null.");
    }

    @Test
    public void testDecreaseBookStock_NullISBN() {
        List<Book> books = createSampleBooks();
        String ISBN = null;
        int quantity = 1;
        boolean result = Book.decreaseBookStock(books, ISBN, quantity);
        assertFalse(result, "Expected false when ISBN is null.");
    }

    @Test
    public void testDecreaseBookStock_EmptyISBN() {
        List<Book> books = createSampleBooks();
        String ISBN = "";
        int quantity = 1;
        boolean result = Book.decreaseBookStock(books, ISBN, quantity);
        assertFalse(result, "Expected false when ISBN is empty.");
    }

    @Test
    public void testDecreaseBookStock_NegativeQuantity() {
        List<Book> books = createSampleBooks();
        String ISBN = "1234567890";
        int quantity = -1;
        boolean result = Book.decreaseBookStock(books, ISBN, quantity);
        assertFalse(result, "Expected false when quantity is negative.");
    }

    @Test
    public void testDecreaseBookStock_ZeroQuantity() {
        List<Book> books = createSampleBooks();
        String ISBN = "1234567890";
        int quantity = 0;
        boolean result = Book.decreaseBookStock(books, ISBN, quantity);
        assertFalse(result, "Expected false when quantity is zero.");
    }

    @Test
    public void testDecreaseBookStock_BookNotFound() {
        List<Book> books = createSampleBooks();
        String ISBN = "0000000000"; // ISBN not in the list
        int quantity = 1;
        boolean result = Book.decreaseBookStock(books, ISBN, quantity);
        assertFalse(result, "Expected false when book is not found.");
    }

    @Test
    public void testDecreaseBookStock_ValidDecrease() {
        List<Book> books = createSampleBooks();
        String ISBN = "1234567890";
        int quantity = 2;
        boolean result = Book.decreaseBookStock(books, ISBN, quantity);
        assertTrue(result, "Expected true when stock is successfully decreased.");
        assertEquals(3, books.get(0).getStock(), "Expected stock to be decreased by 2.");
    }

    @Test
    public void testDecreaseBookStock_QuantityExceedsStock() {
        List<Book> books = createSampleBooks();
        String ISBN = "1234567890";
        int quantity = 10; // Exceeds current stock
        boolean result = Book.decreaseBookStock(books, ISBN, quantity);
        assertTrue(result, "Expected true even when quantity exceeds current stock.");
        assertEquals(0, books.get(0).getStock(), "Expected stock to be set to 0.");
    }

    private List<Book> createSampleBooks() {
        List<Book> books = new ArrayList<>();
        books.add(new Book("1234567890", "Sample Book 1", "Fiction", "Supplier A", "Author A", 5, "2025-01-01", 10.0, 15.0, 20.0, "path/to/cover1.jpg"));
        books.add(new Book("0987654321", "Sample Book 2", "Non-Fiction", "Supplier B", "Author B", 3, "2025-01-02", 12.0, 18.0, 25.0, "path/to/cover2.jpg"));
        return books;
    }
}
