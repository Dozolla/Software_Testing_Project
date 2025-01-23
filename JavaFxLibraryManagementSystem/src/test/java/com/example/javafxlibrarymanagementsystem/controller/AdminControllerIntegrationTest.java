package com.example.javafxlibrarymanagementsystem.controller;

import com.example.javafxlibrarymanagementsystem.model.Bill;
import com.example.javafxlibrarymanagementsystem.model.LibraryModel;
import com.example.javafxlibrarymanagementsystem.model.User;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.concurrent.CountDownLatch;

import static org.junit.jupiter.api.Assertions.*;

class AdminControllerIntegrationTest {

    private LibraryModel libraryModel;
    private AdminController adminController;

    @BeforeAll
    static void initToolkit() {
        // Initialize JavaFX Toolkit
        CountDownLatch latch = new CountDownLatch(1);
        Platform.startup(() -> latch.countDown());
        try {
            latch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @BeforeEach
    void setUp() {
        libraryModel = new LibraryModel(); // Real instance of the model
        adminController = new AdminController(libraryModel); // Controller interacting with the model
    }

    @Test
    void testApplyPermissions() throws Exception {
        runOnFxThread(() -> {
            Stage permissionsStage = new Stage();

            // Apply permissions for librarian and manager
            adminController.applyPermissions(true, false, permissionsStage);

            // Verify that permissions are set correctly in the model
            assertTrue(libraryModel.isLibrarianCanAddBooks(), "Librarian permission should be set to true.");
            assertFalse(libraryModel.isManagerCanAddBooks(), "Manager permission should be set to false.");
        });
    }


    /// Unit Test
    @Test
    void testHandleCalculateTotalIncomes() {
        Bill bill1 = new Bill(100, "2025-01-10");
        Bill bill2 = new Bill(200, "2025-01-15");
        Bill bill3 = new Bill(50, "2025-01-20");
        // Add sample bills to the library model
        libraryModel.addBill(bill1);
        libraryModel.addBill(bill2);
        libraryModel.addBill(bill3);

        // Set up date range and label for results
        LocalDate fromDate = LocalDate.of(2025, 1, 10);
        LocalDate toDate = LocalDate.of(2025, 1, 15);
        Label totalIncomesLabel = new Label();

        // Call the method under test
        adminController.handleCalculateTotalIncomes(fromDate, toDate, totalIncomesLabel);

        // Verify the results
        assertEquals("Total Incomes: $300.00", totalIncomesLabel.getText(), "Total incomes should match the sum of bills in the given date range.");
    }

    @Test
    void testHandleRegisterUser() throws Exception {
        runOnFxThread(() -> {
            Stage registerUserStage = new Stage();

            // Call the method to register a user
            adminController.handleRegisterUser(
                    "John Doe",
                    "johndoe",
                    "password123",
                    "1990-01-01",
                    "1234567890",
                    "johndoe@example.com",
                    "50000",
                    "LIBRARIAN",
                    registerUserStage
            );

            // Verify that the user is added to the library model
            User registeredUser = libraryModel.getUserByUsername("johndoe");
            assertNotNull(registeredUser, "Registered user should be found in the model.");
            assertEquals("John Doe", registeredUser.getName(), "User name should match.");
            assertEquals(User.AccessLevel.LIBRARIAN, registeredUser.getAccessLevel(), "User access level should match.");
            assertEquals(50000.0, registeredUser.getSalary(), 0.01, "User salary should match.");
        });
    }

    // Helper method to run code on the JavaFX Application Thread
    private void runOnFxThread(Runnable action) throws Exception {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            try {
                action.run();
            } finally {
                latch.countDown();
            }
        });
        latch.await(); // Wait for the action to complete
    }

    @Test
    void testHandleModifyUser() throws Exception {
        runOnFxThread(() -> {
            // Add an initial user to the library model
            User originalUser = new User(
                    "Jane Smith",
                    "janesmith",
                    "password456",
                    "1985-05-15",
                    "9876543210",
                    "janesmith@example.com",
                    40000.0,
                    User.AccessLevel.MANAGER,
                    true
            );
            libraryModel.addUser(originalUser);

            // Call the method to modify the user
            Stage modifyUserStage = new Stage();
            adminController.handleModifyUser(
                    originalUser,
                    "Jane Doe",
                    "janedoe",
                    "newpassword456",
                    "1985-05-15",
                    "1112223333",
                    "janedoe@example.com",
                    "45000",
                    "MANAGER",
                    modifyUserStage,
                    true
            );

            // Verify that the user is modified in the library model
            User modifiedUser = libraryModel.getUserByUsername("janedoe");
            assertNotNull(modifiedUser, "Modified user should be found in the model.");
            assertEquals("Jane Doe", modifiedUser.getName(), "User name should be updated.");
            assertEquals("newpassword456", modifiedUser.getPassword(), "Password should be updated.");
            assertEquals("1112223333", modifiedUser.getPhone(), "Phone number should be updated.");
            assertEquals("janedoe@example.com", modifiedUser.getEmail(), "Email should be updated.");
            assertEquals(45000.0, modifiedUser.getSalary(), 0.01, "Salary should be updated.");
            assertEquals(User.AccessLevel.MANAGER, modifiedUser.getAccessLevel(), "Access level should remain the same.");
        });
    }
}
