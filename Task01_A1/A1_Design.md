
# TASK A1 – TRANSPORT BUS PASS FILE FORMAT

## 1. Problem Description

The Transport Department needs a file-based system to store and manage student bus pass details.

The system must support three operations:

1. Add a new bus pass.
2. Find a bus pass using its Pass ID.
3. List all available bus passes.

The system supports both delimited text file storage and binary file storage.

## 2. Text File Record Layout

Each record is stored as one line in a text file.

Fields:
- Pass ID
- Register Number
- Student Name
- Route Number
- Boarding Stop
- Validity Date
- Amount

Example:

P1001|192371024|Abi|7|Tambaram|2027-03-31|3800.00
P1002|192371025|Arun|8|Guindy|2027-03-31|3800.00

### Handling Delimiters in Student Names

The pipe symbol (|) is used as the field delimiter.

If a student's name contains the delimiter, it must be escaped.

Example:

Original name: Abi|Kumar
Stored name: Abi\|Kumar

Backslash characters must also be escaped to avoid ambiguity.

During reading, the program must decode escaped characters before displaying the original name.

## 3. Binary File Record Layout

Binary records are stored using DataOutputStream.

Field order:

1. Pass ID – writeUTF()
2. Register Number – writeUTF()
3. Student Name – writeUTF()
4. Route Number – writeInt()
5. Boarding Stop – writeUTF()
6. Validity Date – writeUTF()
7. Amount – writeDouble()

The corresponding input stream uses DataInputStream methods in exactly the same order.

Advantages:
- Stores typed values directly.
- Avoids manual delimiter parsing.
- Suitable for structured records.

Limitation:
- The file is not directly human-readable.
- Reading must follow the same field order and data types.

Note: writeUTF() uses Java modified UTF-8 encoding and has a maximum encoded length of 65,535 bytes per string.

## 4. Pseudocode

### Algorithm 1: ADD PASS

BEGIN
    OPEN file in append mode
    GET bus pass details
    VALIDATE all required fields

    ENCODE delimiter and escape characters in text fields
    CONVERT details into a delimited record

    WRITE record to file
    FLUSH output stream
    CLOSE file

    DISPLAY "Bus pass added successfully"
END

### Algorithm 2: FIND PASS

BEGIN
    GET required Pass ID

    IF file does not exist
        DISPLAY "No bus pass records found"
        RETURN
    END IF

    OPEN file using BufferedReader

    WHILE another line exists
        READ one line

        IF line is damaged or has an invalid field count
            SKIP the line
            CONTINUE
        END IF

        DECODE escaped fields

        IF Pass ID matches required Pass ID
            DISPLAY matching bus pass
            CLOSE file
            RETURN
        END IF
    END WHILE

    CLOSE file
    DISPLAY "Bus pass not found"
END

### Algorithm 3: LIST PASSES

BEGIN
    IF file does not exist
        DISPLAY "No records available"
        RETURN
    END IF

    OPEN file using BufferedReader

    WHILE another line exists
        READ one line

        IF line is valid
            DECODE fields
            DISPLAY bus pass details
        ELSE
            DISPLAY damaged record warning
        END IF
    END WHILE

    CLOSE file
END

## 5. Error Handling

| Error condition | Handling |
|---|---|
| Missing file | Report no records or create the file during add |
| Damaged record | Skip the invalid record and log its line number |
| Missing fields | Validate the number of fields |
| Invalid amount | Catch NumberFormatException |
| Incomplete write | Validate records when reading; detect an incomplete final record |
| Permission denied | Catch IOException and display an error |

### Preventing Incomplete Writes

A safer approach is to write updated data to a temporary file, flush and close it, and then replace the original file using Files.move() with ATOMIC_MOVE where supported.

For append-only storage, an incomplete final line should be detected and rejected rather than treated as a complete record.

## 6. Alternative: RandomAccessFile

RandomAccessFile supports reading and writing at specific file positions.

A fixed-length record can be designed using fixed-size fields.

Example:
- Pass ID: 10 bytes
- Register Number: 15 bytes
- Student Name: 30 bytes
- Route Number: 4 bytes
- Boarding Stop: 30 bytes
- Validity Date: 10 bytes
- Amount: 8 bytes

String fields are padded to their allocated lengths.

The record position is calculated as:

Record Position = Record Number × Record Size

The program can use seek() to move directly to the required record position.

### Comparison

| Feature | Sequential File | RandomAccessFile |
|---|---|---|
| Record structure | Variable length | Fixed length |
| Searching | O(n) | O(n) without an index |
| Access by record number | Sequential scan | O(1) using seek() |
| Storage | Flexible | Requires padding |
| Updating | Often rewrites or appends | Can update at a known position |

Important: Searching by Pass ID is not automatically O(1). An index mapping Pass ID to record position is needed for direct lookup.

## 7. Conclusion

Delimited text files are easy to read and maintain, while binary files store structured data efficiently. RandomAccessFile provides direct access when record positions are known. Proper validation, exception handling and incomplete-write detection are essential for reliable bus pass management.
