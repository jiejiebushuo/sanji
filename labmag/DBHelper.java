package com.qst.exam;
import java.sql.*;
public class DBHelper {
   private static final String URL = "jdbc:mysql://localhost:3306/library_system"; 
   // 替换为你的数据库 URL
   private static final String USER = "root"; 
   // 替换为你的数据库用户名
   private static final String PASSWORD = "qwer1234"; 
 // 替换为你的数据库密码
   public static Connection getConnection() throws SQLException {
      return DriverManager.getConnection(URL, USER, PASSWORD);
   }
   public static void closeConnection(Connection connection, Statement stmt, ResultSet rs) {
         try {
            if (rs != null) {
               rs.close();
            }
            if (stmt != null) {
               stmt.close();
            }
            if (connection != null) {
               connection.close();
            }
         } catch (SQLException e) {
            e.printStackTrace();
         }
   }
}
