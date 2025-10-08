package librarymanagement;

/**
 * Book is a concrete subclass of Item. Demonstrates inheritance.
 */
public class Book extends Item {
    private int yearPublished;
    private int quantity;

    public Book() { super(); }

    public Book(String id, String title, String author, int yearPublished, int quantity) {
        super(id, title, author);
        this.yearPublished = yearPublished;
        this.quantity = quantity;
    }

    public int getYearPublished() {
        return yearPublished;
    }

    public void setYearPublished(int yearPublished) {
        this.yearPublished = yearPublished;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return super.toString() + " | " + yearPublished + " | " + quantity;
    }
}
