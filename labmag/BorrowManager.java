
package com.qst.exam;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
public class BorrowManager {
 public static void borrowBook(String borrowerName, Date borrowDate, 
String bookId) {
 String sql = "INSERT INTO BorrowRecords (borrower_name, borrow_date, book_id) VALUES (?, ?, ?)";
 try (Connection conn = DBHelper.getConnection();
 PreparedStatement stmt = conn.prepareStatement(sql)) {
 stmt.setString(1, borrowerName);
 stmt.setDate(2, borrowDate);
 stmt.setString(3, bookId);
 stmt.executeUpdate();
 System.out.println("Book borrowed successfully!");
 } catch (SQLException e) {
 e.printStackTrace();
 }
 }
 public static void returnBook(int borrowId, Date returnDate) {
 String sql = "UPDATE BorrowRecords SET return_date = ?, is_returned = TRUE WHERE borrow_id = ?";
 try (Connection conn = DBHelper.getConnection();
 PreparedStatement stmt = conn.prepareStatement(sql)) {
 stmt.setDate(1, returnDate);
 stmt.setInt(2, borrowId);
 stmt.executeUpdate();
 System.out.println("Book returned successfully!");
 } catch (SQLException e) {
 e.printStackTrace();
 }
 }
 public static List<Map<String, String>> getBorrowRecords() {
 String sql = "SELECT * FROM BorrowRecords";
 List<Map<String, String>> borrowRecords = new ArrayList<>();
 try (Connection conn = DBHelper.getConnection();
 Statement stmt = conn.createStatement();
 ResultSet rs = stmt.executeQuery(sql)) {
 while (rs.next()) {
 Map<String, String> record = new HashMap<>();
 record.put("borrow_id", 
String.valueOf(rs.getInt("borrow_id")));
 record.put("borrower_name", 
rs.getString("borrower_name"));
 record.put("borrow_date", 
rs.getDate("borrow_date").toString());
 record.put("return_date", rs.getDate("return_date") != null ? 
rs.getDate("return_date").toString() : "Not returned");
 record.put("is_returned", rs.getBoolean("is_returned") ? 
"Yes" : "No");
 record.put("book_id", rs.getString("book_id"));
 borrowRecords.add(record);
 }
 } catch (SQLException e) {
 e.printStackTrace();
 }
 return borrowRecords;
 }
}