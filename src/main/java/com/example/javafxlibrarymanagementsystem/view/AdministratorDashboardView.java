package com.example.javafxlibrarymanagementsystem.view;

import com.example.javafxlibrarymanagementsystem.controller.AdminController;
import com.example.javafxlibrarymanagementsystem.model.*;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class AdministratorDashboardView {
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

    private Label totalBooksBoughtLabel;
    private Button logoutButton;

    private TextField searchField;
    private Button searchButton;
    private TableView<User> usersTable;
    private TableColumn<User, String> nameColumn;
    private TableColumn<User, String> birthdayColumn;
    private TableColumn<User, String> phoneColumn;
    private TableColumn<User, String> emailColumn;
    private TableColumn<User, Double> salaryColumn; // Assuming salary is common to all users
    private TableColumn<User, String> accessLevelColumn;

    private  Label totalIncomesLabel;
    private Label totalCostsLabel;
    private LibraryModel libraryModel;
    private final int LOW_STOCK_THRESHOLD = 5; // Adjust the threshold as needed
    private Timeline lowStockAlertTimeline;
    private boolean librarianCanAddBooks = false;
    private boolean managerCanAddBooks = false;
    AdminController adminController;
    public AdministratorDashboardView(LibraryModel libraryModel) {
        this.libraryModel = libraryModel;
    }

    public void initialize() {

        initializeUserTable();
        adminController = new AdminController(libraryModel);
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
// Initialize the timeline for automatic low stock alerts
        initializeLowStockAlertsTimeline();
        // Initialize books table
        booksTable = new TableView<>();
        booksTable.getColumns().addAll(isbnColumn, titleColumn, authorColumn, sellingPriceColumn, categoryColumn,
                supplierColumn, stockColumn, purchasedDateColumn, purchasedPriceColumn, originalPriceColumn, bookCoverColumn);

        totalIncomesLabel  = new Label("Total Incomes");
        totalCostsLabel = new Label("Total Costs");
        // Add components to the layout
        VBox root = new VBox(10);
        root.setPadding(new Insets(20));

        Label titleLabel = new Label("Manager Dashboard");
        titleLabel.setStyle("-fx-font-size: 18; -fx-font-weight: bold;");

        // Create a menu bar
        MenuBar menuBar = new MenuBar();

        Menu bookMenu = new Menu("Books");
        Menu authorMenu = new Menu("Authors");
        Menu categoryMenu = new Menu("Categories");
        Menu stockMenu = new Menu("Stock");
        Menu performanceMenu = new Menu("Performance");
        Menu statisticsMenu = new Menu("Statistics");
        Menu employeesMenu = new Menu("Employees");

        // Create menu items
        MenuItem addBookMenuItem = new MenuItem("Add Book");
        MenuItem checkOutBookMenuItem = new MenuItem("Checkout books");
        MenuItem createAuthorMenuItem = new MenuItem("Create New Author");
        MenuItem addCategoryMenuItem = new MenuItem("Add Category");
        MenuItem showAuthorsMenuItem = new MenuItem("Show All Authors");
        MenuItem showCategoriesMenuItem = new MenuItem("Show All Categories");
        MenuItem addBooksToStockMenuItem = new MenuItem("Add Books to Stock");
        MenuItem showCategoriesWithStockMenuItem = new MenuItem("show Categories with Stock");
        MenuItem checkPerformanceMenuItem = new MenuItem("Check Performance");
        MenuItem bookStatisticsMenuItem = new MenuItem("Book Statistics");
        MenuItem permissionsMenuItem = new MenuItem("Permissions");
        // Add "Manage Employees" menu items
        MenuItem registerEmployeeMenuItem = new MenuItem("Register Employee");
        MenuItem modifyEmployeeMenuItem = new MenuItem("Modify Employee");
        MenuItem deleteEmployeeMenuItem = new MenuItem("Delete Employee");

        MenuItem calculateTotalIncomeMenuItem = new MenuItem("Total Calculations");

        // Set actions for menu items
        addBookMenuItem.setOnAction(event -> openAddBookDialog());
        createAuthorMenuItem.setOnAction(event -> openCreateAuthorDialog());
        addCategoryMenuItem.setOnAction(event -> openAddCategoryDialog());
        showAuthorsMenuItem.setOnAction(event -> adminController.showAllAuthors());
        showCategoriesMenuItem.setOnAction(event -> adminController.showAllCategories());
        addBooksToStockMenuItem.setOnAction(event -> openAddBooksToStockDialog());
        checkPerformanceMenuItem.setOnAction(event -> openPerformanceCheckDialog());
        bookStatisticsMenuItem.setOnAction(event -> openBookStatisticsDialog());
        showCategoriesWithStockMenuItem.setOnAction(event -> showCategoriesWithStock());
        checkOutBookMenuItem.setOnAction(event->handleCheckOutBooks());
        registerEmployeeMenuItem.setOnAction(event->openRegisterUserDialog());
        modifyEmployeeMenuItem.setOnAction(event->openModifyUserDialog());
        deleteEmployeeMenuItem.setOnAction(actionEvent -> openDeleteUserDialog());
        calculateTotalIncomeMenuItem.setOnAction(actionEvent -> openTotalCalculationsView());
        permissionsMenuItem.setOnAction(event -> openPermissionsDialog());
        // Add menu items to menus
        bookMenu.getItems().addAll(addBookMenuItem, checkOutBookMenuItem,showAuthorsMenuItem, showCategoriesMenuItem,showCategoriesWithStockMenuItem);
        authorMenu.getItems().addAll(createAuthorMenuItem);
        categoryMenu.getItems().addAll(addCategoryMenuItem);
        stockMenu.getItems().addAll(addBooksToStockMenuItem);
        performanceMenu.getItems().addAll(checkPerformanceMenuItem);
        statisticsMenu.getItems().addAll(bookStatisticsMenuItem,calculateTotalIncomeMenuItem);
        employeesMenu.getItems().addAll(registerEmployeeMenuItem, modifyEmployeeMenuItem, deleteEmployeeMenuItem,permissionsMenuItem);

        // Add menus to the menu bar
        menuBar.getMenus().addAll(bookMenu, authorMenu, categoryMenu, stockMenu, performanceMenu, statisticsMenu,employeesMenu);

        HBox searchBox = new HBox(10, new Label("Search ISBN:"), searchField, searchButton);

        root.getChildren().addAll(menuBar, titleLabel, searchBox,new Label("BOOKS DATA"),booksTable,new Label("EMPLOYEES DATA"),usersTable,logoutButton);
        Scene scene = new Scene(root, 800, 600);

        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Administrator Dashboard");

        stage.show();
        // Populate books table
        updateBooksTable();
        // Populate user table
        updateUsersTable();
    }

    private void openPermissionsDialog() {
        Stage permissionsStage = new Stage();
        permissionsStage.initModality(Modality.APPLICATION_MODAL);
        permissionsStage.setTitle("Modify Permissions");

        // CheckBoxes for permissions
        CheckBox librarianCheckBox = new CheckBox("Librarians can add new books to the stock");
        CheckBox managerCheckBox = new CheckBox("Managers can add new books to the stock");

        // Initialize CheckBox states based on current permissions
        librarianCheckBox.setSelected(librarianCanAddBooks);
        managerCheckBox.setSelected(managerCanAddBooks);

        // Button to apply changes
        Button applyButton = new Button("Apply Changes");
        applyButton.setOnAction(event -> adminController.applyPermissions(librarianCheckBox.isSelected(), managerCheckBox.isSelected(), permissionsStage));

        // Layout
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10));
        layout.getChildren().addAll(librarianCheckBox, managerCheckBox, applyButton);

        permissionsStage.setScene(new Scene(layout, 400, 200));
        permissionsStage.show();
    }


    private void openTotalCalculationsView() {
        // Create a new stage for the total calculations view
        Stage totalCalculationsStage = new Stage();
        totalCalculationsStage.setTitle("Total Calculations");

        // Initialize components
        DatePicker fromDatePicker = new DatePicker();
        DatePicker toDatePicker = new DatePicker();
        Button calculateIncomesButton = new Button("Calculate Total Incomes");
        Button calculateCostsButton = new Button("Calculate Total Costs");


        // Set actions for buttons
        calculateIncomesButton.setOnAction(event -> adminController.handleCalculateTotalIncomes(fromDatePicker.getValue(), toDatePicker.getValue(),totalIncomesLabel));
        calculateCostsButton.setOnAction(event -> adminController.handleCalculateTotalCosts(fromDatePicker.getValue(), toDatePicker.getValue(),totalCostsLabel));

        // Layout
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10));
        layout.getChildren().addAll(
                new HBox(10, new Label("From Date:"), fromDatePicker),
                new HBox(10, new Label("To Date:"), toDatePicker),
                calculateIncomesButton,
                calculateCostsButton,
                new HBox(10, totalIncomesLabel, totalCostsLabel)
        );

        totalCalculationsStage.setScene(new Scene(layout, 400, 300));
        totalCalculationsStage.show();
    }


    private void initializeUserTable() {
        // Initialize user table and columns
        usersTable = new TableView<>();

        nameColumn = new TableColumn<>("Name");
        birthdayColumn = new TableColumn<>("Birthday");
        phoneColumn = new TableColumn<>("Phone");
        emailColumn = new TableColumn<>("Email");
        salaryColumn = new TableColumn<>("Salary");
        accessLevelColumn = new TableColumn<>("Access Level");

        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        birthdayColumn.setCellValueFactory(new PropertyValueFactory<>("birthday"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        salaryColumn.setCellValueFactory(new PropertyValueFactory<>("salary"));
        accessLevelColumn.setCellValueFactory(new PropertyValueFactory<>("accessLevel"));

        usersTable.getColumns().addAll(nameColumn, birthdayColumn, phoneColumn, emailColumn, salaryColumn, accessLevelColumn);


    }
    private void updateUsersTable() {
        // Retrieve all users from the library model
        List<User> users = libraryModel.getAllUsers();
         System.out.println(users);
        // Populate the user table
        ObservableList<User> observableUsers = FXCollections.observableArrayList(users);
        usersTable.setItems(observableUsers);
        usersTable.refresh();
    }

    private void updateBooksTable() {
        ObservableList<Book> books = FXCollections.observableArrayList(libraryModel.getAllBooks());
        booksTable.setItems(books);
        booksTable.refresh(); // Add this line to force a refresh

    }


    private void openRegisterUserDialog() {
        // Create a new stage for the register user dialog
        Stage registerUserStage = new Stage();
        registerUserStage.setTitle("Register User");

        // Initialize components for registering a new user
        TextField nameField = new TextField();
        nameField.setPromptText("Name");

        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");

        TextField birthdayField = new TextField();
        birthdayField.setPromptText("Birthday");

        TextField phoneField = new TextField();
        phoneField.setPromptText("Phone");

        TextField emailField = new TextField();
        emailField.setPromptText("Email");

        TextField salaryField = new TextField();
        salaryField.setPromptText("Salary");

        TextField accessLevelField = new TextField();
        accessLevelField.setPromptText("Access Level");

        Button registerUserButton = new Button("Register User");
        registerUserButton.setOnAction(event -> adminController.handleRegisterUser(
                nameField.getText(),
                usernameField.getText(),
                passwordField.getText(),
                birthdayField.getText(),
                phoneField.getText(),
                emailField.getText(),
                salaryField.getText(),
                accessLevelField.getText(),
                registerUserStage
        ));

        // Refresh the users table
        updateUsersTable();
        // Add components to the layout
        VBox registerUserRoot = new VBox(10);
        registerUserRoot.setPadding(new Insets(20));
        registerUserRoot.getChildren().addAll(
                new Label("Name:"),
                nameField,
                new Label("Username:"),
                usernameField,
                new Label("Password:"),
                passwordField,
                new Label("Birthday:"),
                birthdayField,
                new Label("Phone:"),
                phoneField,
                new Label("Email:"),
                emailField,
                new Label("Salary:"),
                salaryField,
                new Label("Access Level:"),
                accessLevelField,
                registerUserButton
        );

        Scene registerUserScene = new Scene(registerUserRoot, 400, 600);
        registerUserStage.setScene(registerUserScene);

        // Show the register user stage
        registerUserStage.show();
    }


    private void openModifyUserDialog() {
        // Assuming you have a method to retrieve a list of existing users, e.g., getExistingUsers()
        List<User> existingUsers = libraryModel.getAllUsers();

        // Display a dialog or a dropdown menu to let the user select an existing user
        User selectedUser = showUserSelectionDialog(existingUsers);

        // Check if the user selected a user
        if (selectedUser != null) {
            // Call the openModifyUserDialog with the selected user
            openModifyUserDialog(selectedUser);
        }
    }

    private User showUserSelectionDialog(List<User> existingUsers) {
        // Implement logic to display a dialog or dropdown menu with a list of existing users
        // Return the user selected by the user

        // For simplicity, let's assume you have a method that shows a choice dialog
        // and returns the selected user, or null if the user cancels the selection
        return showUserChoiceDialog(existingUsers);
    }

    private User showUserChoiceDialog(List<User> users) {
        // Implement the logic to display a choice dialog with a list of usernames
        // Return the selected user or null if the user cancels the selection

        // Extract usernames from the list of users
        List<String> usernames = users.stream().map(User::getUsername).collect(Collectors.toList());

        // For simplicity, you can use a ChoiceDialog
        ChoiceDialog<String> dialog = new ChoiceDialog<>(usernames.get(0), usernames);
        dialog.setTitle("Select User");
        dialog.setHeaderText("Choose a user to modify:");
        dialog.setContentText("User:");

        Optional<String> result = dialog.showAndWait();

        // Convert the selected username back to the corresponding User object
        return result.map(username -> users.stream().filter(user -> user.getUsername().equals(username)).findFirst().orElse(null)).orElse(null);
    }
    private void openModifyUserDialog(User userToModify) {
        // Create a new stage for the modify user dialog
        Stage modifyUserStage = new Stage();
        modifyUserStage.setTitle("Modify User");

        // Initialize components for modifying an existing user
        TextField nameField = new TextField();
        nameField.setPromptText("Name");
        nameField.setText(userToModify.getName());

        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");
        usernameField.setText(userToModify.getUsername());

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        // You may choose to pre-fill the password field or leave it empty for security reasons

        TextField birthdayField = new TextField();
        birthdayField.setPromptText("Birthday");
        birthdayField.setText(userToModify.getBirthday());

        TextField phoneField = new TextField();
        phoneField.setPromptText("Phone");
        phoneField.setText(userToModify.getPhone());

        TextField emailField = new TextField();
        emailField.setPromptText("Email");
        emailField.setText(userToModify.getEmail());

        TextField salaryField = new TextField();
        salaryField.setPromptText("Salary");
        salaryField.setText(String.valueOf(userToModify.getSalary()));

        TextField accessLevelField = new TextField();
        accessLevelField.setPromptText("Access Level");
        accessLevelField.setText(userToModify.getAccessLevel().toString());

        ChoiceBox<String> choiceBox = new ChoiceBox<>();
        choiceBox.getItems().addAll("Can Add Book", "Cannot Add Book");
        choiceBox.setValue("Can Add Book"); // Set a default value

        Button modifyUserButton = new Button("Modify User");
        modifyUserButton.setOnAction(event -> {
                    String selectedOption = choiceBox.getValue();
                    boolean canAddBook = selectedOption.equals("Can Add Book");

                    adminController.handleModifyUser(
                            userToModify,
                            nameField.getText(),
                            usernameField.getText(),
                            passwordField.getText(),
                            birthdayField.getText(),
                            phoneField.getText(),
                            emailField.getText(),
                            salaryField.getText(),
                            accessLevelField.getText(),

                            modifyUserStage,
                            canAddBook
                    );
                }
        );
        // Refresh the users table
        updateUsersTable();

        // Add components to the layout
        VBox modifyUserRoot = new VBox(10);
        modifyUserRoot.setPadding(new Insets(20));
        modifyUserRoot.getChildren().addAll(
                new Label("Name:"),
                nameField,
                new Label("Username:"),
                usernameField,
                new Label("Password:"),
                passwordField,
                new Label("Birthday:"),
                birthdayField,
                new Label("Phone:"),
                phoneField,
                new Label("Email:"),
                emailField,
                new Label("Salary:"),
                salaryField,
                new Label("Access Level:"),
                accessLevelField,
                new Label("Add Book Permission:"),
                choiceBox,
                modifyUserButton
        );

        Scene modifyUserScene = new Scene(modifyUserRoot, 400, 600);
        modifyUserStage.setScene(modifyUserScene);

        // Show the modify user stage
        modifyUserStage.show();
    }


    private void openDeleteUserDialog() {
        // Assuming you have a method to retrieve a list of existing users, e.g., getExistingUsers()
        List<User> existingUsers = libraryModel.getAllUsers();

        // Display a dialog or a dropdown menu to let the user select an existing user
        User selectedUser = showUserSelectionDialog(existingUsers);

        // Check if the user selected a user
        if (selectedUser != null) {
            // Call the openModifyUserDialog with the selected user
            openDeleteUserDialog(selectedUser);
        }
    }



    private void openDeleteUserDialog(User userToDlt) {
        // Create a new stage for the modify user dialog
        Stage modifyUserStage = new Stage();
        modifyUserStage.setTitle("Delete User");

        // Initialize components for modifying an existing user
        TextField nameField = new TextField();
        nameField.setPromptText("Name");
        nameField.setText(userToDlt.getName());

        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");
        usernameField.setText(userToDlt.getUsername());

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        // You may choose to pre-fill the password field or leave it empty for security reasons

        TextField birthdayField = new TextField();
        birthdayField.setPromptText("Birthday");
        birthdayField.setText(userToDlt.getBirthday());

        TextField phoneField = new TextField();
        phoneField.setPromptText("Phone");
        phoneField.setText(userToDlt.getPhone());

        TextField emailField = new TextField();
        emailField.setPromptText("Email");
        emailField.setText(userToDlt.getEmail());

        TextField salaryField = new TextField();
        salaryField.setPromptText("Salary");
        salaryField.setText(String.valueOf(userToDlt.getSalary()));

        TextField accessLevelField = new TextField();
        accessLevelField.setPromptText("Access Level");
        accessLevelField.setText(userToDlt.getAccessLevel().toString());

        Button modifyUserButton = new Button("Delete User");
        modifyUserButton.setOnAction(event -> adminController.handleDeleteUser(
                userToDlt
        ));

        // Refresh the users table or perform any necessary updates
        updateUsersTable();
        // Add components to the layout
        VBox modifyUserRoot = new VBox(10);
        modifyUserRoot.setPadding(new Insets(20));
        modifyUserRoot.getChildren().addAll(
                new Label("Name:"),
                nameField,
                new Label("Username:"),
                usernameField,
                new Label("Password:"),
                passwordField,
                new Label("Birthday:"),
                birthdayField,
                new Label("Phone:"),
                phoneField,
                new Label("Email:"),
                emailField,
                new Label("Salary:"),
                salaryField,
                new Label("Access Level:"),
                accessLevelField,
                modifyUserButton
        );

        Scene modifyUserScene = new Scene(modifyUserRoot, 400, 600);
        modifyUserStage.setScene(modifyUserScene);

        // Show the modify user stage
        modifyUserStage.show();
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

                        // Save the bill to a text file
                        adminController.saveBillToFile(bill);

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
                adminController.displayLowStockBooksAlert(lowStockBooks);
            }
        });
    }




    private void openCreateAuthorDialog() {
        // Create a new stage for the create author dialog
        Stage createAuthorStage = new Stage();
        createAuthorStage.setTitle("Create New Author");

        // Initialize components for creating a new author
        newAuthorNameField = new TextField();
        newAuthorNameField.setPromptText("Author Name");

        createAuthorButton = new Button("Create Author");
        createAuthorButton.setOnAction(event -> adminController.handleCreateAuthor(createAuthorStage,newAuthorNameField));

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

            adminController.addBookController(   isbn,
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
        createCategoryButton.setOnAction(event -> adminController.handleAddCategory(addCategoryStage,newCategoryField));
        // Refresh the books table after adding a new category
        updateBooksTable();
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
        adminController.displayListDialog("Categories with Stock", categoriesWithStock);
    }

    private int calculateStockForCategory(String category) {
        // Calculate the total stock for the given category
        List<Book> booksInCategory = libraryModel.getAllBooks().stream()
                .filter(book -> book.getCategory().equalsIgnoreCase(category))
                .collect(Collectors.toList());

        int totalStock = booksInCategory.stream().mapToInt(Book::getStock).sum();
        return totalStock;
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
    public void display() {
        initialize();
    }

}
