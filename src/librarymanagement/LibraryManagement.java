/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package librarymanagement;

/**
 *
 * @author livne
 */
public class LibraryManagement {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        java.util.Scanner sc = new java.util.Scanner(System.in);
        Library lib = new Library(100);
        String dataFile = "data/books.txt";  // Use same file for input and output
        String dataOut = "data/books_out.txt";  // Backup/report file
        try {
            lib.loadFromFile(dataFile);
            System.out.println("Loaded " + lib.size() + " records from " + dataFile);
        } catch (Exception e) {
            System.out.println("Warning: could not load data: " + e.getMessage());
        }

        boolean running = true;
        while (running) {
            System.out.println("\nLibrary Management Menu");
            System.out.println("1. List all books");
            System.out.println("2. Add book");
            System.out.println("3. Update book");
            System.out.println("4. Delete book");
            System.out.println("5. Search by ID");
            System.out.println("6. Generate report (and save)");
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
                    if (lib.delete(delId)) System.out.println("Deleted."); else System.out.println("Not found.");
                    break;
                case "5":
                    System.out.print("Enter ID to search: ");
                    String sid = sc.nextLine().trim();
                    Book found = lib.searchById(sid);
                    System.out.println(found == null ? "Not found" : found.toString());
                    break;
                case "6":
                    String rpt = lib.generateReport();
                    System.out.println(rpt);
                    try (java.io.BufferedWriter bw = new java.io.BufferedWriter(new java.io.FileWriter(dataOut))) {
                        bw.write(rpt);
                        System.out.println("Report saved to " + dataOut);
                    } catch (Exception e) { System.out.println("Could not save report: " + e.getMessage()); }
                    break;
                case "7":
                    try {
                        lib.saveToFile(dataFile);  // Save back to original file
                        System.out.println("Saved " + lib.size() + " records to " + dataFile);
                    } catch (Exception e) { System.out.println("Save failed: " + e.getMessage()); }
                    running = false;
                    break;
                default:
                    System.out.println("Invalid option.");
            }
        }
        sc.close();
    }

    private static void addBookInteractively(java.util.Scanner sc, Library lib) {
        System.out.print("ID: ");
        String id = sc.nextLine().trim();
        if (id.isEmpty()) { System.out.println("ID cannot be empty."); return; }
        System.out.print("Title: ");
        String title = sc.nextLine().trim();
        System.out.print("Author: ");
        String author = sc.nextLine().trim();
        System.out.print("Year Published: ");
        String y = sc.nextLine().trim();
        int year = 0; try { year = Integer.parseInt(y); } catch (NumberFormatException ex) { System.out.println("Invalid year."); return; }
        System.out.print("Quantity: ");
        String q = sc.nextLine().trim();
        int qty = 0; try { qty = Integer.parseInt(q); } catch (NumberFormatException ex) { System.out.println("Invalid quantity."); return; }
        Book b = new Book(id, title, author, year, qty);
        if (lib.add(b)) System.out.println("Added."); else System.out.println("Library is full.");
    }

    private static void updateBookInteractively(java.util.Scanner sc, Library lib) {
        System.out.print("Enter ID to update: ");
        String id = sc.nextLine().trim();
        Book existing = lib.searchById(id);
        if (existing == null) { System.out.println("Not found."); return; }
        System.out.println("Leave blank to keep existing value.");
        System.out.print("New Title (current: " + existing.getTitle() + "): ");
        String title = sc.nextLine().trim(); if (title.isEmpty()) title = existing.getTitle();
        System.out.print("New Author (current: " + existing.getAuthor() + "): ");
        String author = sc.nextLine().trim(); if (author.isEmpty()) author = existing.getAuthor();
        System.out.print("New Year (current: " + existing.getYearPublished() + "): ");
        String y = sc.nextLine().trim(); int year = existing.getYearPublished(); if (!y.isEmpty()) try { year = Integer.parseInt(y); } catch (NumberFormatException ex) { System.out.println("Invalid year; update cancelled."); return; }
        System.out.print("New Quantity (current: " + existing.getQuantity() + "): ");
        String q = sc.nextLine().trim(); int qty = existing.getQuantity(); if (!q.isEmpty()) try { qty = Integer.parseInt(q); } catch (NumberFormatException ex) { System.out.println("Invalid qty; update cancelled."); return; }
        Book updated = new Book(id, title, author, year, qty);
        if (lib.update(id, updated)) System.out.println("Updated."); else System.out.println("Update failed.");
    }
    
}
