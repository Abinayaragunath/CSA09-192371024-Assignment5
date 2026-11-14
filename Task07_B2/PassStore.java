
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PassStore {

    private final File file;

    public PassStore(String fileName) {
        file = new File(fileName);
    }

    public void add(String[] pass) throws IOException {

        if (pass == null || pass.length != 5) {
            throw new IllegalArgumentException(
                "Exactly 5 fields are required."
            );
        }

        try (BufferedWriter writer =
                new BufferedWriter(new FileWriter(file, true))) {

            writer.write(String.join(",", pass));
            writer.newLine();
        }
    }

    public String[] find(String passId) throws IOException {

        if (!file.exists()) {
            return null;
        }

        try (BufferedReader reader =
                new BufferedReader(new FileReader(file))) {

            String line;

            while ((line = reader.readLine()) != null) {

                if (line.isBlank()) {
                    continue;
                }

                String[] fields = line.split(",", -1);

                if (fields.length != 5) {
                    continue;
                }

                if (fields[0].equals(passId)) {
                    return fields;
                }
            }
        }

        return null;
    }

    public List<String[]> list() throws IOException {

        List<String[]> records = new ArrayList<>();

        if (!file.exists()) {
            return records;
        }

        try (BufferedReader reader =
                new BufferedReader(new FileReader(file))) {

            String line;

            while ((line = reader.readLine()) != null) {

                if (line.isBlank()) {
                    continue;
                }

                String[] fields = line.split(",", -1);

                if (fields.length == 5) {
                    records.add(fields);
                }
            }
        }

        return records;
    }

    public static void main(String[] args) {

        File testFile = new File("Output", "buspasses.csv");

        try {

            testFile.getParentFile().mkdirs();

            // Prepare fresh test records
            try (BufferedWriter writer =
                    new BufferedWriter(new FileWriter(testFile))) {

                writer.write("P1001,192311001,Arun,5,3500.0");
                writer.newLine();

                writer.write("P1002,192311002,Damaged");
                writer.newLine();

                writer.write("P1003,192311003,Divya,7,3800.0");
                writer.newLine();
            }

            PassStore store = new PassStore(testFile.getPath());

            System.out.println("BUS PASS FILE STORE");
            System.out.println("===================");

            // TEST 1: ADD
            System.out.println("\nTEST 1 - ADD PASS:");

            store.add(new String[]{
                "P1004", "192311004", "Kumar", "9", "4000.0"
            });

            System.out.println("P1004 added successfully.");

            // TEST 2: FIND
            System.out.println("\nTEST 2 - FIND PASS:");

            System.out.println(
                Arrays.toString(store.find("P1003"))
            );

            // TEST 3: MISSING PASS
            System.out.println("\nTEST 3 - MISSING PASS:");

            System.out.println(store.find("P9999"));

            // TEST 4: LIST
            System.out.println("\nTEST 4 - LIST VALID PASSES:");

            for (String[] pass : store.list()) {
                System.out.println(Arrays.toString(pass));
            }

            // TEST 5: MISSING FILE
            System.out.println("\nTEST 5 - MISSING FILE:");

            PassStore missingStore =
                new PassStore("Output/nonexistent.csv");

            System.out.println(missingStore.find("P9999"));

        } catch (IOException e) {
            System.out.println("File error: " + e.getMessage());
        }
    }
}
