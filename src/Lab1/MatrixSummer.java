package Lab1;

class MatrixSummer {
    public static final boolean SUM = true;
    public static final boolean SUBTRACTION = false;
    private final Double[][] leftMatrix;
    private final Double[][] rightMatrix;
    private final Double[][] result;

    private final String logMessage;
    private final int numThreads;
    private final boolean isSum;

    public MatrixSummer(Double[][] leftMatrix, Double[][] rightMatrix, boolean action, String logMessage, int numThreads) {
        this.leftMatrix = leftMatrix;
        this.rightMatrix = rightMatrix;
        this.isSum = action;
        this.logMessage = logMessage;
        this.numThreads = numThreads;
        this.result = new Double[leftMatrix.length][leftMatrix[0].length];
    }

    public Double[][] getResult() {
        return result;
    }

    public void runParallelComputation() throws InterruptedException {
        Thread[] threads = new Thread[numThreads];

        int rowsPerThread = leftMatrix.length / numThreads;

        for (int i = 0; i < numThreads; i++) {
            final int startRow = i * rowsPerThread;
            final int endRow = (i == numThreads - 1) ? leftMatrix.length : startRow + rowsPerThread;
            threads[i] = new Thread(() -> computeOperations(startRow, endRow));
            threads[i].start();
        }

        for (Thread thread : threads) {
            thread.join();
        }

        log();
    }

    private void computeOperations(int startRow, int endRow) {
        for (int row = startRow; row < endRow; row++) {
            for (int column = 0; column < leftMatrix[row].length; column++) {
                performOperation(row, column);
            }
        }
    }

    private void performOperation(int row, int column) {
        Double leftOperand = leftMatrix[row][column];
        Double rightOperand = isSum ? rightMatrix[row][column] : -rightMatrix[row][column];
        result[row][column] = leftOperand + rightOperand;
    }

    private void log() {
        Printer.printMatrix(result, logMessage);
    }
}
