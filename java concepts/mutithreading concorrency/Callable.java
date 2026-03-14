import java.util.concurrent.Callable;

public class MigrationTask implements Callable<Integer> {
    private String fileId;

    public MigrationTask(String fileId) {
        this.fileId = fileId;
    }

    @Override
    public Integer call() throws Exception { // Can throw checked exceptions!
        System.out.println("Migrating file: " + fileId);
        
        // Simulating a network failure for a specific file
        if (fileId.equals("file_13")) {
            throw new IOException("AWS S3 Bucket timed out");
        }
        
        // Simulating successful migration returning the bytes transferred
        return 2048; 
    }
}

// Execution (Requires ExecutorService, you cannot pass a Callable to a raw Thread):
// ExecutorService executor = Executors.newFixedThreadPool(2);
// Future<Integer> result = executor.submit(new MigrationTask("file_99"));
// 
// try {
//     Integer bytesTransferred = result.get(); // Blocks until the thread is done
//     System.out.println("Success! Bytes: " + bytesTransferred);
// } catch (ExecutionException e) {
//     System.err.println("The thread threw an exception: " + e.getCause());
// }