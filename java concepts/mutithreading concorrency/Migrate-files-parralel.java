import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class MigrationManager {

    // 1. Define the Callable Task
    static class FileMigrationTask implements Callable<String> {
        private String fileId;

        public FileMigrationTask(String fileId) {
            this.fileId = fileId;
        }

        @Override
        public String call() throws Exception {
            // Simulate processing
            if (fileId.equals("file_500")) {
                throw new Exception("File corrupted in source cloud");
            }
            return fileId + " migrated successfully"; // The success result
        }
    }

    public static void main(String[] args) {
        // Use a Thread Pool suited for I/O bound tasks
        ExecutorService executor = Executors.newFixedThreadPool(50);
        
        // We need a list to hold all our "receipts" (Futures)
        List<Future<String>> futures = new ArrayList<>();

        // 2. Submit 1,000 tasks
        for (int i = 1; i <= 1000; i++) {
            Callable<String> task = new FileMigrationTask("file_" + i);
            Future<String> future = executor.submit(task);
            futures.add(future); // Store the Future to check it later
        }

        int successCount = 0;
        int failureCount = 0;

        // 3. Process the Results and Handle Errors
        for (Future<String> future : futures) {
            try {
                // This blocks until this specific task is done.
                // If the task threw an error, .get() throws an ExecutionException here!
                String result = future.get(); 
                
                successCount++;
                // System.out.println(result); 
                
            } catch (ExecutionException e) {
                // THIS is where you catch the failures for your summary report!
                failureCount++;
                // e.getCause() unwraps the actual exception thrown inside the Callable
                System.err.println("Migration failed. Reason: " + e.getCause().getMessage());
                
            } catch (InterruptedException e) {
                // Thrown if the main thread is interrupted while waiting
                System.err.println("Main thread was interrupted.");
                Thread.currentThread().interrupt(); 
            }
        }

        // 4. Always shut down the executor
        executor.shutdown();

        System.out.println("\n--- Migration Summary Report ---");
        System.out.println("Successful: " + successCount);
        System.out.println("Failures: " + failureCount);
    }
}