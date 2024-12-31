package com.example.javafxlibrarymanagementsystem.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class LibraryModelTest {
    // Test
    @Test
    public void testAddBook() {
        // Create the Library Model
        LibraryModel libraryModel = new LibraryModel();

//        Book b = mock(Book.class);


        // Create the Book
        Book book = new Book("123456789", "F. Scott Fitzgerald", "good", "Noci", "Ismalin",10, "12.12.2007",2.0,3.0, 4.0, "" );

        // Add the Book
        libraryModel.addBook(book);


        // Check if the Book was added
        assertTrue(libraryModel.getAllBooks().contains(book));

    }

}
