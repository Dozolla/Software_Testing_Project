package com.example.javafxlibrarymanagementsystem;

import com.example.javafxlibrarymanagementsystem.model.LibraryModel;
import com.example.javafxlibrarymanagementsystem.model.User;
import com.example.javafxlibrarymanagementsystem.view.LoginView;
import javafx.application.Platform;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.matcher.base.WindowMatchers;
import org.testfx.matcher.control.TableViewMatchers;

import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.assertions.api.Assertions.assertThat;

class RegisterEmployeeSystemTest extends ApplicationTest {

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

    @BeforeEach
    void setupTestData() {
        // Ensure the library model starts with only the admin user
        libraryModel.getAllUsers().removeIf(user -> !user.getUsername().equals("admin1"));
    }

    @Test
    void testLoginAndRegisterEmployee() {
        // Step 1: Login as admin
        clickOn(".text-field").write("admin1");
        sleep(500); // Optional for debugging

        clickOn(".password-field").write("password123");
        sleep(500);

        assertThat(lookup(".text-field").queryAs(TextField.class)).hasText("Administrator One");
        assertThat(lookup(".password-field").queryAs(PasswordField.class)).hasText("admin1");

        clickOn(".button");

        // Verify login by checking for the dashboard window
        verifyThat(window("Administrator Dashboard"), WindowMatchers.isShowing());

        // Step 2: Open Register Employee dialog
        clickOn("#registerEmployeeMenuItem"); // Use the appropriate ID for the menu item

        // Step 3: Fill in employee details
        clickOn("#nameField").write("John Doe");
        clickOn("#usernameField").write("johndoe");
        clickOn("#passwordField").write("password123");
        clickOn("#birthdayField").write("1990-05-15");
        clickOn("#phoneField").write("1234567891");
        clickOn("#emailField").write("johndoe@example.com");
        clickOn("#salaryField").write("40000");
        clickOn("#accessLevelField").write("LIBRARIAN");

        // Step 4: Submit the registration
        clickOn("#registerUserButton");

        // Step 5: Verify the new employee was added
        verifyThat("#usersTable", TableViewMatchers.hasTableCell("John Doe"));
    }
}
