package com.example.javafxlibrarymanagementsystem.controller;

import com.example.javafxlibrarymanagementsystem.model.Bill;
import com.example.javafxlibrarymanagementsystem.model.BillItem;
import com.example.javafxlibrarymanagementsystem.model.Book;
import com.example.javafxlibrarymanagementsystem.model.LibraryModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ManagerController {
    private LibraryModel libraryModel;
    public ManagerController(LibraryModel libraryModel) {
        this.libraryModel = libraryModel;
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


    public void handleCreateAuthor(Stage createAuthorStage, TextField newAuthorNameField) {
        String authorName = newAuthorNameField.getText().trim();

        if (!authorName.isEmpty()) {
            libraryModel.addAuthor(authorName);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Author Created");
            alert.setHeaderText("New author created successfully!");
            alert.showAndWait();

            // Close the create author stage
            createAuthorStage.close();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Please enter a valid author name.");
            alert.showAndWait();
        }
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


    public void handleAddCategory(Stage addCategoryStage, TextField newCategoryField) {
        String categoryName = newCategoryField.getText().trim();

        if (!categoryName.isEmpty()) {
            libraryModel.addBookCategory(categoryName);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Category Added");
            alert.setHeaderText("New category added successfully!");
            alert.showAndWait();

            // Close the add category stage
            addCategoryStage.close();


        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Please enter a valid category name.");
            alert.showAndWait();
        }
    }

    public void showAllAuthors() {
        // Get the list of all authors from the library model
        List<String> authors = libraryModel.getAllAuthors();

        // Create and show a dialog to display all authors
        displayListDialog("All Authors", authors);
    }

    public void showAllCategories() {
        // Get the list of all categories from the library model
        List<String> categories = libraryModel.getBookCategories();

        // Create and show a dialog to display all categories
        displayListDialog("All Categories", categories);
    }

    public void displayListDialog(String title, List<String> items) {
        // Create a new stage for the list dialog
        Stage listDialogStage = new Stage();
        listDialogStage.setTitle(title);

        // Initialize components for displaying the list
        ListView<String> listView = new ListView<>();
        ObservableList<String> observableList = FXCollections.observableArrayList(items);
        listView.setItems(observableList);

        // Add components to the layout
        VBox listDialogRoot = new VBox(10);
        listDialogRoot.setPadding(new Insets(20));
        listDialogRoot.getChildren().add(listView);

        Scene listDialogScene = new Scene(listDialogRoot, 400, 400);
        listDialogStage.setScene(listDialogScene);

        // Show the list dialog stage
        listDialogStage.show();
    }
}
