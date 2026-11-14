# B3 – JDBC Batch Processing: Practical Simulation

## 1. Objective

To demonstrate batch processing, successful record insertion, failure handling and rollback using Java.

**Note:** This is an in-memory Java simulation. Actual JDBC connectivity and database execution were not performed.

## 2. Technologies Used

- Java
- ArrayList
- List
- Exception handling
- IntelliJ IDEA / VS Code
- Command Prompt / PowerShell

## 3. Implementation

The program uses a `BusPass` class to represent bus-pass records.

The `executeBatch()` method processes a group of records. It stores the initial record count before processing.

If an invalid amount is encountered, the program removes all records added during that batch. This simulates rollback.

## 4. Test Cases

| Test | Input | Expected result |
|---|---|---|
| Successful batch | 10 valid records | 10 records inserted |
| Invalid batch | One record with negative amount | Batch rejected |
| Rollback | Failed batch | Previous records retained |

## 5. Result

- First batch: 10 records inserted successfully.
- Second batch: Processing stopped when an invalid amount was encountered.
- Rollback simulation: Newly added records were removed.
- Final record count: 10.

## 6. Conclusion

The Java simulation demonstrates the basic concepts of batch processing and rollback. It shows how a failed batch can be prevented from leaving partially inserted records.

Actual JDBC `PreparedStatement`, `addBatch()`, `executeBatch()`, and database transaction operations require a database connection and were not executed.
