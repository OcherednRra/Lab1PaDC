package Lab1;

public class VectorMaximumFinder {
    private final Double[] _vector;
    private volatile Double _max = Double.MIN_VALUE;

    private final String _logMessage;
    private final int _numThreads;

    public VectorMaximumFinder(Double[] vector, String logMessage, int numThreads) {
        this._vector = vector;
        this._logMessage = logMessage;
        this._numThreads = numThreads;
    }

    public Double getResult() throws InterruptedException {
        return _max;
    }

    public void runParallelComputation() throws InterruptedException {
        Thread[] threads = new Thread[_numThreads];

        for (int i = 0; i < _numThreads; i++) {
            final int start = i * (_vector.length / _numThreads);
            final int end = (i == _numThreads - 1)
                    ? _vector.length
                    : start + (_vector.length / _numThreads);
            threads[i] = new Thread(() -> {
                Double localMax = Double.MIN_VALUE;
                for (int j = start; j < end; j++) {
                    if (_vector[j] > localMax) {
                        localMax = _vector[j];
                    }
                }

                updateMax(localMax);
            });

            threads[i].start();
        }

        for (int i = 0; i < _numThreads; i++) {
            threads[i].join();
        }

        log();
    }

    private synchronized void updateMax(Double localMax) {
        if (localMax > _max)
            _max = localMax;
    }

    private void log() {
        Printer.printScalar(_max, _logMessage);
    }
}
