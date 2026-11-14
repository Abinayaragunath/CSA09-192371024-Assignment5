
import java.io.*;
import java.util.Arrays;

public class BusPassValidator {

    public static void validateFile(File file)
            throws IOException {

        int valid = 0;
        int invalid = 0;
        int lineNumber = 0;

        try (BufferedReader reader =
                new BufferedReader(new FileReader(file))) {

            String line;

            while ((line = reader.readLine()) != null) {

                lineNumber++;

                if (line.isBlank()) {
                    System.out.println(
                            "Line " + lineNumber + ": Empty line - INVALID");
                    invalid++;
                    continue;
                }

                String[] fields = line.split(",", -1);

                if (fields.length != 5) {
                    System.out.println(
                            "Line " + lineNumber
                            + ": Incorrect field count - INVALID");
                    invalid++;
                    continue;
                }

                if (fields[0].isBlank()
                        || fields[1].isBlank()
                        || fields[2].isBlank()
                        || fields[3].isBlank()
                        || fields[4].isBlank()) {

                    System.out.println(
                            "Line " + lineNumber
                            + ": Missing field - INVALID");
                    invalid++;
                    continue;
                }

                try {
                    int route = Integer.parseInt(fields[3]);
                    double amount = Double.parseDouble(fields[4]);

                    if (route <= 0 || !Double.isFinite(amount)
                            || amount < 0) {
                        throw new NumberFormatException();
                    }

                } catch (NumberFormatException e) {
                    System.out.println(
                            "Line " + lineNumber
                            + ": Invalid route or amount - INVALID");
                    invalid++;
                    continue;
                }

                System.out.println(
                        "Line " + lineNumber + ": VALID "
                        + Arrays.toString(fields));

                valid++;
            }
        }

        System.out.println("\nVALID RECORDS: " + valid);
        System.out.println("INVALID RECORDS: " + invalid);
    }

    public static void main(String[] args) {

        File file = new File("Output/damaged_passes.csv");

        try {

            file.getParentFile().mkdirs();

            // Create sample test records
            try (BufferedWriter writer =
                    new BufferedWriter(new FileWriter(file))) {

                writer.write("P1001,192311001,Arun,5,3500.0");
                writer.newLine();

                writer.write("P1002,192311002,Damaged");
                writer.newLine();

                writer.write("P1003,192311003,Divya,7,ABC");
                writer.newLine();

                writer.newLine();

                writer.write("P1004,192311004,Kumar,9,4000.0");
                writer.newLine();
            }

            System.out.println("BUS PASS VALIDATION");
            System.out.println("===================");

            validateFile(file);

        } catch (IOException e) {
            System.out.println(
                    "File error: " + e.getMessage());
        }
    }
}
