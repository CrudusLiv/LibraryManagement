/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package librarymanagement;

import java.util.ArrayList;
import java.util.Scanner;

public class LibraryManagement {

    static ArrayList<Users> users = new ArrayList<>();

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Library lib = new Library(100);
        String dataFile = "data/books.txt";

        try {
            lib.loadFromFile(dataFile);
            System.out.println("Loaded " + lib.size() + " records from " + dataFile);
        } catch (Exception e) {
            System.out.println("Warning: could not load data: " + e.getMessage());
        }

        // 🔹 USER LOGIN OR REGISTER FIRST
        Users currentUser = loginOrRegister(sc);
        if (currentUser == null) {
            System.out.println("Login failed. Exiting...");
            return;
        }

        System.out.println("\n✅ Welcome, " + currentUser.getName() + " (" + currentUser.getRole() + ")!");

        // 🔹 Proceed to existing Library menu
        boolean running = true;
        while (running) {
            System.out.println("\nLibrary Management Menu");
            System.out.println("1. List all books");
            System.out.println("2. Add book");
            System.out.println("3. Update book");
            System.out.println("4. Delete book");
            System.out.println("5. Search by ID");
            System.out.println("6. Generate report");
            System.out.println("7. Save and exit");
            System.out.print("Choose an option: ");

            String opt = sc.nextLine().trim();
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
                    if (lib.delete(delId)) System.out.println("Deleted."); 
                    else System.out.println("Not found.");
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
                    try {
                        lib.saveToFile(dataFile);
                        System.out.println("Saved " + lib.size() + " records.");
                    } catch (Exception e) { 
                        System.out.println("Save failed: " + e.getMessage()); 
                    }
                    running = false;
                    break;
                default:
                    System.out.println("Invalid option.");
            }
        }

        sc.close();
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
        System.out.print("Enter name: ");
        String name = sc.nextLine().trim();
        System.out.print("Enter email: ");
        String email = sc.nextLine().trim();
        System.out.print("Enter role (Admin/Member): ");
        String role = sc.nextLine().trim();

        users.add(new Users(id, name, email, role));
        System.out.println("✅ User registered successfully!");
    }

    // ✅ Login user
    private static Users login(Scanner sc) {
        System.out.print("Enter User ID: ");
        String id = sc.nextLine().trim();
        for (Users u : users) {
            if (u.getUserId().equals(id)) {
                return u;
            }
        }
        System.out.println("❌ User not found. Try registering.");
        return null;
    }

    // ✅ Existing add/update methods remain unchanged...
    private static void addBookInteractively(Scanner sc, Library lib) { /* same as your code */ }
    private static void updateBookInteractively(Scanner sc, Library lib) { /* same as your code */ }
}
