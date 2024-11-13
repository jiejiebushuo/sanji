
package com.qst.exam;
import java.sql.*;
import java.sql.Date;
import java.util.*;
public class BookManager {
 public static void addBook(String bookId, String name, String author, String publisher, Date publishDate, String category) {
 String sql = "INSERT INTO Books (book_id, name, author, publisher, publish_date, category) VALUES (?, ?, ?, ?, ?, ?)";
 try (Connection conn = DBHelper.getConnection();
 PreparedStatement stmt = conn.prepareStatement(sql)) {
 stmt.setString(1, bookId);
 stmt.setString(2, name);
 stmt.setString(3, author);
 stmt.setString(4, publisher);
 stmt.setDate(5, publishDate);
 stmt.setString(6, category);
 stmt.executeUpdate();
 System.out.println("Book added successfully!");
 } catch (SQLException e) {
 e.printStackTrace();
 }
 }
 public static void updateBook(String bookId, String name, String author, String publisher, Date publishDate, String category) {
 String sql = "UPDATE Books SET name = ?, author = ?, publisher = ?, publish_date = ?, category = ? WHERE book_id = ?";
 try (Connection conn = DBHelper.getConnection();
 PreparedStatement stmt = conn.prepareStatement(sql)) {
 stmt.setString(1, name);
 stmt.setString(2, author);
 stmt.setString(3, publisher);
 stmt.setDate(4, publishDate);
 stmt.setString(5, category);
 stmt.setString(6, bookId);
 stmt.executeUpdate();
 System.out.println("Book updated successfully!");
 } catch (SQLException e) {
 e.printStackTrace();
 }
 }
 public static void deleteBook(String bookId) {
 String sql = "DELETE FROM Books WHERE book_id = ?";
 try (Connection conn = DBHelper.getConnection();
 PreparedStatement stmt = conn.prepareStatement(sql)) {
 stmt.setString(1, bookId);
 stmt.executeUpdate();
 System.out.println("Book deleted successfully!");
 } catch (SQLException e) {
 e.printStackTrace();
 }
 }
 public static List<Map<String, String>> getAllBooks() {
 String sql = "SELECT * FROM Books";
 List<Map<String, String>> books = new ArrayList<>();
 try (Connection conn = DBHelper.getConnection();
 Statement stmt = conn.createStatement();
 ResultSet rs = stmt.executeQuery(sql)) {
 while (rs.next()) {
 Map<String, String> book = new HashMap<>();
 book.put("book_id", rs.getString("book_id"));
 book.put("name", rs.getString("name"));
 book.put("author", rs.getString("author"));
 book.put("publisher", rs.getString("publisher"));
 book.put("publish_date", 
rs.getDate("publish_date").toString());
 book.put("category", rs.getString("category"));
 books.add(book);
 }
 } catch (SQLException e) {
 e.printStackTrace();
 }
 return books;
 }
}