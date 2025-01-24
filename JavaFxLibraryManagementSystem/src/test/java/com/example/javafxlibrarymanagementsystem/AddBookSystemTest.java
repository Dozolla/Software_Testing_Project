package com.example.javafxlibrarymanagementsystem;

import com.example.javafxlibrarymanagementsystem.model.LibraryModel;
import com.example.javafxlibrarymanagementsystem.model.User;
import com.example.javafxlibrarymanagementsystem.view.LoginView;
import javafx.application.Platform;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.matcher.base.WindowMatchers;
import org.testfx.matcher.control.TableViewMatchers;

import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.assertions.api.Assertions.assertThat;

class AddBookSystemTest extends ApplicationTest {

    private LibraryModel libraryModel;

    @BeforeAll
    static void setupToolkit() {
        // Initialize JavaFX toolkit
        Platform.startup(() -> {
        });

    }

    @Override
    public void start(Stage stage) {
        libraryModel = new LibraryModel();
//        libraryModel.loadData();

        // Add an administrator user to the library model
        User adminUser = new User(
                "Administrator One",
                "admin1",
                "password123",
                "1990-01-01",
                "1234567890",
                "admin@example.com",
                60000.0,
                User.AccessLevel.ADMINISTRATOR,
                true
        );
        libraryModel.addUser(adminUser);

        // Show the login view
        LoginView loginView = new LoginView(stage, libraryModel);
        loginView.show();
    }

//    @BeforeEach
//    void setupTestData() {
//        // Ensure the library model starts empty except for the admin user
//        libraryModel.getAllBooks().clear();
//    }

    @Test
    void testLoginAndAddBook() {
        // Ensure focus on the username text field and type the username
        clickOn(".text-field").write("admin1");
        sleep(500); // Optional for debugging

        // Ensure focus on the password field and type the password
        clickOn(".password-field").write("password123");
        sleep(500);

        // Verify the text was entered correctly (optional, remove for password field if not needed)
        assertThat(lookup(".text-field").queryAs(TextField.class)).hasText("admin1");
        assertThat(lookup(".password-field").queryAs(PasswordField.class)).hasText("password123");

        // Click the login button
        clickOn(".button");

        // Verify login (you might want to check for a change in the stage title or presence of a dashboard)
        verifyThat(window("Administrator Dashboard"), WindowMatchers.isShowing());

        // Add book after login
        clickOn("#addBookMenuItem"); // Use proper IDs for menu items or buttons
        clickOn("#newBookIsbnField").write("1234567890");
        clickOn("#newBookTitleField").write("Test Book Title");
        clickOn("#newBookAuthorField").write("Test Author");
        clickOn("#newBookSellingPriceField").write("19.99");
        clickOn("#newBookCategoryField").write("Fiction");
        clickOn("#addBookButton");

        // Verify the book was added (check the table or list for the new entry)
        verifyThat("#booksTable", TableViewMatchers.hasTableCell("Test Book Title"));
    }

}

