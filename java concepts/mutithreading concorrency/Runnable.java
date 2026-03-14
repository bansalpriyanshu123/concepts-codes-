public class CleanupTask implements Runnable {
    private String directoryPath;

    public CleanupTask(String directoryPath) {
        this.directoryPath = directoryPath;
    }

    @Override
    public void run() {
        try {
            // Simulating deleting files
            System.out.println("Cleaning up files in " + directoryPath);
            Thread.sleep(1000); 
        } catch (InterruptedException e) {
            // MUST be caught here. Cannot add 'throws InterruptedException' to method signature.
            System.err.println("Cleanup interrupted!");
        }
    }
}

// Execution:
// Thread t = new Thread(new CleanupTask("/tmp/logs"));
// t.start();