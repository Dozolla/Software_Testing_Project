package com.example.javafxlibrarymanagementsystem;

import com.example.javafxlibrarymanagementsystem.model.LibraryModel;
import com.example.javafxlibrarymanagementsystem.view.LoginView;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

import static org.testfx.assertions.api.Assertions.assertThat;


class LibraryManagementAppTest extends ApplicationTest {

    @Override
    public void start(Stage stage) {
        // Create the Library Model
        LibraryModel libraryModel = new LibraryModel();

        // Create the LoginView
        LoginView loginView = new LoginView(stage, libraryModel);

        // Set up the primary stage
        stage.setTitle("Library Management System");
        stage.setScene(new Scene(loginView.getView(), 600, 400));
        stage.show();

        // Show the LoginView
        loginView.show();
    }

    @Test
    public void should_contain_no_txt() {
        TextField txt = lookup(".text-field").queryAs(TextField.class);
        assertThat(txt).hasToString(null);
    }

    @Test
    public void fill_text() {
        clickOn(".text-field").write("Administrator One");
        sleep(1000);
//        assertThat(lookup(".text-field").queryAs(TextField.class)).hasText("Administrator One");
        // click on the second text field
        clickOn(".password-field").clickOn(".password-field").write("admin1");
        sleep(1000);

        clickOn(".button");
    }

}