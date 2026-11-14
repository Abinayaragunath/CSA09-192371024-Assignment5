# A5 – Transaction Management and SQL Injection Prevention

## 1. Problem Description

Consider a student transferring ₹500 from their wallet to the college canteen.

Two operations are required:

1. Deduct ₹500 from the student's wallet.
2. Add ₹500 to the canteen account.

Both operations must succeed together. If one operation fails, the other must also be cancelled.

This is handled using a database transaction.

## 2. What is a Transaction?

A transaction is a group of database operations treated as one logical unit.

Transactions follow ACID properties:

| Property | Meaning |
|---|---|
| Atomicity | All operations succeed or none succeed |
| Consistency | Data remains valid before and after the transaction |
| Isolation | Concurrent transactions do not interfere incorrectly |
| Durability | Committed changes remain stored |

## 3. Commit and Rollback

- `commit()` permanently saves the transaction changes.
- `rollback()` cancels the uncommitted changes.

## 4. Wallet Transfer Pseudocode

```text
START TRANSACTION

TRY:
    Check student wallet balance

    IF balance >= 500:
        Deduct 500 from student wallet
        Add 500 to canteen account

        COMMIT TRANSACTION
        DISPLAY "Transfer successful"

    ELSE:
        ROLLBACK TRANSACTION
        DISPLAY "Insufficient balance"

CATCH ERROR:
    ROLLBACK TRANSACTION
    DISPLAY "Transfer failed"
```

The transaction ensures that the wallet deduction and canteen credit happen together.

## 5. Problem with Separate Auto-Commit Updates

When auto-commit is enabled, each database update is committed separately.

Example:

1. Student wallet is reduced by ₹500.
2. Canteen credit operation fails.
3. The wallet deduction is already permanent.

This creates inconsistent financial records.

Solution: Disable auto-commit, execute both operations in one transaction, then commit or rollback.

## 6. SQL Injection

SQL injection is a security vulnerability caused by inserting untrusted user input directly into a database query.

Unsafe login example:

```java
String sql = "SELECT * FROM users WHERE username = '"
             + username + "' AND password = '"
             + password + "'";
```

An attacker may provide specially crafted input to alter the query's logic and bypass authentication.

## 7. PreparedStatement

PreparedStatement uses parameter placeholders instead of directly combining user input with SQL.

Example:

```java
String sql =
    "SELECT * FROM users WHERE username = ? AND password = ?";

PreparedStatement ps = connection.prepareStatement(sql);

ps.setString(1, username);
ps.setString(2, password);
```

Advantages:

- Prevents user input from being treated as SQL code.
- Reduces SQL injection risk.
- Supports parameterized query execution.
- Improves code readability.

Passwords should also be stored using secure password hashing, not plain text.

## 8. Conclusion

Database transactions maintain consistency by using commit and rollback. PreparedStatement protects database applications against SQL injection by separating query structure from user input.