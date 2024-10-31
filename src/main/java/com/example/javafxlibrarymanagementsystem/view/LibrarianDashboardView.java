package com.example.javafxlibrarymanagementsystem.view;

import com.example.javafxlibrarymanagementsystem.controller.LibrarianController;
import com.example.javafxlibrarymanagementsystem.model.*;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class LibrarianDashboardView {

    private TableView<Book> booksTable;
    private TableColumn<Book, String> isbnColumn;
    private TableColumn<Book, String> titleColumn;
    private TableColumn<Book, String> authorColumn;
    private TableColumn<Book, Double> sellingPriceColumn;
    private TableColumn<Book, String> categoryColumn;
    private TableColumn<Book, String> supplierColumn;
    private TableColumn<Book, Integer> stockColumn;
    private TableColumn<Book, String> purchasedDateColumn;
    private TableColumn<Book, Double> purchasedPriceColumn;
    private TableColumn<Book, Double> originalPriceColumn;
    private TableColumn<Book, String> bookCoverColumn;
    private Button logoutButton;

    private TextField searchField;
    private Button searchButton;

    private Button checkOutBooksButton;

    private LibraryModel libraryModel;
    private final int LOW_STOCK_THRESHOLD = 5; // Adjust the threshold as needed
    private Timeline lowStockAlertTimeline;

    private Button addBookButton;

    private TextField newBookIsbnField;
    private TextField newBookTitleField;
    private TextField newBookAuthorField;
    private TextField newBookSellingPriceField;
    private TextField newBookCategoryField;
    private  MenuBar menuBar;
    private User user;
    public LibrarianDashboardView(LibraryModel libraryModel) {
        this.libraryModel = libraryModel;
    }

    public void initialize() {
        // Initialize table columns
        isbnColumn = new TableColumn<>("ISBN");
        titleColumn = new TableColumn<>("Title");
        authorColumn = new TableColumn<>("Author");
        sellingPriceColumn = new TableColumn<>("Selling Price");
        categoryColumn = new TableColumn<>("Category");
        supplierColumn = new TableColumn<>("Supplier");
        stockColumn = new TableColumn<>("Stock");
        purchasedDateColumn = new TableColumn<>("Purchased Date");
        purchasedPriceColumn = new TableColumn<>("Purchased Price");
        originalPriceColumn = new TableColumn<>("Original Price");
        bookCoverColumn = new TableColumn<>("Book Cover");

        // Set cell value factories for new columns
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
        supplierColumn.setCellValueFactory(new PropertyValueFactory<>("supplier"));
        stockColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        purchasedDateColumn.setCellValueFactory(new PropertyValueFactory<>("purchasedDate"));
        purchasedPriceColumn.setCellValueFactory(new PropertyValueFactory<>("purchasedPrice"));
        originalPriceColumn.setCellValueFactory(new PropertyValueFactory<>("originalPrice"));
        // Assuming book cover is represented as an image path
        bookCoverColumn.setCellValueFactory(new PropertyValueFactory<>("bookCoverImagePath"));

        isbnColumn.setCellValueFactory(new PropertyValueFactory<>("ISBN"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
        sellingPriceColumn.setCellValueFactory(new PropertyValueFactory<>("sellingPrice"));
        logoutButton = new Button("Logout");
        logoutButton.setOnAction(event -> handleLogout());
        searchField = new TextField();
        searchButton = new Button("Search");
        searchButton.setOnAction(event -> handleSearch());

        addBookButton = new Button("Add Book");
        addBookButton.setOnAction(actionEvent -> openAddBookDialog());

        menuBar = new MenuBar();

        Menu bookMenu = new Menu("Book Operations");
        MenuItem addBookMenuItem = new MenuItem("Add Book");
        MenuItem checkOutBookMenuItem = new MenuItem("Checkout books");
        addBookMenuItem.setOnAction(event -> openAddBookDialog());
        checkOutBookMenuItem.setOnAction(event->handleCheckOutBooks());
        bookMenu.getItems().addAll(addBookMenuItem, checkOutBookMenuItem);

        // Add menus to the menu bar
        menuBar.getMenus().addAll(bookMenu);

// Initialize the timeline for automatic low stock alerts
        initializeLowStockAlertsTimeline();
        // Initialize books table
        booksTable = new TableView<>();
        booksTable.getColumns().addAll(isbnColumn, titleColumn, authorColumn, sellingPriceColumn, categoryColumn,
                supplierColumn, stockColumn, purchasedDateColumn, purchasedPriceColumn, originalPriceColumn, bookCoverColumn);

        // Populate books table
        updateBooksTable();
        checkOutBooksButton = new Button("Check Out Books");
    }

    private void updateBooksTable() {
        ObservableList<Book> books = FXCollections.observableArrayList(libraryModel.getAllBooks());
        booksTable.setItems(books);
        booksTable.refresh(); // Add this line to force a refresh

    }

    private void openAddBookDialog() {

        if(user.getCanAddBooksPermission()) {
            // Create a new stage for the add book dialog
            Stage addBookStage = new Stage();
            addBookStage.setTitle("Add Book");

            // Initialize components for adding a new book
            newBookIsbnField = new TextField();
            newBookIsbnField.setPromptText("ISBN");

            newBookTitleField = new TextField();
            newBookTitleField.setPromptText("Title");

            newBookAuthorField = new TextField();
            newBookAuthorField.setPromptText("Author");

            newBookSellingPriceField = new TextField();
            newBookSellingPriceField.setPromptText("Selling Price");

            newBookCategoryField = new TextField();
            newBookCategoryField.setPromptText("Category");

            addBookButton = new Button("Add Book");
            addBookButton.setOnAction(event -> handleAddBook(addBookStage));

            // Add components to the layout
            VBox addBookRoot = new VBox(10);
            addBookRoot.setPadding(new Insets(20));
            addBookRoot.getChildren().addAll(newBookIsbnField, newBookTitleField, newBookAuthorField,
                    newBookSellingPriceField, newBookCategoryField, addBookButton);

            Scene addBookScene = new Scene(addBookRoot, 400, 300);
            addBookStage.setScene(addBookScene);

            // Show the add book stage
            addBookStage.show();
        }else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("You dont have permission to add Books");
            alert.showAndWait();
        }
    }

    private void handleAddBook(Stage addBookStage) {
        String isbn = newBookIsbnField.getText().trim();
        String title = newBookTitleField.getText().trim();
        String author = newBookAuthorField.getText().trim();
        String sellingPriceStr = newBookSellingPriceField.getText().trim();
        String category = newBookCategoryField.getText().trim();

        try {
            double sellingPrice = Double.parseDouble(sellingPriceStr);

            // Check if the category already exists
            if (!libraryModel.getBookCategories().contains(category)) {
                // If not, add the new category
                libraryModel.addBookCategory(category);
            }

            // Prompt the manager for the remaining details using a single dialog box
            Dialog<Void> detailsDialog = new Dialog<>();
            detailsDialog.setTitle("Book Details");
            detailsDialog.setHeaderText("Enter the details for the new book");

            // Set the button types
            ButtonType addButton = new ButtonType("Add Book", ButtonBar.ButtonData.OK_DONE);
            detailsDialog.getDialogPane().getButtonTypes().addAll(addButton, ButtonType.CANCEL);

            // Create and add text fields
            TextField supplierField = new TextField();
            supplierField.setPromptText("Supplier");
            supplierField.setMinWidth(200);

            TextField stockField = new TextField();
            stockField.setPromptText("Stock");
            stockField.setMinWidth(200);

            TextField purchasedDateField = new TextField();
            purchasedDateField.setPromptText("Purchased Date");
            purchasedDateField.setMinWidth(200);

            TextField purchasedPriceField = new TextField();
            purchasedPriceField.setPromptText("Purchased Price");
            purchasedPriceField.setMinWidth(200);

            TextField originalPriceField = new TextField();
            originalPriceField.setPromptText("Original Price");
            originalPriceField.setMinWidth(200);

            TextField bookCoverImagePathField = new TextField();
            bookCoverImagePathField.setPromptText("Book Cover Image Path");
            bookCoverImagePathField.setMinWidth(200);

            detailsDialog.getDialogPane().setContent(new VBox(10, supplierField, stockField, purchasedDateField, purchasedPriceField, originalPriceField, bookCoverImagePathField));

            // Convert the result to a button type when the add button is clicked
            detailsDialog.setResultConverter(dialogButton -> {
                if (dialogButton == addButton) {
                    return null;
                }
                return null;
            });

            // Show the dialog and wait for the user's response
            detailsDialog.showAndWait();

            // Retrieve the values from the text fields
            String supplier = supplierField.getText().trim();
            int stock = Integer.parseInt(stockField.getText().trim());
            String purchasedDate = purchasedDateField.getText().trim();
            double purchasedPrice = Double.parseDouble(purchasedPriceField.getText().trim());
            double originalPrice = Double.parseDouble(originalPriceField.getText().trim());
            String bookCoverImagePath = bookCoverImagePathField.getText().trim();

            // Create a new book with all the details
            LibrarianController librarianController = new LibrarianController(libraryModel);
            librarianController.addBookController(   isbn,
                    title,
                    category,
                    supplier,
                    author,
                    stock,
                    purchasedDate,
                    sellingPrice,
                    purchasedPrice,
                    originalPrice,
                    bookCoverImagePath);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Book Added");
            alert.setHeaderText("New book added successfully!");
            alert.showAndWait();

            // Refresh the books table after adding a new book
            updateBooksTable();

            // Clear the input fields
            newBookIsbnField.clear();
            newBookTitleField.clear();
            newBookAuthorField.clear();
            newBookSellingPriceField.clear();
            newBookCategoryField.clear();

            // Close the add book stage
            addBookStage.close();
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Please enter valid numeric values for stock, selling price, purchased price, and original price.");
            alert.showAndWait();
        }
    }
    private void handleCheckOutBooks() {
        // Get selected book(s) from the table
        Book selectedBook = booksTable.getSelectionModel().getSelectedItem();

        if (selectedBook != null) {
            // Prompt the librarian to enter the quantity
            TextInputDialog dialog = new TextInputDialog("1");
            dialog.setTitle("Check Out Books");
            dialog.setHeaderText("Enter quantity to check out for " + selectedBook.getTitle());
            dialog.setContentText("Quantity:");

            Optional<String> result = dialog.showAndWait();

            result.ifPresent(quantityStr -> {
                try {
                    int quantity = Integer.parseInt(quantityStr);

                    // Check if there are enough items in stock
                    if (selectedBook.getStock() >= quantity) {
                        // Create a bill item
                        BillItem billItem = new BillItem(selectedBook.getISBN(), selectedBook.getTitle(),
                                quantity, selectedBook.getSellingPrice());

                        // Call a method in the library model to handle the check-out process
                        Bill bill = libraryModel.checkOutBooks(selectedBook, billItem);

                        // Display the total amount of the bill to the librarian
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Bill Information");
                        alert.setHeaderText("Bill created successfully!");
                        alert.setContentText("Total Amount: $" + bill.getTotalAmount());
                        alert.showAndWait();


                        LibrarianController librarianController = new LibrarianController(libraryModel);
                        // Save the bill to a text file
                        librarianController.saveBillToFile(bill);

                        // Update the books table
                        updateBooksTable();
                    } else {
                        // Insufficient stock, show an alert
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setTitle("Insufficient Stock");
                        alert.setHeaderText("There are not enough items in stock for " + selectedBook.getTitle());
                        alert.showAndWait();
                    }
                } catch (NumberFormatException e) {
                    // Handle invalid input
                    e.printStackTrace();
                    // You might show an alert to the user about the invalid input
                }
            });
        } else {
            // No book selected, show an alert
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Book Selected");
            alert.setHeaderText("Please select a book to check out.");
            alert.showAndWait();
        }
    }



    // New method to display the Librarian Dashboard
    public void display(User user) {
        this.user= user;
        initialize(); // Ensure initialize is called explicitly

        Stage stage = new Stage();

        VBox root = new VBox(10);
        root.setPadding(new Insets(20));

        // Add search components to the UI
        HBox searchBox = new HBox(10, new Label("Search ISBN:"), searchField, searchButton);
        Label titleLabel = new Label("Librarian Dashboard");
        titleLabel.setStyle("-fx-font-size: 18; -fx-font-weight: bold;");
        root.getChildren().addAll(titleLabel,menuBar, searchBox, booksTable ,logoutButton);
        Scene scene = new Scene(root, 800, 600);

        stage.setScene(scene);
        stage.setTitle("Librarian Dashboard");

        stage.show();
    }


    private void handleLogout() {
        // Close the current stage (Librarian Dashboard)
        Stage stage = (Stage) logoutButton.getScene().getWindow();
        stage.close();

        // Open the login screen
        openLoginScreen();
    }
    private void openLoginScreen() {
        LoginView loginView = new LoginView(new Stage(), libraryModel);
        loginView.show();
    }
    private void handleSearch() {
        String searchISBN = searchField.getText().trim();

        if (!searchISBN.isEmpty()) {
            // Call a method in the library model to search for the book by ISBN
            Book foundBook = libraryModel.searchBookByISBN(searchISBN);

            if (foundBook != null) {
                // Book found, update the table and display information
                ObservableList<Book> foundBooks = FXCollections.observableArrayList(foundBook);
                booksTable.setItems(foundBooks);
                booksTable.refresh();

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Book Found");
                alert.setHeaderText("Book found with ISBN: " + searchISBN);
                alert.setContentText("Title: " + foundBook.getTitle() + "\nAuthor: " + foundBook.getAuthor()
                        + "\nStock: " + foundBook.getStock());
                alert.showAndWait();
            } else {
                // Book not found, show an alert
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Book Not Found");
                alert.setHeaderText("No book found with ISBN: " + searchISBN);
                alert.showAndWait();
            }
        } else {
            // Empty search field, show an alert
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Empty Search Field");
            alert.setHeaderText("Please enter an ISBN to search for a book.");
            alert.showAndWait();
        }
    }


    private void initializeLowStockAlertsTimeline() {
        // Set up a timeline to check for low stock every 1 second
        lowStockAlertTimeline = new Timeline(
                new KeyFrame(Duration.seconds(120), event -> {
                    System.out.println("Timer event fired!");
                    receiveLowStockAlerts();
                })
        );
        lowStockAlertTimeline.setCycleCount(Timeline.INDEFINITE);
        lowStockAlertTimeline.play();
    }

    private void receiveLowStockAlerts() {
        Platform.runLater(() -> {
            // Get the list of all books with low stock
            List<Book> lowStockBooks = libraryModel.getLowStockBooks(LOW_STOCK_THRESHOLD);

            if (!lowStockBooks.isEmpty()) {
                // Create and show an automatic alert to display books with low stock
                LibrarianController librarianController = new LibrarianController(libraryModel);
                // Save the bill to a text file
                librarianController.displayLowStockBooksAlert(lowStockBooks);

            }
        });
    }


}
