# B2 – SQL Injection and Data Validation

## 1. Objective

To understand SQL injection vulnerabilities and implement validation for damaged bus pass records.

## 2. SQL Injection – Concept

SQL injection occurs when an application directly combines user input with an SQL query.

- Unsafe approach: Building database queries by concatenating user input.
- Safe approach: Using parameterized queries with PreparedStatement.
- Parameterized queries treat user input as data instead of executable SQL.

**Note:** SQL execution and database connectivity were not implemented in this task.

## 3. Bus Pass File Validation

Implemented using `BusPassValidator.java`.

Validation checks:
1. Exactly five fields must be present.
2. Empty lines are rejected.
3. Required fields must not be empty.
4. Route number must be a valid positive integer.
5. Amount must be a valid, non-negative number.

## 4. Test Results

| Test case | Expected result | Actual result |
|---|---|---|
| Valid record P1001 | Valid | Passed |
| Incorrect field count | Invalid | Passed |
| Non-numeric amount | Invalid | Passed |
| Empty line | Invalid | Passed |
| Valid record P1004 | Valid | Passed |

## 5. Execution Output

- Total records: 5
- Valid records: 2
- Invalid records: 3

## 6. Conclusion

The Java validation program successfully identifies valid and damaged bus pass records. Parameterized queries are the recommended approach for preventing SQL injection in database applications.