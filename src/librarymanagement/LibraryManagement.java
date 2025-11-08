/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package librarymanagement;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class LibraryManagement {

    static ArrayList<Users> users = new ArrayList<>();
    private static final String BOOK_DATA_FILE = "data/books.txt";
    private static final String USER_DATA_FILE = "data/users.txt";

    public static void main(String[] args) {
        Library lib = new Library(100);

        try {
            lib.loadFromFile(BOOK_DATA_FILE);
            System.out.println("Loaded " + lib.size() + " records from " + BOOK_DATA_FILE);
        } catch (Exception e) {
            System.out.println("Warning: could not load data: " + e.getMessage());
        }

        loadUsersFromFile(USER_DATA_FILE, lib);

        try (Scanner sc = new Scanner(System.in)) {
            // 🔹 USER LOGIN OR REGISTER FIRST
            Users currentUser = loginOrRegister(sc);
            if (currentUser == null) {
                System.out.println("Login failed. Exiting...");
                return;
            }

            System.out.println("\n✅ Welcome, " + currentUser.getName() + " (" + currentUser.getRole() + ")!");

            // 🔹 Proceed to role-aware Library menu
            boolean running = true;
            while (running) {
                printMenuForUser(currentUser);
                System.out.print("Choose an option: ");

                String opt = sc.nextLine().trim();
                if (isAdmin(currentUser)) {
                    switch (opt) {
                        case "1":
                            System.out.println(lib.generateReport());
                            break;
                        case "2":
                            addBookInteractively(sc, lib);
                            break;
                        case "3":
                            updateBookInteractively(sc, lib);
                            break;
                        case "4":
                            System.out.print("Enter ID to delete: ");
                            String delId = sc.nextLine().trim();
                            if (lib.delete(delId)) {
                                System.out.println("Deleted.");
                                persistState(lib);
                            } else {
                                System.out.println("Not found.");
                            }
                            break;
                        case "5":
                            System.out.print("Enter ID to search: ");
                            String sid = sc.nextLine().trim();
                            Book found = lib.searchById(sid);
                            System.out.println(found == null ? "Not found" : found.toString());
                            break;
                        case "6":
                            System.out.println(lib.generateReport());
                            break;
                        case "7":
                            showBorrowedBooks(currentUser);
                            break;
                        case "8":
                            borrowBook(sc, lib, currentUser);
                            break;
                        case "9":
                            persistState(lib);
                            System.out.println("Saved " + lib.size() + " records and " + users.size() + " users.");
                            running = false;
                            break;
                        default:
                            System.out.println("Invalid option.");
                    }
                } else {
                    switch (opt) {
                        case "1":
                            System.out.println(lib.generateReport());
                            break;
                        case "2":
                            System.out.print("Enter ID to search: ");
                            String sid = sc.nextLine().trim();
                            Book found = lib.searchById(sid);
                            System.out.println(found == null ? "Not found" : found.toString());
                            break;
                        case "3":
                            showBorrowedBooks(currentUser);
                            break;
                        case "4":
                            borrowBook(sc, lib, currentUser);
                            break;
                        case "5":
                            persistState(lib);
                            running = false;
                            break;
                        default:
                            System.out.println("Invalid option.");
                    }
                }
            }
        }
    }

    // ✅ Ask user to Login or Register
    private static Users loginOrRegister(Scanner sc) {
        while (true) {
            System.out.println("\n====== USER SYSTEM ======");
            System.out.println("1. Login");
            System.out.println("2. Register (New User)");
            System.out.println("3. Exit Program");
            System.out.print("Choose: ");
            String choice = sc.nextLine().trim();

            switch (choice) {
                case "1": return login(sc);
                case "2": register(sc); break;
                case "3": return null;
                default: System.out.println("Invalid option.");
            }
        }
    }

    // ✅ Register new user
    private static void register(Scanner sc) {
        System.out.print("Enter new User ID: ");
        String id = sc.nextLine().trim();
        if (findUserById(id) != null) {
            System.out.println("❌ A user with that ID already exists.");
            return;
        }
        System.out.print("Enter name: ");
        String name = sc.nextLine().trim();
        System.out.print("Enter email: ");
        String email = sc.nextLine().trim();
        String role;
        while (true) {
            System.out.print("Enter role (Admin/Member): ");
            role = sc.nextLine().trim();
            if (role.equalsIgnoreCase("Admin") || role.equalsIgnoreCase("Member")) {
                role = role.substring(0, 1).toUpperCase() + role.substring(1).toLowerCase();
                break;
            }
            System.out.println("Invalid role. Please enter Admin or Member.");
        }

        users.add(new Users(id, name, email, role));
        saveUsersToFile(USER_DATA_FILE);
        System.out.println("✅ User registered successfully!");
    }

    // ✅ Login user
    private static Users login(Scanner sc) {
        System.out.print("Enter User ID: ");
        String id = sc.nextLine().trim();
        Users existing = findUserById(id);
        if (existing != null) {
            return existing;
        }
        System.out.println("❌ User not found. Try registering.");
        return null;
    }

    // ✅ Existing add/update methods remain unchanged...
    private static void addBookInteractively(Scanner sc, Library lib) {
        System.out.print("Enter new book ID: ");
        String id = sc.nextLine().trim();
        if (id.isEmpty()) {
            System.out.println("ID cannot be empty.");
            return;
        }
        if (lib.searchById(id) != null) {
            System.out.println("A book with that ID already exists.");
            return;
        }

        System.out.print("Enter title: ");
        String title = sc.nextLine().trim();
        System.out.print("Enter author: ");
        String author = sc.nextLine().trim();

        System.out.print("Enter publication year: ");
        String yearInput = sc.nextLine().trim();
        int year;
        try {
            year = Integer.parseInt(yearInput);
        } catch (NumberFormatException ex) {
            System.out.println("Invalid year.");
            return;
        }

        System.out.print("Enter quantity: ");
        String qtyInput = sc.nextLine().trim();
        int quantity;
        try {
            quantity = Integer.parseInt(qtyInput);
        } catch (NumberFormatException ex) {
            System.out.println("Invalid quantity.");
            return;
        }

        Book newBook = new Book(id, title, author, year, quantity);
        if (lib.add(newBook)) {
            System.out.println("Book added successfully.");
            persistState(lib);
        } else {
            System.out.println("Library is full. Could not add book.");
        }
    }

    private static void updateBookInteractively(Scanner sc, Library lib) {
        System.out.print("Enter ID of book to update: ");
        String id = sc.nextLine().trim();
        Book existing = lib.searchById(id);
        if (existing == null) {
            System.out.println("Book not found.");
            return;
        }

        System.out.print("Enter new title (leave blank to keep '" + existing.getTitle() + "'): ");
        String title = sc.nextLine().trim();
        if (title.isEmpty()) title = existing.getTitle();

        System.out.print("Enter new author (leave blank to keep '" + existing.getAuthor() + "'): ");
        String author = sc.nextLine().trim();
        if (author.isEmpty()) author = existing.getAuthor();

        System.out.print("Enter new publication year (leave blank to keep " + existing.getYearPublished() + "): ");
        String yearInput = sc.nextLine().trim();
        int year = existing.getYearPublished();
        if (!yearInput.isEmpty()) {
            try {
                year = Integer.parseInt(yearInput);
            } catch (NumberFormatException ex) {
                System.out.println("Invalid year. Keeping existing value.");
            }
        }

        System.out.print("Enter new quantity (leave blank to keep " + existing.getQuantity() + "): ");
        String qtyInput = sc.nextLine().trim();
        int quantity = existing.getQuantity();
        if (!qtyInput.isEmpty()) {
            try {
                quantity = Integer.parseInt(qtyInput);
            } catch (NumberFormatException ex) {
                System.out.println("Invalid quantity. Keeping existing value.");
            }
        }

        Book updated = new Book(existing.getId(), title, author, year, quantity);
        if (lib.update(id, updated)) {
            System.out.println("Book updated successfully.");
            persistState(lib);
        } else {
            System.out.println("Update failed.");
        }
    }

    private static void borrowBook(Scanner sc, Library lib, Users currentUser) {
        System.out.print("Enter ID to borrow: ");
        String borrowId = sc.nextLine().trim();
        Book book = lib.searchById(borrowId);
        if (book == null) {
            System.out.println("Not found.");
            return;
        }
        for (Book borrowed : currentUser.getBorrowedBooks()) {
            if (borrowed.getId().equals(book.getId())) {
                System.out.println("You already borrowed this book.");
                return;
            }
        }
        if (book.getQuantity() <= 0) {
            System.out.println("No copies available.");
            return;
        }
        currentUser.borrowBook(book);
        book.setQuantity(book.getQuantity() - 1);
        persistState(lib);
        System.out.println("Borrowed: " + book.getTitle());
    }

    private static boolean isAdmin(Users user) {
        return user != null && "ADMIN".equalsIgnoreCase(user.getRole());
    }

    private static void printMenuForUser(Users user) {
        System.out.println("\nLibrary Management Menu");
        if (isAdmin(user)) {
            System.out.println("1. List all books");
            System.out.println("2. Add book");
            System.out.println("3. Update book");
            System.out.println("4. Delete book");
            System.out.println("5. Search by ID");
            System.out.println("6. Generate report");
            System.out.println("7. View borrowed books");
            System.out.println("8. Borrow book");
            System.out.println("9. Save and exit");
        } else {
            System.out.println("1. List all books");
            System.out.println("2. Search by ID");
            System.out.println("3. View borrowed books");
            System.out.println("4. Borrow book");
            System.out.println("5. Exit");
        }
    }

    private static void showBorrowedBooks(Users user) {
        ArrayList<Book> borrowed = user.getBorrowedBooks();
        if (borrowed.isEmpty()) {
            System.out.println("No borrowed books.");
            return;
        }
        System.out.println("Borrowed books:");
        for (int i = 0; i < borrowed.size(); i++) {
            Book book = borrowed.get(i);
            System.out.println((i + 1) + ". " + book.toString());
        }
    }

    private static void loadUsersFromFile(String path, Library lib) {
        File file = new File(path);
        if (!file.exists()) {
            return;
        }
        users.clear();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.isBlank()) {
                    continue;
                }
                String[] parts = line.split(",", -1);
                if (parts.length < 4) {
                    continue;
                }
                String id = parts[0].trim();
                String name = parts[1].trim();
                String email = parts[2].trim();
                String role = parts[3].trim();
                Users user = new Users(id, name, email, role);
                if (parts.length >= 5) {
                    String borrowedSegment = parts[4].trim();
                    if (!borrowedSegment.isEmpty()) {
                        String[] borrowedIds = borrowedSegment.split("\\|");
                        for (String bookId : borrowedIds) {
                            String trimmedId = bookId.trim();
                            if (trimmedId.isEmpty()) {
                                continue;
                            }
                            Book borrowedBook = lib.searchById(trimmedId);
                            if (borrowedBook != null) {
                                user.borrowBook(borrowedBook);
                            }
                        }
                    }
                }
                users.add(user);
            }
            System.out.println("Loaded " + users.size() + " users from " + path);
        } catch (IOException ex) {
            System.out.println("Warning: could not load users: " + ex.getMessage());
        }
    }

    private static void saveUsersToFile(String path) {
        File file = new File(path);
        File parent = file.getParentFile();
        if (parent != null && !parent.exists()) {
            parent.mkdirs();
        }
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
            for (Users user : users) {
                StringBuilder borrowedBuilder = new StringBuilder();
                ArrayList<Book> borrowedBooks = user.getBorrowedBooks();
                for (int i = 0; i < borrowedBooks.size(); i++) {
                    if (i > 0) {
                        borrowedBuilder.append("|");
                    }
                    borrowedBuilder.append(borrowedBooks.get(i).getId());
                }
                bw.write(user.getUserId() + "," + user.getName() + "," + user.getEmail() + "," + user.getRole() + "," + borrowedBuilder);
                bw.newLine();
            }
        } catch (IOException ex) {
            System.out.println("Warning: could not save users: " + ex.getMessage());
        }
    }

    private static void persistState(Library lib) {
        try {
            lib.saveToFile(BOOK_DATA_FILE);
        } catch (IOException ex) {
            System.out.println("Warning: could not save books: " + ex.getMessage());
        }
        saveUsersToFile(USER_DATA_FILE);
    }

    private static Users findUserById(String userId) {
        for (Users user : users) {
            if (user.getUserId().equals(userId)) {
                return user;
            }
        }
        return null;
    }
}
