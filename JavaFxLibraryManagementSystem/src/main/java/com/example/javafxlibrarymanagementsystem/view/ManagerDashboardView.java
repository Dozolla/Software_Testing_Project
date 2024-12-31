package com.example.javafxlibrarymanagementsystem.view;

import com.example.javafxlibrarymanagementsystem.controller.ManagerController;
import com.example.javafxlibrarymanagementsystem.model.LibraryModel;
import com.example.javafxlibrarymanagementsystem.model.Book;
import com.example.javafxlibrarymanagementsystem.model.User;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ManagerDashboardView {

    private TableView<Book> booksTable;
    private TableColumn<Book, String> isbnColumn;
    private TableColumn<Book, String> titleColumn;
    private TableColumn<Book, String> authorColumn;
    private TableColumn<Book, Double> sellingPriceColumn;
    private TableColumn<Book, String> categoryColumn;

    private TextField newBookIsbnField;
    private TextField newBookTitleField;
    private TextField newBookAuthorField;
    private TextField newBookSellingPriceField;
    private TextField newBookCategoryField;
    private Button addBookButton;
    private TextField newAuthorNameField;
    private Button createAuthorButton;
    private Button addCategoryButton;
    private TextField newCategoryField;
    private Button createCategoryButton;
    private Button showAuthorsButton;
    private Button showCategoriesButton;
    private Button addBooksToStockButton;
    private TextField categoryForStockField;
    private TextField quantityForStockField;
    private Button showCategoriesWithStockButton;
    // Add UI components for checking librarian performance
    private DatePicker fromDateField;
    private DatePicker toDateField;
    private Button checkPerformanceButton;

    private Label totalBillsLabel;
    private Label totalBooksSoldLabel;
    private Label totalAmountLabel;
    private Timeline lowStockAlertTimeline;
    private Button checkPerformanceButtonCall;
    // Add UI components for book statistics
    private Button bookStatisticsButton;
    private Button logoutButton; // New button for logout

    private Label totalBooksBoughtLabel;
    private LibraryModel libraryModel;
    private final int LOW_STOCK_THRESHOLD = 5; // Adjust the threshold as needed
    private User user;
    public ManagerDashboardView(LibraryModel libraryModel) {
        this.libraryModel = libraryModel;
    }

    ManagerController managerController;

    public void initialize() {

        managerController = new ManagerController(libraryModel);
        // Initialize table columns
        isbnColumn = new TableColumn<>("ISBN");
        titleColumn = new TableColumn<>("Title");
        authorColumn = new TableColumn<>("Author");
        sellingPriceColumn = new TableColumn<>("Selling Price");
        categoryColumn = new TableColumn<>("Category");

        isbnColumn.setCellValueFactory(new PropertyValueFactory<>("ISBN"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
        sellingPriceColumn.setCellValueFactory(new PropertyValueFactory<>("sellingPrice"));
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
        showCategoriesWithStockButton = new Button("Show Categories with Stock");
        showCategoriesWithStockButton.setOnAction(event -> showCategoriesWithStock());

        // Initialize books table
        booksTable = new TableView<>();
        booksTable.getColumns().addAll(isbnColumn, titleColumn, authorColumn, sellingPriceColumn, categoryColumn);
        bookStatisticsButton = new Button("Book Statistics");
        bookStatisticsButton.setOnAction(event -> openBookStatisticsDialog());

        // Populate books table
        updateBooksTable();

        // Initialize button for adding a new book
        Button addBookButton = new Button("Add Book");
        addBookButton.setOnAction(event -> openAddBookDialog());

        // Initialize button for creating a new author
        createAuthorButton = new Button("Create New Author");
        createAuthorButton.setOnAction(event -> openCreateAuthorDialog());
        addCategoryButton = new Button("Add Category");
        addCategoryButton.setOnAction(event -> openAddCategoryDialog());
        // Initialize button for showing all authors
        showAuthorsButton = new Button("Show All Authors");
        showAuthorsButton.setOnAction(event -> managerController.showAllAuthors());

        // Initialize button for showing all categories
        showCategoriesButton = new Button("Show All Categories");
        showCategoriesButton.setOnAction(event -> managerController.showAllCategories());
        addBooksToStockButton = new Button("Add Books to Stock");
        addBooksToStockButton.setOnAction(event -> openAddBooksToStockDialog());


// Initialize the timeline for automatic low stock alerts
        initializeLowStockAlertsTimeline();

// Manually trigger low stock alert for testing
      //  receiveLowStockAlerts();



        // Initialize the button for checking librarian performance
        checkPerformanceButtonCall = new Button("Check Performance");
        checkPerformanceButtonCall.setOnAction(event -> openPerformanceCheckDialog());
        logoutButton = new Button("Logout");
        logoutButton.setOnAction(event -> logout());


        // Add components to the layout
        VBox root = new VBox(10);
        root.setPadding(new Insets(20));

        Label titleLabel = new Label("Manager Dashboard");
        titleLabel.setStyle("-fx-font-size: 18; -fx-font-weight: bold;");


        // Create a menu bar
        MenuBar menuBar = new MenuBar();

        // Create menus
        Menu bookMenu = new Menu("Books");
        Menu authorMenu = new Menu("Authors");
        Menu categoryMenu = new Menu("Categories");
        Menu stockMenu = new Menu("Stock");
        Menu performanceMenu = new Menu("Performance");
        Menu statisticsMenu = new Menu("Statistics");

        // Create menu items
        MenuItem addBookMenuItem = new MenuItem("Add Book");
        MenuItem createAuthorMenuItem = new MenuItem("Create New Author");
        MenuItem addCategoryMenuItem = new MenuItem("Add Category");
        MenuItem showAuthorsMenuItem = new MenuItem("Show All Authors");
        MenuItem showCategoriesMenuItem = new MenuItem("Show All Categories");
        MenuItem addBooksToStockMenuItem = new MenuItem("Add Books to Stock");
        MenuItem showCategoriesWithStockMenuItem = new MenuItem("show Categories with Stock");
        MenuItem checkPerformanceMenuItem = new MenuItem("Check Performance");
        MenuItem bookStatisticsMenuItem = new MenuItem("Book Statistics");
           // Set actions for menu items
        addBookMenuItem.setOnAction(event -> openAddBookDialog());
        createAuthorMenuItem.setOnAction(event -> openCreateAuthorDialog());
        addCategoryMenuItem.setOnAction(event -> openAddCategoryDialog());
        showAuthorsMenuItem.setOnAction(event -> managerController.showAllAuthors());
        showCategoriesMenuItem.setOnAction(event -> managerController.showAllCategories());
        addBooksToStockMenuItem.setOnAction(event -> openAddBooksToStockDialog());
        checkPerformanceMenuItem.setOnAction(event -> openPerformanceCheckDialog());
        bookStatisticsMenuItem.setOnAction(event -> openBookStatisticsDialog());
        showCategoriesWithStockMenuItem.setOnAction(event -> showCategoriesWithStock());
        // Add menu items to menus
        bookMenu.getItems().addAll(addBookMenuItem, showAuthorsMenuItem, showCategoriesMenuItem,showCategoriesWithStockMenuItem);
        authorMenu.getItems().addAll(createAuthorMenuItem);
        categoryMenu.getItems().addAll(addCategoryMenuItem);
        stockMenu.getItems().addAll(addBooksToStockMenuItem);
        performanceMenu.getItems().addAll(checkPerformanceMenuItem);
        statisticsMenu.getItems().addAll(bookStatisticsMenuItem);

              // Add menus to the menu bar
        menuBar.getMenus().addAll(bookMenu, authorMenu, categoryMenu, stockMenu, performanceMenu, statisticsMenu);


        root.getChildren().addAll(menuBar, titleLabel, booksTable,logoutButton);
        Scene scene = new Scene(root, 800, 600);

        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Manager Dashboard");

        stage.show();
    }

    private void updateBooksTable() {
        ObservableList<Book> books = FXCollections.observableArrayList(libraryModel.getAllBooks());
        booksTable.setItems(books);
        booksTable.refresh();
    }
    private void openCreateAuthorDialog() {
        // Create a new stage for the create author dialog
        Stage createAuthorStage = new Stage();
        createAuthorStage.setTitle("Create New Author");

        // Initialize components for creating a new author
        newAuthorNameField = new TextField();
        newAuthorNameField.setPromptText("Author Name");

        createAuthorButton = new Button("Create Author");
        createAuthorButton.setOnAction(event -> managerController.handleCreateAuthor(createAuthorStage, newAuthorNameField));

        // Add components to the layout
        VBox createAuthorRoot = new VBox(10);
        createAuthorRoot.setPadding(new Insets(20));
        createAuthorRoot.getChildren().addAll(newAuthorNameField, createAuthorButton);

        Scene createAuthorScene = new Scene(createAuthorRoot, 400, 200);
        createAuthorStage.setScene(createAuthorScene);

        // Show the create author stage
        createAuthorStage.show();
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

            managerController.addBookController( isbn,
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

    private void openAddCategoryDialog() {
        // Create a new stage for the add category dialog
        Stage addCategoryStage = new Stage();
        addCategoryStage.setTitle("Add Category");

        // Initialize components for adding a new category
        newCategoryField = new TextField();
        newCategoryField.setPromptText("Category Name");

        createCategoryButton = new Button("Add Category");
        createCategoryButton.setOnAction(event -> managerController.handleAddCategory(addCategoryStage,newCategoryField));

        // Add components to the layout
        VBox addCategoryRoot = new VBox(10);
        addCategoryRoot.setPadding(new Insets(20));
        addCategoryRoot.getChildren().addAll(newCategoryField, createCategoryButton);

        Scene addCategoryScene = new Scene(addCategoryRoot, 400, 200);
        addCategoryStage.setScene(addCategoryScene);

        // Show the add category stage
        addCategoryStage.show();
    }




    private void openAddBooksToStockDialog() {
        // Create a new stage for the add books to stock dialog
        Stage addBooksToStockStage = new Stage();
        addBooksToStockStage.setTitle("Add Books to Stock");

        // Initialize components for adding books to stock
        categoryForStockField = new TextField();
        categoryForStockField.setPromptText("Category Name");

        quantityForStockField = new TextField();
        quantityForStockField.setPromptText("Quantity");

        Button addBooksToStockConfirmButton = new Button("Add Books to Stock");
        addBooksToStockConfirmButton.setOnAction(event -> handleAddBooksToStock(addBooksToStockStage));

        // Add components to the layout
        VBox addBooksToStockRoot = new VBox(10);
        addBooksToStockRoot.setPadding(new Insets(20));
        addBooksToStockRoot.getChildren().addAll(categoryForStockField, quantityForStockField, addBooksToStockConfirmButton);

        Scene addBooksToStockScene = new Scene(addBooksToStockRoot, 400, 200);
        addBooksToStockStage.setScene(addBooksToStockScene);

        // Show the add books to stock stage
        addBooksToStockStage.show();
    }

    private void handleAddBooksToStock(Stage addBooksToStockStage) {
        String categoryName = categoryForStockField.getText().trim();
        String quantityStr = quantityForStockField.getText().trim();

        try {
            int quantity = Integer.parseInt(quantityStr);

            // Check if the category exists
            if (libraryModel.getBookCategories().contains(categoryName)) {
                // Add the specified quantity of books to the stock for the category
                libraryModel.addBooksToStock(categoryName, quantity);

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Books Added to Stock");
                alert.setHeaderText("Books added to stock successfully!");
                alert.showAndWait();

                // Close the add books to stock stage
                addBooksToStockStage.close();

                // Refresh the books table after adding books to stock
                updateBooksTable();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Please enter a valid category name.");
                alert.showAndWait();
            }
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Please enter a valid numeric value for quantity.");
            alert.showAndWait();
        }
    }

    private void showCategoriesWithStock() {
        // Get the list of all categories from the library model
        List<String> categories = libraryModel.getBookCategories();

        // Create a list to hold category information with stock numbers
        List<String> categoriesWithStock = new ArrayList<>();

        // Populate the list with category information
        for (String category : categories) {
            int stock = calculateStockForCategory(category);
            categoriesWithStock.add(category + " - Stock: " + stock);
        }

        // Create and show a dialog to display categories with stock numbers
        managerController.displayListDialog("Categories with Stock", categoriesWithStock);
    }

    private int calculateStockForCategory(String category) {
        // Calculate the total stock for the given category
        List<Book> booksInCategory = libraryModel.getAllBooks().stream()
                .filter(book -> book.getCategory().equalsIgnoreCase(category))
                .collect(Collectors.toList());

        int totalStock = booksInCategory.stream().mapToInt(Book::getStock).sum();
        return totalStock;
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
                managerController.displayLowStockBooksAlert(lowStockBooks);
            }
        });
    }


    private void openPerformanceCheckDialog() {
        // Create a new stage for the performance check dialog
        Stage performanceCheckStage = new Stage();
        performanceCheckStage.setTitle("Librarian Performance Check");

        // Initialize components for the performance check
        // You can use the existing code for checkLibrarianPerformance in this dialog if needed
        Label performanceLabel = new Label("Performance Check");
        // Add more components as needed
        fromDateField = new DatePicker();
        toDateField = new DatePicker();
        checkPerformanceButton = new Button("Check Performance");
        checkPerformanceButton.setOnAction(event -> checkLibrarianPerformance());

        totalBillsLabel = new Label("Total Bills: ");
        totalBooksSoldLabel = new Label("Total Books Sold: ");
        totalAmountLabel = new Label("Total Amount: ");

        // Add components to the layout
        VBox performanceCheckRoot = new VBox(10);
        performanceCheckRoot.setPadding(new Insets(20));
        performanceCheckRoot.getChildren().addAll(
                // ... (existing components),
                new HBox(10, performanceLabel,new Label("From Date:"), fromDateField, new Label("To Date:"), toDateField, checkPerformanceButton),
                totalBillsLabel, totalBooksSoldLabel, totalAmountLabel
        );


        Scene performanceCheckScene = new Scene(performanceCheckRoot, 900, 200);
        performanceCheckStage.setScene(performanceCheckScene);

        // Show the performance check stage
        performanceCheckStage.show();
    }

    private void checkLibrarianPerformance() {
        LocalDate fromDate = fromDateField.getValue();
        LocalDate toDate = toDateField.getValue();

        if (fromDate != null && toDate != null) {
            // Get the performance metrics from the library model
            int totalBills = libraryModel.getTotalBills(fromDate, toDate);
            int totalBooksSold = libraryModel.getTotalBooksSold(fromDate, toDate);
            double totalAmount = libraryModel.getTotalAmount(fromDate, toDate);

            // Display the performance metrics
            totalBillsLabel.setText("Total Bills: " + totalBills);
            totalBooksSoldLabel.setText("Total Books Sold: " + totalBooksSold);
            totalAmountLabel.setText("Total Amount: $" + totalAmount);
        } else {
            // Display an error message if the date fields are not filled
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Please select both From Date and To Date.");
            alert.showAndWait();
        }
    }
    private void openBookStatisticsDialog() {
        // Create a new stage for the book statistics dialog
        Stage bookStatisticsStage = new Stage();
        bookStatisticsStage.setTitle("Book Statistics");

        // Initialize components for book statistics
        DatePicker fromDateField = new DatePicker();
        DatePicker toDateField = new DatePicker();
        Button checkBookStatisticsButton = new Button("Check Book Statistics");
        checkBookStatisticsButton.setOnAction(event -> checkBookStatistics(fromDateField.getValue(), toDateField.getValue()));

        totalBooksSoldLabel = new Label("Total Books Sold: ");
        totalBooksBoughtLabel = new Label("Total Books Bought: ");
        totalAmountLabel = new Label("Total Amount: ");

        // Add components to the layout
        VBox bookStatisticsRoot = new VBox(10);
        bookStatisticsRoot.setPadding(new Insets(20));
        bookStatisticsRoot.getChildren().addAll(
                new HBox(10, new Label("From Date:"), fromDateField, new Label("To Date:"), toDateField, checkBookStatisticsButton),
                totalBooksSoldLabel, totalBooksBoughtLabel, totalAmountLabel
        );

        Scene bookStatisticsScene = new Scene(bookStatisticsRoot, 900, 200);
        bookStatisticsStage.setScene(bookStatisticsScene);

        // Show the book statistics stage
        bookStatisticsStage.show();
    }
    private void checkBookStatistics(LocalDate fromDate, LocalDate toDate) {
        if (fromDate != null && toDate != null) {
            // Get the book statistics from the library model
            int totalBooksSold = libraryModel.getTotalBooksSold(fromDate, toDate);
            int totalBooksBought = libraryModel.getTotalBooksBought(fromDate, toDate);
            double totalAmount = libraryModel.getTotalAmount(fromDate, toDate);
System.out.println(totalBooksSold +" "+totalBooksBought+" "+totalAmount );
            Platform.runLater(() -> {
                totalBooksSoldLabel.setText("Total Books Sold: " + totalBooksSold);
                totalBooksBoughtLabel.setText("Total Books Bought: " + totalBooksBought);
                totalAmountLabel.setText("Total Amount: $" + totalAmount);
            });
        } else {
            // Display an error message if the date fields are not filled
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Please select both From Date and To Date.");
            alert.showAndWait();
        }
    }
    private void logout() {
        System.out.println("here");
        // Close the current stage (dashboard view)
        Stage currentStage = (Stage) logoutButton.getScene().getWindow();
        currentStage.close();

        // Open the login screen
        openLoginScreen();
    }
    private void openLoginScreen() {
        // Create a new stage for the login screen
        Stage loginStage = new Stage();

        // Create an instance of the LoginView with the LibraryModel
        LoginView loginView = new LoginView(loginStage, libraryModel);

        // Display the login screen
        loginView.show();
    }
    // Display method
    public void display(User user) {
        this.user= user;
        initialize();
    }
}
