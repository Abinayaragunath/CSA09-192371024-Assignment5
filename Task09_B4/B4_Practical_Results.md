# B4 – Data Migration Practical Results

## 1. Objective

To demonstrate the migration of bus-pass records from an input text file into a separate output file using Java.

The program validates records, processes valid records in batches, and generates a failure report for invalid records.

**Note:** This practical uses Java file handling to simulate migration. It does not perform an actual JDBC or database migration.

## 2. Technologies Used

- Java
- FileReader and FileWriter
- BufferedReader and BufferedWriter
- ArrayList
- Exception handling
- PowerShell
- VS Code

## 3. Input Data

The program generates an input file named `buspasses.csv` containing seven records:

- Four valid bus-pass records.
- One record with an incorrect field count.
- One record with a non-numeric amount.
- One empty record.

## 4. Migration Process

1. Read the input file line by line.
2. Split each record into five fields.
3. Validate the pass ID, register number, student name, route and amount.
4. Store valid records separately from invalid records.
5. Process valid records in batches of three.
6. Write migrated records to `migrated_records.csv`.
7. Write validation failures to `failed_records.txt`.
8. Display the migration summary.

## 5. Batch Processing

The configured batch size is three records.

| Batch | Records processed | Result |
|---|---:|---|
| Batch 1 | 3 | Successful |
| Batch 2 | 1 | Successful |
| Total | 4 | Successful |

## 6. Validation Results

| Input line | Problem | Result |
|---|---|---|
| 1 | Valid record | Accepted |
| 2 | Valid record | Accepted |
| 3 | Valid record | Accepted |
| 4 | Incorrect field count | Rejected |
| 5 | Invalid amount | Rejected |
| 6 | Empty record | Rejected |
| 7 | Valid record | Accepted |

## 7. Generated Output Files

**Output/migrated_records.csv**

Contains the four successfully migrated records:

- P1001 – Arun
- P1002 – Bala
- P1003 – Divya
- P1007 – Ravi

**Output/failed_records.txt**

Contains the three validation errors:

- Line 4: Incorrect field count
- Line 5: Invalid route or amount
- Line 6: Empty record

## 8. Performance and Complexity

Let N be the number of input records.

- Reading and validating records: O(N) time.
- Separating valid and invalid records: O(N) time.
- Writing migrated records: O(N) time.
- Storing records in memory: O(N) space.

Batch processing groups records and reduces the number of individual write operations. The configured batch size is three for this demonstration.

## 9. Result

The Java program executed successfully.

| Measurement | Actual result |
|---|---:|
| Total input records | 7 |
| Valid records | 4 |
| Migrated records | 4 |
| Failed records | 3 |
| Batch size | 3 |
| Batches processed | 2 |

## 10. Conclusion

The practical successfully demonstrated file-based data migration using Java. Valid records were separated, processed in batches and written to an output file. Invalid records were recorded in a separate failure report.

This simulation demonstrates the migration workflow and validation concepts. Actual database transactions, JDBC batch insertion and DatabaseMetaData operations were not executed.
