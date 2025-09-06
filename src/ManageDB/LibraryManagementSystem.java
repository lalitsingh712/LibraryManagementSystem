package ManageDB;

import java.sql.*;
import java.util.Scanner;

public class LibraryManagementSystem {
    private static final String URL = "jdbc:mysql://localhost:3306/library_db";
    private static final String USER = "root";  
    private static final String PASSWORD = "Indi@123"; 

    private static Connection connect() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    // Add a new book
    private static void addBook() {
        try (Connection conn = connect();
             PreparedStatement ps = conn.prepareStatement("INSERT INTO Books (title, author, available_copies) VALUES (?, ?, ?)")) {

            Scanner sc = new Scanner(System.in);
            System.out.print("Enter Book Title: ");
            String title = sc.nextLine();
            System.out.print("Enter Author: ");
            String author = sc.nextLine();
            System.out.print("Enter Available Copies: ");
            int copies = sc.nextInt();

            ps.setString(1, title);
            ps.setString(2, author);
            ps.setInt(3, copies);

            ps.executeUpdate();
            System.out.println("Book added successfully!");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // View all books
    private static void viewBooks() {
        try (Connection conn = connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM Books")) {

            System.out.println("Books in Library:");
            while (rs.next()) {
                System.out.println(rs.getInt("book_id") + " | " +
                        rs.getString("title") + " | " +
                        rs.getString("author") + " | Copies: " +
                        rs.getInt("available_copies"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Add a user
    private static void addUser() {
        try (Connection conn = connect();
             PreparedStatement ps = conn.prepareStatement("INSERT INTO Users (name, email) VALUES (?, ?)")) {

            Scanner sc = new Scanner(System.in);
            System.out.print("Enter User Name: ");
            String name = sc.nextLine();
            System.out.print("Enter Email: ");
            String email = sc.nextLine();

            ps.setString(1, name);
            ps.setString(2, email);
            ps.executeUpdate();

            System.out.println("User added successfully!");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Issue a book
    private static void issueBook() {
        try (Connection conn = connect()) {
            Scanner sc = new Scanner(System.in);
            System.out.print("Enter User ID: ");
            int userId = sc.nextInt();
            System.out.print("Enter Book ID: ");
            int bookId = sc.nextInt();

            // check if book is available
            PreparedStatement check = conn.prepareStatement("SELECT available_copies FROM Books WHERE book_id=?");
            check.setInt(1, bookId);
            ResultSet rs = check.executeQuery();

            if (rs.next() && rs.getInt("available_copies") > 0) {
                // update book copies
                PreparedStatement update = conn.prepareStatement("UPDATE Books SET available_copies = available_copies - 1 WHERE book_id=?");
                update.setInt(1, bookId);
                update.executeUpdate();

                // insert into transactions
                PreparedStatement insert = conn.prepareStatement("INSERT INTO Transactions (user_id, book_id, issue_date) VALUES (?, ?, CURDATE())");
                insert.setInt(1, userId);
                insert.setInt(2, bookId);
                insert.executeUpdate();

                System.out.println("Book issued successfully!");
            } else {
                System.out.println("Book not available!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Return a book
    private static void returnBook() {
        try (Connection conn = connect()) {
            Scanner sc = new Scanner(System.in);
            System.out.print("Enter Transaction ID: ");
            int transactionId = sc.nextInt();

            // get book_id from transaction
            PreparedStatement getBook = conn.prepareStatement("SELECT book_id FROM Transactions WHERE transaction_id=? AND return_date IS NULL");
            getBook.setInt(1, transactionId);
            ResultSet rs = getBook.executeQuery();

            if (rs.next()) {
                int bookId = rs.getInt("book_id");

                // update return_date
                PreparedStatement updateTransaction = conn.prepareStatement("UPDATE Transactions SET return_date=CURDATE() WHERE transaction_id=?");
                updateTransaction.setInt(1, transactionId);
                updateTransaction.executeUpdate();

                // increase book copies
                PreparedStatement updateBook = conn.prepareStatement("UPDATE Books SET available_copies = available_copies + 1 WHERE book_id=?");
                updateBook.setInt(1, bookId);
                updateBook.executeUpdate();

                System.out.println("Book returned successfully!");
            } else {
                System.out.println("Invalid Transaction ID or Book already returned!");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    //DELETE BOOKS
    private static void deleteBook(Scanner sc) {
        try (Connection conn = connect();
             PreparedStatement ps = conn.prepareStatement("DELETE FROM Books WHERE book_id=?")) {

            System.out.print("Enter Book ID to delete: ");
            int bookId = sc.nextInt();

            int rows = ps.executeUpdate();
            if (rows > 0) {
                System.out.println("✅ Book deleted successfully!");
            } else {
                System.out.println("❌ Book not found!");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Main Menu
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\n=== Library Management System ===");
            System.out.println("1. Add Book");
            System.out.println("2. View Books");
            System.out.println("3. Add User");
            System.out.println("4. Issue Book");
            System.out.println("5. Return Book");
            System.out.println("6. Delete Book");
            System.out.println("7. Exit");

            System.out.print("Choose option: ");
            int choice = sc.nextInt();

            switch (choice) {
                case 1 -> addBook();
                case 2 -> viewBooks();
                case 3 -> addUser();
                case 4 -> issueBook();
                case 5 -> returnBook();
                case 6 -> deleteBook(sc);
                case 7 -> {
                    System.out.println("Exiting... Goodbye!");
                    System.exit(0);
                }
                default -> System.out.println("Invalid choice, try again!");
            }
        }
    }
}
