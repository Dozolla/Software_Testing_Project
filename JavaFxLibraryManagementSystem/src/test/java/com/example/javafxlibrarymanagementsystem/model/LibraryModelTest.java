package com.example.javafxlibrarymanagementsystem.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class LibraryModelTest {

    @Test
    public void testAddBookWithMock() {
        // Create the mocked Book object
        Book mockedBook = mock(Book.class);

        // Set up mock behavior for the Book (optional)
        when(mockedBook.getISBN()).thenReturn("123456789");
        when(mockedBook.getAuthor()).thenReturn("F. Scott Fitzgerald");

        // Create the LibraryModel instance
        LibraryModel libraryModel = new LibraryModel();

        // Add the mocked Book to the LibraryModel
        libraryModel.addBook(mockedBook);

        // Verify that the book was added
        assertTrue(libraryModel.getAllBooks().contains(mockedBook), "Mocked book was not successfully added to the library model.");

        // Verify interactions with the mock (optional)
        verify(mockedBook, atLeastOnce()).getISBN();
        verify(mockedBook, atLeastOnce()).getAuthor();
    }
}
