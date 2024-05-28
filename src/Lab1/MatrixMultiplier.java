package Lab1;

public class MatrixMultiplier {
    private final Double[][] leftMatrix;
    private final Double[][] rightMatrix;
    private final Double[][] result;
    private final String logMessage;
    private final int numThreads;

    public MatrixMultiplier(Double[][] leftMatrix, Double[][] rightMatrix, String logMessage, int numThreads) {
        this.leftMatrix = leftMatrix;
        this.rightMatrix = rightMatrix;
        this.numThreads = numThreads;
        this.logMessage = logMessage;
        this.result = new Double[leftMatrix.length][rightMatrix[0].length];
    }

    public Double[][] getResult() {
        return result;
    }

    public void runParallelComputation() throws InterruptedException {
        Thread[] threads = new Thread[numThreads];

        int rowsPerThread = leftMatrix.length / numThreads;

        for (int i = 0; i < numThreads; i++) {
            final int start = i * rowsPerThread;
            final int end = (i == numThreads - 1) ? leftMatrix.length : start + rowsPerThread;
            threads[i] = new Thread(() -> multiplyMatrixRows(start, end));
            threads[i].start();
        }

        for (Thread thread : threads) {
            thread.join();
        }

        log();
    }

    private void multiplyMatrixRows(int start, int end) {
        for (int row = start; row < end; row++) {
            for (int column = 0; column < rightMatrix[0].length; column++) {
                double sum = 0.0;
                for (int element = 0; element < leftMatrix[row].length; element++) {
                    sum += leftMatrix[row][element] * rightMatrix[element][column];
                }
                result[row][column] = sum;
            }
        }
    }

    private void log() {
        Printer.printMatrix(result, logMessage);
    }
}
