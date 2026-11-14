
import java.io.*;
import java.nio.charset.StandardCharsets;

public class PassBenchmark {

    private static final int BUFFER_SIZE = 8192;

    // Generate a test file of the requested size in MiB
    private static void generateFile(File file, int sizeMB)
            throws IOException {

        byte[] data = "Attendance record: PRESENT\n"
                .getBytes(StandardCharsets.UTF_8);

        long targetSize = sizeMB * 1024L * 1024L;

        try (BufferedOutputStream out =
                new BufferedOutputStream(new FileOutputStream(file))) {

            long written = 0;

            while (written + data.length <= targetSize) {
                out.write(data);
                written += data.length;
            }

            int remaining = (int) (targetSize - written);

            if (remaining > 0) {
                out.write(data, 0, remaining);
            }
        }
    }

    // Count newline characters using one-byte reads
    private static long countUnbuffered(File file)
            throws IOException {

        long lines = 0;

        try (FileInputStream in = new FileInputStream(file)) {

            int value;

            while ((value = in.read()) != -1) {
                if (value == '\n') {
                    lines++;
                }
            }
        }

        return lines;
    }

    // Count newline characters using buffered block reads
    private static long countBuffered(File file)
            throws IOException {

        long lines = 0;
        byte[] buffer = new byte[BUFFER_SIZE];

        try (BufferedInputStream in =
                new BufferedInputStream(
                        new FileInputStream(file), BUFFER_SIZE)) {

            int bytesRead;

            while ((bytesRead = in.read(buffer)) != -1) {

                for (int i = 0; i < bytesRead; i++) {
                    if (buffer[i] == '\n') {
                        lines++;
                    }
                }
            }
        }

        return lines;
    }

    private static void benchmarkFiles(File folder)
            throws IOException {

        System.out.println("\nFILE READING PERFORMANCE");
        System.out.println("-------------------------------------------");

        for (int size : new int[]{1, 10, 50}) {

            File file = new File(folder, "attendance_" + size + "MB.txt");

            generateFile(file, size);

            long start = System.nanoTime();
            long unbufferedLines = countUnbuffered(file);
            long unbufferedTime = System.nanoTime() - start;

            start = System.nanoTime();
            long bufferedLines = countBuffered(file);
            long bufferedTime = System.nanoTime() - start;

            System.out.println("\nFile size: " + size + " MiB");
            System.out.println("Unbuffered lines: " + unbufferedLines);
            System.out.printf("Unbuffered time: %.3f ms%n",
                    unbufferedTime / 1_000_000.0);

            System.out.println("Buffered lines: " + bufferedLines);
            System.out.printf("Buffered time: %.3f ms%n",
                    bufferedTime / 1_000_000.0);

            System.out.println("Line counts match: "
                    + (unbufferedLines == bufferedLines));
        }
    }

    // Generate a bus pass dataset
    private static void generatePasses(File file, int count)
            throws IOException {

        try (BufferedWriter writer =
                new BufferedWriter(new FileWriter(file))) {

            for (int i = 1; i <= count; i++) {

                writer.write(
                    String.format(
                        "P%06d,1923%06d,Student%d,7,3800.0",
                        i, i, i
                    )
                );

                writer.newLine();
            }
        }
    }

    private static void benchmarkSearch(File folder)
            throws IOException {

        System.out.println("\nPASS SEARCH PERFORMANCE");
        System.out.println("-------------------------------------------");

        for (int count : new int[]{1000, 10000, 100000}) {

            File file = new File(folder, "search_" + count + ".csv");

            generatePasses(file, count);

            PassStore store = new PassStore(file.getPath());

            // Search for the last record to measure a full scan
            String target = String.format("P%06d", count);

            // Warm-up search
            store.find(target);

            int repetitions = 10;
            long start = System.nanoTime();

            for (int i = 0; i < repetitions; i++) {
                store.find(target);
            }

            long elapsed = System.nanoTime() - start;

            System.out.println("\nRecords: " + count);
            System.out.println("Target: " + target);
            System.out.printf("Average search time: %.3f ms%n",
                    elapsed / repetitions / 1_000_000.0);
        }
    }

    public static void main(String[] args) {

        File folder = new File("Output");
        folder.mkdirs();

        try {
            benchmarkFiles(folder);
            benchmarkSearch(folder);

            System.out.println("\nBenchmark completed successfully.");

        } catch (IOException e) {
            System.out.println("Benchmark error: " + e.getMessage());
        }
    }
}
