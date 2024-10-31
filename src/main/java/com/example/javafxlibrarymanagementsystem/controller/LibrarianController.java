package com.example.javafxlibrarymanagementsystem.controller;

import com.example.javafxlibrarymanagementsystem.model.Bill;
import com.example.javafxlibrarymanagementsystem.model.BillItem;
import com.example.javafxlibrarymanagementsystem.model.Book;
import com.example.javafxlibrarymanagementsystem.model.LibraryModel;
import javafx.scene.control.Alert;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class LibrarianController {
    private LibraryModel libraryModel;
    public LibrarianController(LibraryModel libraryModel) {
        this.libraryModel = libraryModel;
    }

    public void addBookController( String isbn,String title, String category, String  supplier, String   author, int stock,
                                   String purchasedDate, double sellingPrice, double purchasedPrice, double originalPrice, String bookCoverImagePath){

        // Create a new book with all the details
        Book newBook = new Book(
                isbn,
                title,
                category,
                supplier,
                author,
                stock,
                purchasedDate,
                sellingPrice,
                purchasedPrice,
                originalPrice,
                bookCoverImagePath
        );

        // Add the book to the system
        libraryModel.addBookWithCategory(newBook, category);
    }
    public void saveBillToFile(Bill bill) {
        // Save the bill to a text file with a unique filename (e.g., using date and time)
        String filename = "Bill_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".txt";

        try (FileWriter writer = new FileWriter(filename)) {
            // Write bill details to the file
            writer.write("Bill Number: " + bill.getBillNumber() + "\n");
            writer.write("Date: " + bill.getCreationDate() + "\n");
            writer.write("Total Amount: $" + bill.getTotalAmount() + "\n");

            // Iterate through bill items and write details to the file
            for (BillItem item : bill.getBillItems()) {
                writer.write(item.toString() + "\n");
            }

            System.out.println("Bill saved to file: " + filename);
        } catch (IOException e) {
            e.printStackTrace();
            // Handle the exception, show an alert, or log the error
        }
    }

    public void displayLowStockBooksAlert(List<Book> lowStockBooks) {
        // Create a string representation of low stock books
        StringBuilder alertMessage = new StringBuilder("Low Stock Alert:\n\n");
        for (Book book : lowStockBooks) {
            alertMessage.append(String.format("ISBN: %s, Title: %s, Stock: %d\n",
                    book.getISBN(), book.getTitle(), book.getStock()));
        }

        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Low Stock Alert");
        alert.setHeaderText("Some books are running low on stock.");
        alert.setContentText(alertMessage.toString());
        alert.showAndWait();
    }
}
