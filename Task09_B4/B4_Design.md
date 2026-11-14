# B4 – Data Migration and Database Metadata

## 1. Objective

The objective is to understand how bus pass records can be migrated from a text file into a database, how migration failures are handled, and how database metadata is retrieved using JDBC.

## 2. File-to-Database Migration

Data migration is the process of transferring records from one storage system to another.

Example:

Source: buspasses.csv  
Destination: Bus Pass Database

Migration steps:

1. Open the source file.
2. Read records one by one.
3. Validate each record.
4. Separate valid and invalid records.
5. Insert valid records into the database using JDBC batches.
6. Commit successful transactions.
7. Generate a report for failed records.

## 3. Batch Migration

Batch migration inserts multiple records together instead of inserting them individually.

Pseudocode:

```text
OPEN source file
DISABLE auto-commit

FOR each record in source file:

    TRY:
        Validate record

        IF valid:
            Add record to JDBC batch
        ELSE:
            Write record and reason to failure report

    CATCH validation error:
        Write record and error reason to failure report

    IF batch contains 1000 valid records:
        Execute batch

EXECUTE remaining batch
COMMIT transaction

IF database error occurs:
    ROLLBACK transaction
    Record migration failure

CLOSE file and database resources
```

## 4. Failure Reporting

Some records may contain missing fields, invalid amounts or incorrect data types.

These records should not be inserted into the database.

Example failure report:

| Record | Error | Action |
|---|---|---|
| P1002 | Missing field | Rejected |
| P1003 | Invalid amount | Rejected |
| P1005 | Invalid route | Rejected |

A failure report helps administrators identify and correct damaged records.

The migration process should distinguish between rejected individual records and database-level failures. A database failure during an all-or-nothing transaction may require rolling back the complete transaction.

## 5. DatabaseMetaData

JDBC provides the DatabaseMetaData interface to retrieve information about the database and JDBC driver.

Example Java code:

```java
DatabaseMetaData metadata =
    connection.getMetaData();

System.out.println(
    metadata.getDatabaseProductName());

System.out.println(
    metadata.getDatabaseProductVersion());

System.out.println(
    metadata.getDriverName());

System.out.println(
    metadata.getDriverVersion());
```

Important methods:

| Method | Purpose |
|---|---|
| getDatabaseProductName() | Database name |
| getDatabaseProductVersion() | Database version |
| getDriverName() | JDBC driver name |
| getDriverVersion() | JDBC driver version |
| getTables() | Retrieves table information |

DatabaseMetaData helps developers understand the database environment and support different database configurations.

## 6. Choosing Storage for a 12-Campus System

Consider a university with:

- 12 campuses.
- 100 times more data than the current system.
- Multiple users accessing records concurrently.

A relational database is the recommended solution.

Reasons:

1. Supports concurrent users.
2. Provides transactions and data consistency.
3. Supports indexing for faster searches.
4. Provides centralized data management.
5. Supports backup and recovery.
6. Allows campus-wise access control.
7. Handles large datasets more effectively than separate files.

A database can also support campus-specific records using a campus ID.

## 7. Alternative: Separate Serialized Files

Another approach is maintaining a separate serialized file for each campus.

Advantages:
- Simple for small independent datasets.
- Does not require a database server.
- Can store Java object graphs directly.

Disadvantages:
- Searching across campuses becomes difficult.
- Concurrent file updates require careful synchronization.
- File corruption can affect stored data.
- Backup and consistency management become harder.
- Scaling to large datasets is difficult.

Therefore, separate serialized files are less suitable for a large university system with many concurrent users.

## 8. Conclusion

File-to-database migration should use validation, batch processing, transactions and failure reports. JDBC DatabaseMetaData provides useful information about the database environment. For a large multi-campus system with high data volume and concurrent access, a relational database is more suitable than separate serialized files.