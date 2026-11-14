# B1 – File Handling Implementation and Performance Results

## 1. Implementation

Implemented using Java:
- PassStore.java
- PassBenchmark.java

File operations:
- Add bus pass records.
- Find records using pass ID.
- List valid records.
- Handle missing files.
- Skip damaged records with incorrect field counts.

BufferedReader and BufferedWriter are used for file operations.

## 2. Functional Testing

| Test case | Expected result | Actual result |
|---|---|---|
| Add P1004 | Record added | Passed |
| Find P1003 | Matching record returned | Passed |
| Find P9999 | null | Passed |
| Damaged record | Skipped | Passed |
| Missing file | null | Passed |

## 3. File Reading Performance

Buffer size: 8192 bytes.

| File size | Unbuffered time (ms) | Buffered time (ms) | Line counts match |
|---|---:|---:|---|
| 1 MiB | 2377.618 | 6.495 | Yes |
| 10 MiB | 52631.758 | 49.722 | Yes |
| 50 MiB | 213265.327 | 49.435 | Yes |

## 4. Pass Search Performance

Each result is the average of 10 searches for the last record.

| Number of records | Average search time |
|---:|---:|
| 1,000 | 5.277 ms |
| 10,000 | 11.173 ms |
| 100,000 | 19.842 ms |

## 5. Performance Analysis

Buffered reading is significantly faster in these tests because it reduces underlying file read operations.

Sequential searching takes O(n) time because records are checked one by one.

A HashMap can provide O(1) average lookup after building the map, but requires additional memory.

Benchmark timings can vary because of JVM warm-up, operating system caching and hardware.

## 6. Conclusion

The file store successfully performs add, find and list operations. It handles missing files and skips damaged records. Buffered reading provides a significant performance improvement for large files.