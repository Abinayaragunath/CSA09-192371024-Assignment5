# A2 – Buffered File Reading and Performance Analysis

## 1. Problem Description

Consider an attendance log file of approximately 50 MB. The program must count the number of lines in the file.

Reading the file one byte at a time using FileInputStream is inefficient because every read operation creates an additional method call.

## 2. Unbuffered File Reading

Example:

```java
FileInputStream fis = new FileInputStream("attendance.txt");

int data;
long lineCount = 0;

while ((data = fis.read()) != -1) {
    if (data == '\n') {
        lineCount++;
    }
}

fis.close();
```

For a 50 MB file, approximately 50 million individual byte-read operations may be required.

This increases method-call overhead and reduces performance.

## 3. Buffered File Reading

A BufferedInputStream improves performance by reading a block of bytes into memory instead of requesting every byte separately.

Example:

```java
BufferedInputStream bis =
    new BufferedInputStream(
        new FileInputStream("attendance.txt"), 8192);

int data;
long lineCount = 0;

while ((data = bis.read()) != -1) {
    if (data == '\n') {
        lineCount++;
    }
}

bis.close();
```

The buffer size is 8192 bytes (8 KB).

The program still processes each byte, but the underlying file is accessed in larger blocks.

## 4. When Buffering May Not Improve Performance

Buffering may provide little improvement when:

- The file is very small.
- The data is already available in memory.
- The application already uses another buffered stream.
- Processing logic, rather than file access, is the main bottleneck.

## 5. Files.readAllLines() Alternative

Java also provides:

```java
List<String> lines =
    Files.readAllLines(Path.of("attendance.txt"));

int lineCount = lines.size();
```

Advantages:
- Simple to implement.
- Convenient for small files.
- Reads all lines into a list.

Disadvantages:
- Loads the complete file into memory.
- Memory consumption increases with file size.
- Not suitable for very large files when memory is limited.

## 6. Complexity Comparison

| Method | Time Complexity | Extra Memory |
|---|---|---|
| FileInputStream | O(n) | O(1) |
| BufferedInputStream | O(n) | O(B) |
| Files.readAllLines() | O(n) | O(n) |

Here, n represents the file size and B represents the buffer size.

## 7. Conclusion

BufferedInputStream is suitable for processing large attendance log files because it reduces the number of underlying file read operations. Files.readAllLines() is convenient for small files but may consume significant memory for large files.