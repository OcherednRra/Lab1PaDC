package Lab1;

public class VectorMultiplier {
    private final Double[] _left;
    private final Double[][] _right;
    private final Double[] _result;

    private final String _logMessage;
    private final int _numThreads;

    public VectorMultiplier(Double[] leftMatrix, Double[][] rightMatrix, String logMessage, int numThreads) {
        this._left = leftMatrix;
        this._right = rightMatrix;
        this._numThreads = numThreads;
        this._logMessage = logMessage;
        this._result = new Double[leftMatrix.length];
    }

    public Double[] getResult() throws InterruptedException {
        return _result;
    }

    public void runParallelComputation() throws InterruptedException {
        Thread[] threads = new Thread[_numThreads];

        for (int i = 0; i < _numThreads; i++) {
            final int start = i * (_left.length / _numThreads);
            final int end = (i == _numThreads - 1)
                    ? _left.length
                    : start + (_left.length / _numThreads);
            threads[i] = new Thread(() -> {
                for (int column = start; column < end; column++) {
                    double sum = 0.0;
                    for (int element = 0; element < _left.length; element++) {
                        sum += _left[element] * _right[element][column];
                    }
                    _result[column] = sum;
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
