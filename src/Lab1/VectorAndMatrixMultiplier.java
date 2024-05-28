package Lab1;

public class VectorAndMatrixMultiplier {
    private final Double[] _vector;
    private final Double[][] _matrix;
    private final Double[] _result;
    private final String _logMessage;
    private final int _numThreads;

    public VectorAndMatrixMultiplier(Double[] vector, Double[][] matrix, String logMessage, int numThreads) {
        this._vector = vector;
        this._matrix = matrix;
        this._numThreads = numThreads;
        this._logMessage = logMessage;
        this._result = new Double[matrix[0].length];
    }

    public Double[] getResult() throws InterruptedException {
        return _result;
    }

    public void runParallelComputation() throws InterruptedException {
        Thread[] threads = new Thread[_numThreads];

        for (int i = 0; i < _numThreads; i++) {
            final int start = i * (_matrix[0].length / _numThreads);
            final int end = (i == _numThreads - 1)
                    ? _matrix[0].length
                    : start + (_matrix[0].length / _numThreads);
            threads[i] = new Thread(() -> {
                for (int col = start; col < end; col++) {
                    double sum = 0.0;
                    for (int row = 0; row < _matrix.length; row++) {
                        sum += _vector[row] * _matrix[row][col];
                    }
                    _result[col] = sum;
                }
            });

            threads[i].start();
        }

        for (int i = 0; i < _numThreads; i++) {
            threads[i].join();
        }

        log();
    }

    private void log() {
        Printer.printVector(_result, _logMessage);
    }
}

