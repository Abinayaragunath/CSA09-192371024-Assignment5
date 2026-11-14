# B3 – JDBC Batch Processing and Transaction Testing

## 1. Objective

The objective is to understand how JDBC inserts multiple bus pass records efficiently using batch processing and database transactions.

## 2. JDBC Architecture

JDBC (Java Database Connectivity) is an API that allows Java applications to communicate with relational databases.

Main components:

1. Java Application – sends database requests.
2. JDBC API – provides database interfaces.
3. JDBC Driver – communicates with the database.
4. Database – stores and retrieves records.

General flow:

Java Application → JDBC API → JDBC Driver → Database

## 3. Statement vs PreparedStatement

| Feature | Statement | PreparedStatement |
|---|---|---|
| Query | Usually created dynamically | Uses parameter placeholders |
| Parameters | String concatenation | setString(), setInt(), etc. |
| SQL injection | More vulnerable if input is concatenated | Safer with bound parameters |
| Repeated execution | May require repeated query construction | Can reuse prepared query |
| Readability | Can become difficult | Usually clearer |

PreparedStatement is recommended for queries containing user input.

## 4. JDBC Batch Processing

Batch processing allows multiple SQL operations to be grouped and sent to the database together.

Important methods:

- `addBatch()` – adds a command to the batch.
- `executeBatch()` – executes the accumulated commands.
- `clearBatch()` – clears pending commands.

### Example pseudocode

```java
PreparedStatement ps = connection.prepareStatement(
    "INSERT INTO bus_pass VALUES (?, ?, ?, ?, ?)");

for (BusPass pass : passes) {
    ps.setString(1, pass.passId);
    ps.setString(2, pass.registerNumber);
    ps.setString(3, pass.studentName);
    ps.setInt(4, pass.route);
    ps.setDouble(5, pass.amount);

    ps.addBatch();
}

ps.executeBatch();
```

This is an illustrative example; it is not executed as part of this submission.

## 5. Batch Size of 1,000

For large datasets, records can be processed in batches of 1,000.

Pseudocode:

```text
SET batchSize = 1000
SET count = 0

FOR each valid bus pass:
    Add record to batch
    count = count + 1

    IF count MOD batchSize = 0:
        Execute batch
        Clear batch

IF remaining records exist:
    Execute remaining batch
```

Advantages:

- Reduces repeated database communication.
- Improves insertion efficiency.
- Handles large datasets in manageable groups.
- Avoids keeping the entire dataset in one batch.

## 6. Transaction Management

A transaction groups database operations into one logical unit.

Important JDBC methods:

| Method | Purpose |
|---|---|
| `setAutoCommit(false)` | Starts manual transaction management |
| `commit()` | Saves successful changes |
| `rollback()` | Cancels uncommitted changes |

### Transaction pseudocode

```text
Disable auto-commit

TRY:
    Read and validate records
    Insert records using batches
    Execute pending batch

    COMMIT transaction

CATCH error:
    ROLLBACK transaction
    Report failure
```

If an invalid amount is detected halfway through the transaction, the application should roll back the transaction so that no partial import remains.

## 7. Performance Comparison

For testing, consider these dataset sizes:

| Dataset | Purpose |
|---|---|
| 1,000 records | Small dataset |
| 10,000 records | Medium dataset |
| 100,000 records | Large dataset |

Expected comparison:

| Method | Expected performance |
|---|---|
| Statement | Repeated query execution can create overhead |
| PreparedStatement | Supports reusable parameterized queries |
| Batch processing | Reduces communication overhead for multiple inserts |

Actual execution times depend on database configuration, indexes, network latency, hardware and batch size.

No benchmark timings are claimed because the JDBC practical was not executed.

## 8. Correctness Testing Plan

The following tests should be performed in a database-enabled environment:

1. Insert 10 valid bus pass records.
2. Retrieve records using SELECT.
3. Verify retrieved values.
4. Inspect column metadata using ResultSetMetaData.
5. Insert an invalid amount halfway through a batch.
6. Verify that rollback leaves no partial records from the transaction.

## 9. Alternative Approaches

### Multi-row INSERT

Multiple records can be inserted using one SQL statement.

Advantages:
- Reduces the number of database requests.
- Useful for inserting several records together.

### LOAD DATA

MySQL LOAD DATA can load records from a file efficiently.

Advantages:
- Suitable for large file imports.
- Often faster than inserting records individually.

Both approaches require database-specific support and suitable validation.

## 10. Conclusion

JDBC batch processing improves the efficiency of inserting large numbers of records. PreparedStatement provides parameterized query execution, while transactions maintain data consistency through commit and rollback.

Actual JDBC execution and performance benchmarking remain pending because no database practical was performed.