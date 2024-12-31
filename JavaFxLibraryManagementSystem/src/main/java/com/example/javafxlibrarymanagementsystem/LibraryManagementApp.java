package com.example.javafxlibrarymanagementsystem;

import com.example.javafxlibrarymanagementsystem.model.LibraryModel;
import com.example.javafxlibrarymanagementsystem.view.LoginView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class LibraryManagementApp extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        // Create the Library Model
        LibraryModel libraryModel = new LibraryModel();

        // Create the LoginView
        LoginView loginView = new LoginView(primaryStage, libraryModel);

        // Set up the primary stage
        primaryStage.setTitle("Library Management System");
        primaryStage.setScene(new Scene(loginView.getView(), 600, 400));
        primaryStage.show();

        // Show the LoginView
        loginView.show();
    }
}
