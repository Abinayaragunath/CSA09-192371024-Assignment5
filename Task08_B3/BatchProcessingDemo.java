
import java.util.ArrayList;
import java.util.List;

class BusPass {
    int passId;
    String studentName;
    double amount;

    BusPass(int passId, String studentName, double amount) {
        this.passId = passId;
        this.studentName = studentName;
        this.amount = amount;
    }

    public String toString() {
        return passId + " | " + studentName + " | Rs." + amount;
    }
}

public class BatchProcessingDemo {

    private static List<BusPass> database = new ArrayList<>();

    public static int executeBatch(List<BusPass> batch) {

        int previousSize = database.size();

        try {
            for (BusPass pass : batch) {

                if (pass.amount <= 0) {
                    throw new IllegalArgumentException(
                        "Invalid amount for " + pass.studentName
                    );
                }

                database.add(pass);
            }

            return database.size() - previousSize;

        } catch (Exception e) {

            // Rollback simulation
            while (database.size() > previousSize) {
                database.remove(database.size() - 1);
            }

            System.out.println("Batch failed: " + e.getMessage());
            System.out.println("Rollback completed.");

            return 0;
        }
    }

    public static void main(String[] args) {

        System.out.println("JDBC BATCH PROCESSING SIMULATION");
        System.out.println("================================");

        List<BusPass> batch1 = new ArrayList<>();

        for (int i = 1; i <= 10; i++) {
            batch1.add(new BusPass(
                1000 + i,
                "Student" + i,
                3500.0 + (i * 100)
            ));
        }

        System.out.println("\nTEST 1: SUCCESSFUL BATCH");

        int inserted = executeBatch(batch1);

        System.out.println("Records inserted: " + inserted);
        System.out.println("Total records: " + database.size());

        System.out.println("\nTEST 2: FAILED BATCH");

        List<BusPass> batch2 = new ArrayList<>();

        batch2.add(new BusPass(1011, "Student11", 4500));
        batch2.add(new BusPass(1012, "Student12", -500));
        batch2.add(new BusPass(1013, "Student13", 4700));

        int failedBatch = executeBatch(batch2);

        System.out.println("Records inserted: " + failedBatch);
        System.out.println("Total records after rollback: " + database.size());

        System.out.println("\nFINAL DATABASE RECORDS");

        for (BusPass pass : database) {
            System.out.println(pass);
        }

        System.out.println("\nSimulation completed.");
    }
}
