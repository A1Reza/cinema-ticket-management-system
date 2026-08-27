# 🎬 Cinema Ticket Management System

A Java-based **Cinema Ticket Management System** built with **JDK 21, JPA, Hibernate, PostgreSQL, Maven, and Design Patterns**.

The project demonstrates how to build a simple persistence-based cinema application using a layered architecture and several classic **Gang of Four (GoF) Design Patterns**.

---

## 🚀 Features

* 🎥 Movie management
* 👤 Customer management
* 🎟️ Ticket purchasing
* 🔎 Find movies by ID and title
* 🎬 Find available movies
* 👤 Find movies purchased by a customer
* 💾 CRUD operations using Repository Pattern
* 🔗 Bidirectional entity relationships
* 🔄 Transaction management with Hibernate
* 🛡️ Repository validation using Proxy Pattern
* 🔌 Legacy service integration using Adapter Pattern
* ⛓️ Ticket purchase validation using Chain of Responsibility
* 🧪 Unit tests with JUnit 5

---

## 🛠️ Technologies

| Technology                | Version     |
| ------------------------- | ----------- |
| Java                      | 21          |
| Hibernate ORM             | 7.0.4.Final |
| Jakarta Persistence (JPA) | 3.2         |
| PostgreSQL                | —           |
| Maven                     | —           |
| JUnit Jupiter             | 5.13.2      |
| JAXB Runtime              | 4.0.5       |

---

## 🏗️ Architecture

The project follows a layered architecture:

```text
Main
 │
 ├── Proxy
 │    └── MovieRepository
 │
 ├── Adapter
 │    └── OldMovieService
 │
 ├── Repository Layer
 │    ├── MovieRepository
 │    ├── CustomerRepository
 │    └── TicketRepository
 │
 ├── Chain of Responsibility
 │    ├── MovieAvailabilityHandler
 │    ├── CustomerValidationHandler
 │    └── TicketCreationHandler
 │
 └── HibernateUtil
      │
      └── EntityManagerFactory
             │
             └── PostgreSQL
```

---

## 📦 Domain Model

The system contains three main entities.

### Movie

```text
Movie
├── id
├── title
├── price
└── status
```

`MovieStatus` is an Enum:

```java
AVAILABLE
NOT_AVAILABLE
```

The Enum is stored in PostgreSQL as a readable string using:

```java
@Enumerated(EnumType.STRING)
```

### Customer

```text
Customer
├── id
├── name
└── email
```

### Ticket

```text
Ticket
├── id
├── purchaseDate
├── customer
└── movie
```

---

## 🔗 Entity Relationships

The relationship between the entities is:

```text
Customer 1 ───────── * Ticket * ───────── 1 Movie
```

Both relationships are **bidirectional**.

### Customer → Ticket

```java
@OneToMany(mappedBy = "customer")
private List<Ticket> tickets;
```

### Movie → Ticket

```java
@OneToMany(mappedBy = "movie")
private List<Ticket> tickets;
```

### Ticket → Customer

```java
@ManyToOne(fetch = FetchType.LAZY, optional = false)
@JoinColumn(name = "customer_id", nullable = false)
private Customer customer;
```

### Ticket → Movie

```java
@ManyToOne(fetch = FetchType.LAZY, optional = false)
@JoinColumn(name = "movie_id", nullable = false)
private Movie movie;
```

`Ticket` is the owning side of both relationships.

---

# 🎨 Design Patterns

The project implements several Design Patterns.

## 1. Singleton Pattern

`HibernateUtil` manages the application's `EntityManagerFactory`.

The Singleton ensures that only one `EntityManagerFactory` instance is created during the application lifetime.

```text
Application
     │
     ▼
HibernateUtil
     │
     ▼
EntityManagerFactory
```

It also provides helper methods for:

* EntityManager creation
* Transaction management
* Database operations

---

## 2. Repository Pattern

Each entity has its own Repository:

```text
MovieRepository
CustomerRepository
TicketRepository
```

Basic operations include:

```text
save()
findById()
update()
delete()
```

`MovieRepository` also provides:

```text
findByTitle()
findAvailableMovies()
findMoviesPurchasedByCustomer()
```

This separates persistence logic from the rest of the application.

---

## 3. Proxy Pattern

`MovieRepositoryProxy` implements the same interface as the real Movie Repository.

The Proxy performs validation before delegating operations to the real repository.

```text
Client
   │
   ▼
MovieRepositoryProxy
   │
   ▼
MovieRepositoryImpl
   │
   ▼
Database
```

For example, the Proxy validates that a movie ID is not `null` before calling the real repository.

---

## 4. Adapter Pattern

The project contains a legacy service:

```java
Movie getMovie(Long id);
```

while the new application expects:

```java
Movie findById(Long id);
```

`MovieRepositoryAdapter` allows the old service to work with the new Repository interface without modifying the legacy service.

```text
New Application
      │
      ▼
MovieRepository
      │
      ▼
MovieRepositoryAdapter
      │
      ▼
OldMovieService
```

---

## 5. Chain of Responsibility

The ticket purchasing process uses Chain of Responsibility.

```text
TicketRequest
      │
      ▼
MovieAvailabilityHandler
      │
      ▼
CustomerValidationHandler
      │
      ▼
TicketCreationHandler
      │
      ▼
Ticket
```

### MovieAvailabilityHandler

Checks whether the movie is:

```text
AVAILABLE
```

If the movie is unavailable, the request stops.

### CustomerValidationHandler

Checks whether the customer exists.

If the customer does not exist, the request stops.

### TicketCreationHandler

If all validations succeed:

1. Finds the Movie
2. Finds the Customer
3. Creates a Ticket
4. Associates the Customer and Movie
5. Persists the Ticket

The ticket creation process is executed inside a transaction.

---

# 🗄️ Database

The project uses **PostgreSQL**.

The Persistence Unit is:

```text
cinema-ticket-unit
```

The database contains tables corresponding to the entities:

```text
movies
customers
tickets
```

The `tickets` table contains foreign keys:

```text
customer_id → customers.id
movie_id    → movies.id
```

---

# 🔍 JPQL Queries

The project uses JPQL for custom queries.

### Find Movie by Title

```java
findByTitle(String title)
```

Uses a named JPQL parameter.

### Find Available Movies

```java
findAvailableMovies()
```

Uses JPQL to find movies with:

```text
MovieStatus.AVAILABLE
```

A `@NamedQuery` is also defined:

```java
@NamedQuery(
    name = "Movie.findAvailable",
    query = "SELECT m FROM Movie m WHERE m.status = :status"
)
```

### Find Movies Purchased by Customer

The project uses a JPQL `JOIN` between:

```text
Movie
Ticket
Customer
```

to retrieve movies purchased by a specific customer.

---

# 🧪 Testing

JUnit 5 is used for repository testing.

## Test 1 — Save and Find Movie

```text
testSaveAndFindMovie()
```

The test verifies:

* Movie can be saved
* Movie can be retrieved by ID
* Title is correct
* Price is correct
* Movie status is correctly stored and retrieved

---

## Test 2 — Find Available Movies

```text
testFindAvailableMovies()
```

The test creates:

```text
Interstellar → AVAILABLE
Inception    → NOT_AVAILABLE
```

Then verifies that:

```text
Interstellar → returned
Inception    → not returned
```

---

# 📁 Project Structure

```text
cinema-ticket-management-system
│
├── src
│   ├── main
│   │   ├── java
│   │   │   └── ir
│   │   │       └── reza
│   │   │           └── cinema
│   │   │               │
│   │   │               ├── adapter
│   │   │               │   ├── MovieRepositoryAdapter.java
│   │   │               │   └── OldMovieService.java
│   │   │               │
│   │   │               ├── chain
│   │   │               │   ├── TicketHandler.java
│   │   │               │   ├── TicketRequest.java
│   │   │               │   ├── MovieAvailabilityHandler.java
│   │   │               │   ├── CustomerValidationHandler.java
│   │   │               │   └── TicketCreationHandler.java
│   │   │               │
│   │   │               ├── entity
│   │   │               │   ├── Movie.java
│   │   │               │   ├── MovieStatus.java
│   │   │               │   ├── Customer.java
│   │   │               │   └── Ticket.java
│   │   │               │
│   │   │               ├── proxy
│   │   │               │   └── MovieRepositoryProxy.java
│   │   │               │
│   │   │               ├── repository
│   │   │               │   ├── movie
│   │   │               │   ├── customer
│   │   │               │   └── ticket
│   │   │               │
│   │   │               ├── util
│   │   │               │   └── HibernateUtil.java
│   │   │               │
│   │   │               └── Main.java
│   │   │
│   │   └── resources
│   │       └── META-INF
│   │           └── persistence.xml
│   │
│   └── test
│       └── java
│           └── ir
│               └── reza
│                   └── cinema
│                       └── repository
│                           └── movie
│                               └── MovieRepositoryTest.java
│
├── .gitignore
├── pom.xml
└── README.md
```

---

# ⚙️ Requirements

Before running the project, make sure you have:

* JDK 21
* Maven
* PostgreSQL
* IntelliJ IDEA or another Java IDE

---

# ▶️ How to Run

### 1. Clone the repository

```bash
git clone https://github.com/A1Reza/cinema-ticket-management-system.git
```

### 2. Create the PostgreSQL database

Create a database named:

```text
cinema_db
```

### 3. Configure PostgreSQL

Update the database configuration in:

```text
src/main/resources/META-INF/persistence.xml
```

Set your own PostgreSQL:

```text
username
password
database URL
```

Do not commit real database credentials to a public repository.

### 4. Build the project

```bash
mvn clean install
```

### 5. Run the application

Run:

```text
Main.java
```

---

# 💻 Example Output

```text
==========================================
    CINEMA TICKET MANAGEMENT SYSTEM
==========================================

[1] Repositories initialized successfully.
[2] Adapter initialized successfully.

[3] Movies created:
    - Interstellar
    - Inception

[4] Customers created:
    - Reza
    - Ali

[5] Movie found by ID:

    ID     : 1
    Title  : Interstellar
    Price  : 15.99
    Status : AVAILABLE

[6] Movie price updated:

    New Price : 17.99

[7] Available Movies
------------------------------------------
    ID     : 1
    Title  : Interstellar
    Price  : 17.99
    Status : AVAILABLE
------------------------------------------

[8] Adapter Pattern
------------------------------------------
    Old Service Method : getMovie(id)
    New Interface      : findById(id)
    Adapted Movie      : Interstellar

[9] Ticket Purchase Chain
------------------------------------------
    Ticket Request
         |
         v
    Movie Availability
         |
         v
    Customer Validation
         |
         v
    Ticket Creation
------------------------------------------

[10] Ticket Created Successfully
==========================================

    Ticket ID      : 1
    Customer       : Reza
    Customer Email : reza@example.com
    Movie          : Interstellar
    Movie Price    : 17.99
    Movie Status   : AVAILABLE

==========================================
    OPERATION COMPLETED SUCCESSFULLY
==========================================
```

---

# 📚 Learning Goals

This project was developed to practice and demonstrate:

* Java 21
* Object-Oriented Programming
* JPA and Hibernate
* Entity relationships
* Bidirectional associations
* Lazy loading
* Transactions
* Repository Pattern
* Singleton Pattern
* Proxy Pattern
* Adapter Pattern
* Chain of Responsibility
* JPQL
* Named Queries
* PostgreSQL
* JUnit 5
* Maven

---

## 👨‍💻 Author

**Reza Asadipour**

GitHub: [A1Reza](https://github.com/A1Reza)

---

## 📄 License

This project is created for educational and learning purposes.
