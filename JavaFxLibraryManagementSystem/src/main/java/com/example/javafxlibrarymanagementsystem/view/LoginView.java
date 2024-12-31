package com.example.javafxlibrarymanagementsystem.view;

import com.example.javafxlibrarymanagementsystem.model.LibraryModel;
import com.example.javafxlibrarymanagementsystem.model.User;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class LoginView {
    private final LibraryModel libraryModel;
    private final Stage stage;

    public LoginView(Stage stage, LibraryModel libraryModel) {
        this.stage = stage;
        this.libraryModel = libraryModel;
    }

    public Parent getView() {
        VBox root = new VBox(10); // 10 is the spacing between components
        root.setPadding(new Insets(20));

        TextField usernameField = new TextField();
        PasswordField passwordField = new PasswordField();
        Button loginButton = new Button("Login");

        loginButton.setOnAction(event -> handleLogin(usernameField.getText(), passwordField.getText()));

        root.getChildren().addAll(
                new Label("Username:"),
                usernameField,
                new Label("Password:"),
                passwordField,
                loginButton
        );

        return root;
    }

    private void handleLogin(String username, String password) {
        User user = libraryModel.getUserByUsername(username);

        if (user != null && user.getPassword().equals(password)) {
            System.out.println(username+ " "+ password+ " " +user.getAccessLevel());
            openDashboard(user);
        } else {
            showAlert("Login Failed", "Invalid username or password. Please try again.");
        }
    }

    private void openDashboard(User user) {
        if (user.getAccessLevel() == User.AccessLevel.LIBRARIAN) {
            new LibrarianDashboardView(libraryModel).display(user);
            close();
        } else if (user.getAccessLevel() == User.AccessLevel.MANAGER) {
            // Uncomment the line below if ManagerDashboardView is implemented
             new ManagerDashboardView(libraryModel).display(user);
             close();
        } else if (user.getAccessLevel() == User.AccessLevel.ADMINISTRATOR) {
            // Uncomment the line below if AdministratorDashboardView is implemented
             new AdministratorDashboardView(libraryModel).display();
             close();
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public void show() {
        Scene scene = new Scene(getView(), 400, 300);
        stage.setTitle("Library Management System - Login");
        stage.setScene(scene);
        stage.show();
    }

    public void close() {
        stage.close();
    }
}
