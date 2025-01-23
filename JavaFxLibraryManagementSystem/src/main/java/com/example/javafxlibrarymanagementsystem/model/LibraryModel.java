package com.example.javafxlibrarymanagementsystem.model;
import java.io.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class LibraryModel {
    private List<Book> books;
    private List<Bill> bills;
    private List<User> users;
    private static AtomicInteger billNumberGenerator = new AtomicInteger(1);
    private List<String> bookCategories;
    private List<String> authors;
    private boolean librarianCanAddBooks;
    private boolean managerCanAddBooks;
    public LibraryModel() {
        this.books = new ArrayList<>();
        this.bills = new ArrayList<>();
        this.users = new ArrayList<>();
        this.bookCategories = new ArrayList<>();  // Initialize bookCategories list
        initializeData(); // Load data from files during initialization
        loadData(); // Load data from files during initialization
    }
    private void initializeData() {
        try (ObjectInputStream bookStream = new ObjectInputStream(new FileInputStream("books.dat"));
             ObjectInputStream billStream = new ObjectInputStream(new FileInputStream("bills.dat"));
             ObjectInputStream userStream = new ObjectInputStream(new FileInputStream("users.dat"));
             ObjectInputStream categoryStream = new ObjectInputStream(new FileInputStream("categories.dat"));
             ObjectInputStream authorStream = new ObjectInputStream(new FileInputStream("authors.dat"))) {

            books = (List<Book>) bookStream.readObject();
            bills = (List<Bill>) billStream.readObject();
            users = (List<User>) userStream.readObject();
            bookCategories = (List<String>) categoryStream.readObject();
            authors = (List<String>) authorStream.readObject();

        } catch (FileNotFoundException e) {
            generateSampleData();
            saveData();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    private void generateSampleData() {

        books = new ArrayList<>();
        books.add(new Book("001", "Book1", "Fiction", "Supplier1", "Author1", 10, "2024-01-01", 20.0, 15.0, 25.0, "image1.jpg"));
        books.add(new Book("002", "Book2", "Non-Fiction", "Supplier2", "Author2", 15, "2024-01-01", 25.0, 18.0, 30.0, "image2.jpg"));

        bills = new ArrayList<>();
        BillItem billItem1 = new BillItem("001", "Book1", 2, 25.0);
        BillItem billItem2 = new BillItem("002", "Book2", 1, 30.0);

        Bill bill1 = new Bill(1, "2024-01-01");
        bill1.addBillItem(billItem1);
        bill1.addBillItem(billItem2);

        bills.add(bill1);

        users = new ArrayList<>();
        users.add(new User("librarian1", "Librarian One", "librarian1password", "1999-01-01","123473895835","tubarajput92@gmail.com", 3000.0,User.AccessLevel.LIBRARIAN,false));
        users.add(new User("manager1", "Manager One", "manager1password", "1999-01-01","65132747384572","tubarajput92@gmail.com", 5000.0,User.AccessLevel.MANAGER,true));
        users.add(new User("admin1", "Administrator One", "admin1password","1999-01-01","36986597629","tubarajput92@gmail.com", 8000.0, User.AccessLevel.ADMINISTRATOR,true));

        // Add sample book categories
        bookCategories = new ArrayList<>();
        bookCategories.add("Fiction");
        bookCategories.add("Non-Fiction");
        bookCategories.add("Science Fiction");
        // Add more categories as needed
// Add sample authors
        authors = new ArrayList<>();
        authors.add("Author1");
        authors.add("Author2");

        // Save data after generating sample data
        saveData();
    }

    private int generateBillNumber() {
        return billNumberGenerator.getAndIncrement();
    }

    private String getCurrentDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(new Date());
    }

    public Bill checkoutBooks(Librarian librarian, List<Book> booksToCheckout, List<Integer> quantities) {
        if (booksToCheckout.size() != quantities.size()) {
            // Handle invalid input
            return null;
        }

        // Check if books are available and create a bill
        Bill bill = new Bill(generateBillNumber(), getCurrentDate());
        for (int i = 0; i < booksToCheckout.size(); i++) {
            Book book = booksToCheckout.get(i);
            int quantity = quantities.get(i);

            if (book.getStock() < quantity) {
                // Handle insufficient stock
                return null;
            }

            BillItem billItem = new BillItem(book.getISBN(), book.getTitle(), quantity, book.getSellingPrice());
            bill.addBillItem(billItem);

            // Update stock and other necessary data
            book.decreaseStock(quantity);
            // You may want to update other book data (e.g., sales statistics) here

            // Save the data after each successful checkout
            saveData();
        }

        return bill;
    }
    // New method to add a book category
    public void addBookCategory(String category) {
        if (!bookCategories.contains(category)) {
            bookCategories.add(category);
            saveData();
        }
    }

    public List<String> getBookCategories() {
        return new ArrayList<>(bookCategories);
    }
    // Methods for managing books

    public void addBook(Book book) {
        books.add(book);
        saveData();
    }

    public List<Book> getAllBooks() {
        return new ArrayList<>(books);
    }

    // Methods for managing bills

    public void addBill(Bill bill) {
        bills.add(bill);
        saveData();
    }

    public List<Bill> getAllBills() {
        return new ArrayList<>(bills);
    }

    // Methods for managing users

    public void addUser(User user) {
        users.add(user);
        saveData();
    }
    public void deleteUser(User userToDelete) {
        users.remove(userToDelete);
    }
    public void removeBorrowedBookOption(User user) {
        // Implement logic to remove the borrowed book option for the specified user
        // You may want to update the User class to have a flag indicating whether borrowing is allowed
        // Update the user in the 'users' list and save the data
        User updatedUser = new User(user.getUsername(), user.getName(), user.getPassword(), user.getBirthday(),
                user.getPhone(), user.getEmail(), user.getSalary(), user.getAccessLevel(),user.getCanAddBooksPermission());
      //  updatedUser.setBorrowingAllowed(false); // Set the flag accordingly

        modifyUser(user, updatedUser);
    }
    public void modifyUser(User oldUser, User updatedUser) {
        // Check if the oldUser exists in the user list
        if (users.contains(oldUser)) {
            // Get the index of the oldUser
            int index = users.indexOf(oldUser);

            // Replace the oldUser with the updatedUser
            users.set(index, updatedUser);
            saveData();

        } else {
            // Handle the case where the oldUser is not found
            System.out.println("User not found. Cannot modify user.");
        }
    }

    public List<User> getAllUsers() {
        return new ArrayList<>(users);
    }

    // Additional methods for data retrieval and statistics

    public User getUserByUsername(String username) {
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }

    public double getTotalIncome() {
        double totalIncome = 0;
        for (Bill bill : bills) {
            totalIncome += bill.getTotalAmount();
        }
        return totalIncome;
    }

    public double getTotalCost() {
        double totalCost = 0;
        for (Book book : books) {
            totalCost += book.getPurchasedPrice();
        }
        for (User user : users) {
            totalCost += user.getSalary();
        }
        return totalCost;
    }

    private void saveData() {
        try (ObjectOutputStream bookStream = new ObjectOutputStream(new FileOutputStream("books.dat"));
             ObjectOutputStream billStream = new ObjectOutputStream(new FileOutputStream("bills.dat"));
             ObjectOutputStream userStream = new ObjectOutputStream(new FileOutputStream("users.dat"));
             ObjectOutputStream categoryStream = new ObjectOutputStream(new FileOutputStream("categories.dat"));
             ObjectOutputStream authorStream = new ObjectOutputStream(new FileOutputStream("authors.dat"))) {

            bookStream.writeObject(books);
            billStream.writeObject(bills);
            userStream.writeObject(users);
            categoryStream.writeObject(bookCategories);
            authorStream.writeObject(authors);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadData() {
        try (ObjectInputStream bookStream = new ObjectInputStream(new FileInputStream("books.dat"));
             ObjectInputStream billStream = new ObjectInputStream(new FileInputStream("bills.dat"));
             ObjectInputStream userStream = new ObjectInputStream(new FileInputStream("users.dat"));
             ObjectInputStream categoryStream = new ObjectInputStream(new FileInputStream("categories.dat"));
             ObjectInputStream authorStream = new ObjectInputStream(new FileInputStream("authors.dat"))) {

            books = (List<Book>) bookStream.readObject();
            bills = (List<Bill>) billStream.readObject();
            users = (List<User>) userStream.readObject();
            bookCategories = (List<String>) categoryStream.readObject();
            authors = (List<String>) authorStream.readObject();

        } catch (FileNotFoundException e) {
            // Ignore if files are not found initially
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void clearData() {
        try {
            // Create an array with the file names
            String[] fileNames = {"books.dat", "bills.dat", "users.dat", "categories.dat", "authors.dat"};

            // Iterate through the file names and delete each file
            for (String fileName : fileNames) {
                File file = new File(fileName);
                if (file.exists()) {
                    file.delete();
                    System.out.println("File " + fileName + " deleted successfully.");
                } else {
                    System.out.println("File " + fileName + " does not exist.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void addBookWithCategory(Book book, String category) {
        // Check if the category exists
        if (bookCategories.contains(category)) {
            book.setCategory(category);
            books.add(book);
            saveData();
        } else {
            // Handle the case where the category does not exist
            System.out.println("Error: Category does not exist.");
        }
    }

    public Bill checkOutBooks(Book book, BillItem billItem) {
        // Perform the check-out process, update stock, create a bill, etc.
        // Return the created bill
        // Example logic (adjust as needed):
        Bill bill = new Bill(generateBillNumber(), getCurrentDate());
        bill.addBillItem(billItem);
        book.decreaseStock(billItem.getQuantity());
        // Update other necessary data...

        return bill;
    }

    public void addAuthor(String author) {
        if (!authors.contains(author)) {
            authors.add(author);
            saveData();
        }
    }

    public List<String> getAllAuthors() {
        return new ArrayList<>(authors);
    }

    public void addBooksToStock(String categoryName, int quantity) {
        // Find books with the specified category
        List<Book> booksToUpdate = books.stream()
                .filter(book -> book.getCategory().equalsIgnoreCase(categoryName))
                .collect(Collectors.toList());

        // Update the stock for each book
        booksToUpdate.forEach(book -> book.setStock(book.getStock() + quantity));

        // Save data after updating stock
        saveData();
    }
    public List<Book> getLowStockBooks(int threshold) {
        List<Book> lowStockBooks = books.stream()
                .filter(book -> book.getStock() < threshold)
                .collect(Collectors.toList());

        System.out.println("Low stock books: " + lowStockBooks.size());
        return lowStockBooks;
    }


    public int getTotalBills(LocalDate fromDate, LocalDate toDate) {
        return (int) bills.stream()
                .filter(bill -> isDateInRange(parseDate(bill.getCreationDate()), fromDate, toDate))
                .count();
    }

    public int getTotalBooksSold(LocalDate fromDate, LocalDate toDate) {
        return (int) bills.stream()
                .filter(bill -> isDateInRange(parseDate(bill.getCreationDate()), fromDate, toDate))
                .flatMap(bill -> bill.getBillItems().stream())  // Adjust this line
                .count();
    }


    public double getTotalAmount(LocalDate fromDate, LocalDate toDate) {
        return bills.stream()
                .filter(bill -> isDateInRange(parseDate(bill.getCreationDate()), fromDate, toDate))
                .mapToDouble(Bill::getTotalAmount)
                .sum();
    }

    private LocalDate parseDate(String dateString) {
        // Assuming the date string is in a specific format, like "yyyy-MM-dd"
        return LocalDate.parse(dateString);
    }

    private boolean isDateInRange(LocalDate date, LocalDate fromDate, LocalDate toDate) {
        return !date.isBefore(fromDate) && !date.isAfter(toDate);
    }

    public int getTotalBooksBought(LocalDate fromDate, LocalDate toDate) {
        // Get the list of all books bought within the date range
        List<Book> booksBoughtInRange = getAllBooks().stream()
                .filter(book -> {
                    LocalDate purchasedDate = LocalDate.parse(book.getPurchasedDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                    return !purchasedDate.isBefore(fromDate) && !purchasedDate.isAfter(toDate);
                })
                .collect(Collectors.toList());

        // Sum up the quantity of books bought in the specified date range
        int totalBooksBought = booksBoughtInRange.stream().mapToInt(Book::getStock).sum();

        return totalBooksBought;
    }

    public Book searchBookByISBN(String isbn) {
        // Iterate through the list of books and find the one with the matching ISBN
        for (Book book : getAllBooks()) {
            if (book.getISBN().equals(isbn)) {
                return book; // Book found
            }
        }
        return null; // Book not found
    }

    public double calculateTotalIncomes(LocalDate fromDate, LocalDate toDate) {
        return bills.stream()
                .filter(bill -> isDateInRange(parseDate(bill.getCreationDate()), fromDate, toDate))
                .mapToDouble(Bill::getTotalAmount)
                .sum();
    }

    public double calculateTotalCosts(LocalDate fromDate, LocalDate toDate) {
        double totalItemPurchases = calculateTotalBooksPurchasedValue(fromDate, toDate);
        double totalStaffSalaries = calculateTotalStaffSalaries(fromDate, toDate);

        return totalItemPurchases + totalStaffSalaries;
    }

    private double calculateTotalBooksPurchasedValue(LocalDate fromDate, LocalDate toDate) {
        return books.stream()
                .filter(book -> isDateInRange(parseDate(book.getPurchasedDate()), fromDate, toDate))
                .mapToDouble(Book::getPurchasedPrice)
                .sum();
    }

    // New method to calculate total staff salaries paid in a specified period
    private double calculateTotalStaffSalaries(LocalDate fromDate, LocalDate toDate) {
        return users.stream()
                .mapToDouble(user -> calculateProRatedSalary(user, fromDate, toDate))
                .sum();
    }

    private double calculateProRatedSalary(User user, LocalDate fromDate, LocalDate toDate) {
        // Assuming the user's birthday is the indicator of joining date for now
        LocalDate joiningDate = LocalDate.parse(user.getBirthday(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        // Calculate the active period
        LocalDate effectiveFromDate = fromDate.isAfter(joiningDate) ? fromDate : joiningDate;
        LocalDate effectiveToDate = toDate.isBefore(LocalDate.now()) ? toDate : LocalDate.now();

        // Calculate the number of months in the active period
        long months = ChronoUnit.MONTHS.between(effectiveFromDate, effectiveToDate);

        // Calculate the pro-rated salary based on the number of active months
        double monthlySalary = user.getSalary() / 12.0;
        return months * monthlySalary;
    }


    public void setLibrarianCanAddBooks(boolean canAddBooks) {
        this.librarianCanAddBooks = canAddBooks;

        // Set CanAddBooks permission for all librarians
        for (User user : users) {
            if (user.getAccessLevel() == User.AccessLevel.LIBRARIAN) {
                user.setCanAddBooksPermission(canAddBooks);
            }
        }

        saveData(); // Save data after updating permissions
    }

    public void setManagerCanAddBooks(boolean canAddBooks) {
        this.managerCanAddBooks = canAddBooks;

        // Set CanAddBooks permission for all managers
        for (User user : users) {
            if (user.getAccessLevel() == User.AccessLevel.MANAGER) {
                user.setCanAddBooksPermission(canAddBooks);
            }
        }

        saveData(); // Save data after updating permissions
    }

    public boolean isLibrarianCanAddBooks() {
        return librarianCanAddBooks;
    }

    public boolean isManagerCanAddBooks() {
        return managerCanAddBooks;
    }
}
