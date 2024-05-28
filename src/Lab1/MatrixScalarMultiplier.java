package Lab1;

public class MatrixScalarMultiplier {
    private final Double scalar;
    private final Double[][] matrix;
    private final Double[][] result;
    private final String logMessage;
    private final int numThreads;

    public MatrixScalarMultiplier(Double[][] matrix, Double scalar, String logMessage, int numThreads) {
        this.scalar = scalar;
        this.matrix = matrix;
        this.numThreads = numThreads;
        this.logMessage = logMessage;
        this.result = new Double[matrix.length][matrix[0].length];
    }

    public Double[][] getResult() {
        return result;
    }

    public void runParallelComputation() throws InterruptedException {
        Thread[] threads = new Thread[numThreads];

        int rowsPerThread = matrix.length / numThreads;

        for (int i = 0; i < numThreads; i++) {
            final int start = i * rowsPerThread;
            final int end = (i == numThreads - 1) ? matrix.length : start + rowsPerThread;
            threads[i] = new Thread(() -> multiplyMatrixRowsByScalar(start, end));
            threads[i].start();
        }

        for (Thread thread : threads) {
            thread.join();
        }

        log();
    }

    private void multiplyMatrixRowsByScalar(int start, int end) {
        for (int row = start; row < end; row++) {
            for (int column = 0; column < matrix[row].length; column++) {
                result[row][column] = matrix[row][column] * scalar;
            }
        }
    }

    private void log() {
        Printer.printMatrix(result, logMessage);
    }
}
