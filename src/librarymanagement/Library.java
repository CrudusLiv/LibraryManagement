package librarymanagement;

import java.io.*;

/**
 * Library manages an array of Book objects. Demonstrates use of arrays and file I/O.
 */
public class Library {
    private Book[] books;
    private int count;

    public Library(int capacity) {
        books = new Book[capacity];
        count = 0;
    }

    public int size() { return count; }

    public Book get(int index) {
        if (index < 0 || index >= count) return null;
        return books[index];
    }
    // TO add book
    public boolean add(Book b) {
        if (count >= books.length) return false;
        books[count++] = b;
        return true;
    }
    //For Updating the Books
    public boolean update(String id, Book updated) {
        for (int i = 0; i < count; i++) {
            if (books[i].getId().equals(id)) {
                books[i] = updated;
                return true;
            }
        }
        return false;
    }
    // To delete the book list
    public boolean delete(String id) {
        for (int i = 0; i < count; i++) {
            if (books[i].getId().equals(id)) {
                // shift left
                for (int j = i; j < count - 1; j++) books[j] = books[j+1];
                books[--count] = null;
                return true;
            }
        }
        return false;
    }
    // To search the book by the id number
    public Book searchById(String id) {
        for (int i = 0; i < count; i++) if (books[i].getId().equals(id)) return books[i];
        return null;
    }
    // Listing all saved books in the Library
    public Book[] listAll() {
        Book[] result = new Book[count];
        for (int i = 0; i < count; i++) result[i] = books[i];
        return result;
    }

    // File I/O: load books from text file (id,title,author,year,quantity)
    public void loadFromFile(String path) throws IOException {
        File f = new File(path);
        if (!f.exists()) return; // nothing to load
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length < 5) continue;
                String id = parts[0].trim();
                String title = parts[1].trim();
                String author = parts[2].trim();
                int year = Integer.parseInt(parts[3].trim());
                int qty = Integer.parseInt(parts[4].trim());
                add(new Book(id, title, author, year, qty));
            }
        }
    }

    // Save to file
    public void saveToFile(String path) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(path))) {
            for (int i = 0; i < count; i++) {
                Book b = books[i];
                bw.write(String.format("%s,%s,%s,%d,%d", b.getId(), b.getTitle(), b.getAuthor(), b.getYearPublished(), b.getQuantity()));
                bw.newLine();
            }
        }
    }

    // Simple report: returns string with all books
    public String generateReport() {
        StringBuilder sb = new StringBuilder();
        sb.append("ID | Title | Author | Year | Qty\n");
        sb.append("-----------------------------------------\n");
        for (int i = 0; i < count; i++) sb.append(books[i].toString()).append("\n");
        return sb.toString();
    }
}
