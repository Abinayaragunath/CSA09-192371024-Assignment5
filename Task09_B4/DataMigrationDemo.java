
import java.io.*;
import java.util.*;

public class DataMigrationDemo {

    private static final int BATCH_SIZE = 3;

    public static void main(String[] args) {

        String inputFile = "Input/buspasses.csv";
        String validFile = "Output/migrated_records.csv";
        String invalidFile = "Output/failed_records.txt";

        List<String> validRecords = new ArrayList<>();
        List<String> invalidRecords = new ArrayList<>();

        System.out.println("BUS PASS DATA MIGRATION");
        System.out.println("=======================");

        File outputFolder = new File("Output");
        outputFolder.mkdirs();

        createInputFile(inputFile);

        // Step 1: Read and validate records
        try (BufferedReader reader =
                     new BufferedReader(new FileReader(inputFile))) {

            String line;
            int lineNumber = 0;

            while ((line = reader.readLine()) != null) {

                lineNumber++;

                if (line.trim().isEmpty()) {
                    invalidRecords.add(
                        "Line " + lineNumber + ": Empty record"
                    );
                    continue;
                }

                String[] fields = line.split(",", -1);

                if (fields.length != 5) {
                    invalidRecords.add(
                        "Line " + lineNumber + ": Incorrect field count"
                    );
                    continue;
                }

                try {
                    int route = Integer.parseInt(fields[3].trim());
                    double amount = Double.parseDouble(fields[4].trim());

                    if (fields[0].trim().isEmpty()
                            || fields[1].trim().isEmpty()
                            || fields[2].trim().isEmpty()
                            || route <= 0
                            || !Double.isFinite(amount)
                            || amount <= 0) {

                        throw new IllegalArgumentException();
                    }

                    validRecords.add(line);

                } catch (Exception e) {
                    invalidRecords.add(
                        "Line " + lineNumber + ": Invalid route or amount"
                    );
                }
            }

        } catch (IOException e) {
            System.out.println("Unable to read input file.");
            return;
        }

        // Step 2: Simulate migration in batches
        int migratedCount = 0;

        try (BufferedWriter writer =
                     new BufferedWriter(new FileWriter(validFile))) {

            for (int i = 0; i < validRecords.size(); i += BATCH_SIZE) {

                int end = Math.min(
                    i + BATCH_SIZE,
                    validRecords.size()
                );

                List<String> batch =
                    validRecords.subList(i, end);

                for (String record : batch) {
                    writer.write(record);
                    writer.newLine();
                }

                writer.flush();

                migratedCount += batch.size();

                System.out.println(
                    "Batch " + ((i / BATCH_SIZE) + 1)
                    + ": " + batch.size() + " records migrated"
                );
            }

        } catch (IOException e) {
            System.out.println("Migration file error: " + e.getMessage());
            return;
        }

        // Step 3: Generate failure report
        try (BufferedWriter writer =
                     new BufferedWriter(new FileWriter(invalidFile))) {

            for (String error : invalidRecords) {
                writer.write(error);
                writer.newLine();
            }

        } catch (IOException e) {
            System.out.println("Unable to create failure report.");
            return;
        }

        // Step 4: Display summary
        System.out.println("\nMIGRATION SUMMARY");
        System.out.println("-----------------");
        System.out.println("Total valid records: " + validRecords.size());
        System.out.println("Total migrated: " + migratedCount);
        System.out.println("Total failed: " + invalidRecords.size());

        System.out.println("\nMigrated file: " + validFile);
        System.out.println("Failure report: " + invalidFile);

        System.out.println("\nMigration simulation completed.");
    }

    private static void createInputFile(String filename) {

        File input = new File(filename);
        input.getParentFile().mkdirs();

        String[] records = {
            "P1001,192311001,Arun,5,3500.0",
            "P1002,192311002,Bala,6,3600.0",
            "P1003,192311003,Divya,7,3800.0",
            "P1004,192311004,Kumar",
            "P1005,192311005,Meena,8,ABC",
            "",
            "P1007,192311007,Ravi,9,4200.0"
        };

        try (BufferedWriter writer =
                     new BufferedWriter(new FileWriter(input))) {

            for (String record : records) {
                writer.write(record);
                writer.newLine();
            }

        } catch (IOException e) {
            System.out.println("Unable to create input file.");
        }
    }
}
