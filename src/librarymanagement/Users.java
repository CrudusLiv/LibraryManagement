/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package librarymanagement;

/**
 *
 * @author User
 */
import java.util.ArrayList;

/**
 * Users class represents a library user (student, teacher, or admin).
 * This class can be extended later to support borrowing books.
 */
public class Users {
    private String userId;
    private String name;
    private String email;
    private String role; // Example: "Member" or "Admin"
    private ArrayList<Book> borrowedBooks;

    // Default constructor
    public Users() {
        borrowedBooks = new ArrayList<>();
    }

    // Parameterized constructor
    public Users(String userId, String name, String email, String role) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.role = role;
        this.borrowedBooks = new ArrayList<>();
    }

    // Getters & Setters
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public ArrayList<Book> getBorrowedBooks() {
        return borrowedBooks;
    }

    // Methods to borrow and return books (optional improvement)
    public void borrowBook(Book book) {
        borrowedBooks.add(book);
    }

    public void returnBook(Book book) {
        borrowedBooks.remove(book);
    }

    @Override
    public String toString() {
        return userId + " | " + name + " | " + email + " | " + role;
    }
}
