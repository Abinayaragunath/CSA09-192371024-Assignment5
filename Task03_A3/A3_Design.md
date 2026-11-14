# A3 – Data Access Object (DAO) Design Pattern

## 1. Problem Description

Consider a student management application containing three modules:

- StudentDB
- CourseDB
- MarksDB

If every module contains repeated database connection and SQL code, maintaining the application becomes difficult.

The DAO (Data Access Object) design pattern solves this problem by separating database operations from business logic.

## 2. What is DAO?

DAO is a design pattern that provides a separate layer for accessing and managing data.

Its main responsibilities are:

- Insert records.
- Find records.
- Update records.
- Delete records.
- Retrieve all records.

The application communicates with the DAO instead of directly executing database operations.

## 3. Generic DAO Interface

A generic DAO interface can be designed as follows:

```java
public interface Dao<T, K> {
    boolean insert(T object);
    T findById(K id);
    List<T> findAll();
    boolean update(T object);
    boolean delete(K id);
}
```

Here:

- T represents the entity type.
- K represents the primary key type.
- insert() adds a record.
- findById() searches for a record.
- findAll() retrieves all records.
- update() modifies a record.
- delete() removes a record.

## 4. StudentDao Implementation

StudentDao implements the generic DAO interface.

```java
public class StudentDao implements Dao<Student, Integer> {

    public boolean insert(Student student) {
        // Insert student record
        return true;
    }

    public Student findById(Integer id) {
        // Find student using ID
        return null;
    }

    public List<Student> findAll() {
        // Retrieve all students
        return new ArrayList<>();
    }

    public boolean update(Student student) {
        // Update student record
        return true;
    }

    public boolean delete(Integer id) {
        // Delete student record
        return true;
    }
}
```

This is a structural example. Actual database operations would be implemented inside these methods.

## 5. Object-to-Table Mapping

The DAO maps Java objects to database tables.

Example:

| Java object | Database table |
|---|---|
| Student | students |
| Course | courses |
| Marks | marks |

The DAO converts application objects into database records and converts retrieved records back into Java objects.

## 6. Why Not Write SQL in GUI or Service Classes?

Writing SQL directly inside GUI or service classes creates tight coupling.

Disadvantages:

- Database code gets repeated.
- Changes become difficult to maintain.
- Testing becomes harder.
- Changing the database implementation affects multiple classes.

With DAO, database operations remain in a separate layer. Changes to data access logic can be handled with less impact on the GUI and service layers.

## 7. Time and Space Complexity

Assuming a database table contains n records:

| Operation | Typical time complexity |
|---|---|
| Insert | O(1) amortized, excluding database overhead |
| Find by indexed ID | O(log n) typically for a B-tree index |
| Find all | O(n) |
| Update by indexed ID | O(log n) typically |
| Delete by indexed ID | O(log n) typically |

The actual performance depends on database indexes, query plans, constraints and storage.

The DAO object itself generally uses O(1) extra space per operation, excluding returned collections and data held by the database driver.

## 8. Conclusion

The DAO design pattern separates database access logic from application logic. A generic DAO interface reduces code duplication and improves maintainability, reusability and testability.
