package com.qst.exam;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Date;
import java.util.List;
import java.util.Map;
public class LibraryGUI {
 private JFrame frame;
 private JTable bookTable;
 private JTable borrowTable;
 public LibraryGUI() {
 frame = new JFrame("图书借阅管理系统");
 frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
 frame.setSize(800, 400);
 // Create tabs
 JTabbedPane tabbedPane = new JTabbedPane();
 frame.add(tabbedPane, BorderLayout.CENTER);
 // Create Book Management Tab
 JPanel bookPanel = new JPanel();
 bookPanel.setLayout(new BorderLayout());
 bookTable = new JTable();
 JScrollPane bookScrollPane = new JScrollPane(bookTable);
 bookPanel.add(bookScrollPane, BorderLayout.CENTER);
 JButton addBookButton = new JButton("添加图书");
 addBookButton.addActionListener(new ActionListener() {
 @Override
 public void actionPerformed(ActionEvent e) {
 // 弹出窗口让用户输入图书信息
 final JFrame addBookFrame = new JFrame("Add Book");
 addBookFrame.setSize(400, 300);
 
addBookFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
 // 创建面板
 JPanel panel = new JPanel();
 panel.setLayout(new GridLayout(7, 2));
 // 图书信息输入框
 final JTextField bookIdField = new JTextField();
 final JTextField nameField = new JTextField();
 final JTextField authorField = new JTextField();
 final JTextField publisherField = new JTextField();
 final JTextField publishDateField = new JTextField();
 String[] categories = {"小说", "计算机", "历史", "教育"};
 final JComboBox<String> categoryComboBox = new 
JComboBox<>(categories);
 panel.add(new JLabel("图书 ID:"));
 panel.add(bookIdField);
 panel.add(new JLabel("书名:"));
 panel.add(nameField);
 panel.add(new JLabel("作者:"));
 panel.add(authorField);
 panel.add(new JLabel("出版社:"));
 panel.add(publisherField);
 panel.add(new JLabel("出版日期"));
 panel.add(publishDateField);
 panel.add(new JLabel("所属分类:"));
 panel.add(categoryComboBox);
 JButton submitButton = new JButton("添加图书");
 submitButton.addActionListener(new ActionListener() {
 @Override
 public void actionPerformed(ActionEvent ae) {
 String bookId = bookIdField.getText();
 String name = nameField.getText();
 String author = authorField.getText();
 String publisher = publisherField.getText();
 String publishDateStr = publishDateField.getText();
 String category = (String) 
categoryComboBox.getSelectedItem();
 try {
 Date publishDate = 
Date.valueOf(publishDateStr); // 转换为 SQL Date
 BookManager.addBook(bookId, name, author, 
publisher, publishDate, category);
 loadBooks(); // 更新图书列表
 addBookFrame.dispose(); // 关闭窗口
 } catch (Exception ex) {
 
JOptionPane.showMessageDialog(addBookFrame, "Invalid date format or empty fields!");
 }
 }
 });
 panel.add(submitButton);
 addBookFrame.add(panel);
 addBookFrame.setVisible(true);
 }
 });
 JPanel bookActionsPanel = new JPanel();
 bookActionsPanel.add(addBookButton);
 bookPanel.add(bookActionsPanel, BorderLayout.SOUTH);
 tabbedPane.add("图书管理", bookPanel);
 // Create Borrow Management Tab
 JPanel borrowPanel = new JPanel();
 borrowPanel.setLayout(new BorderLayout());
 borrowTable = new JTable();
 JScrollPane borrowScrollPane = new JScrollPane(borrowTable);
 borrowPanel.add(borrowScrollPane, BorderLayout.CENTER);
 JButton returnBookButton = new JButton("归还图书");
 returnBookButton.addActionListener(new ActionListener() {
 @Override
 public void actionPerformed(ActionEvent e) {
 // Code to return a book
 }
 });
 JPanel borrowActionsPanel = new JPanel();
 borrowActionsPanel.add(returnBookButton);
 borrowPanel.add(borrowActionsPanel, BorderLayout.SOUTH);
 tabbedPane.add("借阅管理", borrowPanel);
 // Load initial data
 loadBooks();
 loadBorrowRecords();
 frame.setVisible(true);
 }
 private void loadBooks() {
 List<Map<String, String>> books = BookManager.getAllBooks();
 String[] columns = {"图书 ID", "书名", "作者", "出版社", "出版日期", "分类"};
 Object[][] data = new Object[books.size()][columns.length];
 for (int i = 0; i < books.size(); i++) {
 Map<String, String> book = books.get(i);
 data[i][0] = book.get("book_id");
 data[i][1] = book.get("name");
 data[i][2] = book.get("author");
 data[i][3] = book.get("publisher");
 data[i][4] = book.get("publish_date");
 data[i][5] = book.get("category");
 }
 bookTable.setModel(new javax.swing.table.DefaultTableModel(data, columns));
 }
 private void loadBorrowRecords() {
 List<Map<String, String>> borrowRecords = BorrowManager.getBorrowRecords();
 String[] columns = {"ID", "借阅人员姓名", "借阅时间", "归还时间", "是否归还", "图书"};
 Object[][] data = new Object[borrowRecords.size()][columns.length];
 for (int i = 0; i < borrowRecords.size(); i++) {
 Map<String, String> record = borrowRecords.get(i);
 data[i][0] = record.get("borrow_id");
 data[i][1] = record.get("borrower_name");
 data[i][2] = record.get("borrow_date");
 data[i][3] = record.get("return_date");
 data[i][4] = record.get("is_returned");
 data[i][5] = record.get("book_id");
 }
 borrowTable.setModel(new javax.swing.table.DefaultTableModel(data, columns));
 }
 public static void main(String[] args) {
 SwingUtilities.invokeLater(new Runnable() {
 @Override
 public void run() {
 new LibraryGUI();
 }
 });
 }
}