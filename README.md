# Library Management System

A Java console application for managing a library's book catalog and user accounts.

## Features

**Admin users** can:
- List all books
- Add, update, and delete books
- Search books by ID
- Generate a catalog report
- Borrow books

**Member users** can:
- List all books
- Search books by ID
- View their borrowed books
- Borrow books

## Project Structure

```
LibraryManagement/
├── src/librarymanagement/
│   ├── Item.java              # Base class for library items
│   ├── Book.java              # Book entity (extends Item)
│   ├── Library.java           # Array-backed book catalog with file I/O
│   ├── Users.java             # User entity with borrow tracking
│   └── LibraryManagement.java # Main application entry point
├── data/
│   ├── books.txt              # Persisted book catalog
│   └── users.txt              # Persisted user accounts
└── build.xml                  # NetBeans Ant build file
```

## Data Format

**books.txt** — one book per line:
```
id,title,author,year,quantity
```

**users.txt** — one user per line:
```
userId,name,email,role,borrowedBookId1|borrowedBookId2
```

## Getting Started

### Prerequisites

- Java 8 or later
- NetBeans IDE (or any Java IDE / `javac` on the command line)

### Build & Run (NetBeans)

1. Open the project in NetBeans.
2. Click **Run > Run Project** (F6).

### Build & Run (command line)

```bash
javac -d build/classes src/librarymanagement/*.java
java -cp build/classes librarymanagement.LibraryManagement
```

The application loads `data/books.txt` and `data/users.txt` on startup and saves changes back to those files on exit or whenever a modification is made.

## Usage

On launch you are prompted to **login** with an existing User ID or **register** a new account. Roles available during registration: `Admin` or `Member`.

After login the role-appropriate menu is displayed. All changes (new books, borrows, registrations) are persisted immediately to the data files.

## Class Overview

| Class | Responsibility |
|---|---|
| `Item` | Abstract base — id, title, author |
| `Book` | Extends `Item` — adds year and quantity |
| `Library` | Fixed-capacity array of books; CRUD + file I/O |
| `Users` | User account with borrowed-book list |
| `LibraryManagement` | Entry point; user session, menus, persistence |
