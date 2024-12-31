package com.example.javafxlibrarymanagementsystem.controller;

import com.example.javafxlibrarymanagementsystem.model.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

public class AdminController {

    private LibraryModel libraryModel;
    public AdminController(LibraryModel libraryModel) {
        this.libraryModel = libraryModel;
    }

    public void applyPermissions(boolean librarianCanAddBooks, boolean managerCanAddBooks, Stage permissionsStage) {
        // Assume LibraryModel has methods to set these permissions
        libraryModel.setLibrarianCanAddBooks(librarianCanAddBooks);
        libraryModel.setManagerCanAddBooks(managerCanAddBooks);

        // Close the dialog
        permissionsStage.close();
    }

    public void handleCalculateTotalIncomes(LocalDate fromDatePicker, LocalDate toDatePicker, Label totalIncomesLabel) {
        LocalDate fromDate = fromDatePicker;
        LocalDate toDate = toDatePicker;

        if (fromDate == null || toDate == null || fromDate.isAfter(toDate)) {
            showAlert("Invalid date range", "Please select a valid date range.");
            return;
        }

        double totalIncomes = libraryModel.calculateTotalIncomes(fromDate, toDate);
        System.out.println(totalIncomes);
        totalIncomesLabel.setText("Total Incomes: $" + String.format("%.2f", totalIncomes));
    }

    public void handleCalculateTotalCosts(LocalDate fromDatePicker,LocalDate toDatePicker, Label totalCostsLabel) {
        LocalDate fromDate = fromDatePicker;
        LocalDate toDate = toDatePicker;

        if (fromDate == null || toDate == null || fromDate.isAfter(toDate)) {
            showAlert("Invalid date range", "Please select a valid date range.");
            return;
        }

        double totalCosts = libraryModel.calculateTotalCosts(fromDate, toDate);
        System.out.println(totalCosts);
        totalCostsLabel.setText("Total Costs: $" + String.format("%.2f", totalCosts));
    }
    public void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }



    public void handleRegisterUser(String name, String username, String password, String birthday, String phone, String email, String salary, String accessLevel, Stage registerUserStage) {
        // Validate input (add more validation as needed)
        if (name.isEmpty() || username.isEmpty() || password.isEmpty() || birthday.isEmpty() || phone.isEmpty() || email.isEmpty() || salary.isEmpty() || accessLevel.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Please fill in all fields.");
            alert.showAndWait();
            return;
        }

        try {
            Boolean permissionSet= true;
            // Convert salary to double (you may adjust this based on your requirements)
            double parsedSalary = Double.parseDouble(salary);

            User.AccessLevel userAccessLevel = User.AccessLevel.valueOf(accessLevel.toUpperCase());
            if( User.AccessLevel.valueOf(accessLevel.toUpperCase())== User.AccessLevel.LIBRARIAN ){
                permissionSet = false;
            }else{
                permissionSet = true;
            }
            User newUser = new User(name, username, password, birthday, phone, email, parsedSalary, userAccessLevel, permissionSet);
            // Call the registerUser method in the LibraryModel
            libraryModel.addUser(newUser);

            // Show a success alert
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("User Registered");
            alert.setHeaderText("New user registered successfully!");
            alert.showAndWait();


            // Close the register user stage
            registerUserStage.close();

        } catch (NumberFormatException e) {
            // Handle invalid salary input
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Please enter a valid salary.");
            alert.showAndWait();
        }
    }
    public void handleModifyUser(User userToModify, String name, String username, String password, String birthday, String phone, String email, String salary, String accessLevel, Stage modifyUserStage,Boolean addBookPermission) {
        // Validate input (add more validation as needed)
        if (name.isEmpty() || username.isEmpty() || birthday.isEmpty() || phone.isEmpty() || email.isEmpty() || salary.isEmpty() || accessLevel.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Please fill in all fields.");
            alert.showAndWait();
            return;
        }

        try {
            // Create an updated User object
            User updatedUser = new User(
                    name,
                    username,
                    password, // You may choose to update the password or keep it unchanged
                    birthday,
                    phone,
                    email,
                    Double.parseDouble(salary),
                    User.AccessLevel.valueOf(accessLevel),
                    addBookPermission
            );

            // Call the modifyUser method in the LibraryModel
            libraryModel.modifyUser(userToModify, updatedUser);

            // Show a success alert
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("User Modified");
            alert.setHeaderText("User information modified successfully!");
            alert.showAndWait();


            // Close the modify user stage
            modifyUserStage.close();

        } catch (NumberFormatException e) {
            // Handle invalid salary input
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Please enter a valid salary.");
            alert.showAndWait();
        } catch (IllegalArgumentException e) {
            // Handle invalid access level input
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Invalid access level. Please enter a valid access level.");
            alert.showAndWait();
        }
    }


    public void handleDeleteUser(User userToDelete) {
        // Show a confirmation dialog before deleting the user
        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Confirm Deletion");
        confirmationAlert.setHeaderText("Are you sure you want to delete this user?");
        confirmationAlert.setContentText("User: " + userToDelete.getUsername());

        Optional<ButtonType> result = confirmationAlert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            // User confirmed deletion, proceed with deletion
            libraryModel.deleteUser(userToDelete);

            // Show a success message
            Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
            successAlert.setTitle("User Deleted");
            successAlert.setHeaderText("User deleted successfully!");
            successAlert.showAndWait();


        }
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


    public void handleCreateAuthor(Stage createAuthorStage,TextField newAuthorNameField) {
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
