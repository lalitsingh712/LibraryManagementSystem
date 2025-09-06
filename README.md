# 📚 Library Management System (Java + JDBC + MySQL)

This project is a **console-based Library Management System** developed in **Java** using **JDBC** for database connectivity with MySQL.  
It is part of my internship tasks at **Codveda Technologies**.

---

## 💻 Features
- ➕ Add new books to the library  
- 👤 Add new users  
- 📖 View all available books  
- 📦 Issue a book to a user  
- 🔄 Return a book (update status)  
- ❌ Delete a book from the library  
- 🗄️ MySQL database integration (Books, Users, Transactions)

---

## 🛠️ Technologies Used
- **Java (JDK 17/21)**  
- **Eclipse IDE**  
- **MySQL 8.0**  
- **JDBC (MySQL Connector/J)**  

---

## ⚙️ Database Setup
```sql
CREATE DATABASE library_db;
USE library_db;

CREATE TABLE Books (
  book_id INT PRIMARY KEY AUTO_INCREMENT,
  title VARCHAR(100),
  author VARCHAR(100),
  available_copies INT
);

CREATE TABLE Users (
  user_id INT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(100),
  email VARCHAR(100)
);

CREATE TABLE Transactions (
  transaction_id INT PRIMARY KEY AUTO_INCREMENT,
  user_id INT,
  book_id INT,
  issue_date DATE,
  return_date DATE,
  FOREIGN KEY (user_id) REFERENCES Users(user_id),
  FOREIGN KEY (book_id) REFERENCES Books(book_id)
);
