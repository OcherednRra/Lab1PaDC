package Lab1;

public class VectorScalarMultiplier {
    private final Double[] _vector;
    private final Double _scalar;
    private final Double[] _result;

    private final String _logMessage;
    private final int _numThreads;

    public VectorScalarMultiplier(Double[] vector, Double scalar, String logMessage, int numThreads) {
        this._vector = vector;
        this._scalar = scalar;
        this._logMessage = logMessage;
        this._numThreads = numThreads;
        this._result = new Double[vector.length];
    }

    public Double[] getResult() throws InterruptedException {
        return _result;
    }

    public void runParallelComputation() throws InterruptedException {
        Thread[] threads = new Thread[_numThreads];

        for (int i = 0; i < _numThreads; i++) {
            final int start = i * (_vector.length / _numThreads);
            final int end = (i == _numThreads - 1)
                    ? _vector.length
                    : start + (_vector.length / _numThreads);
            threads[i] = new Thread(() -> {
                for (int j = start; j < end; j++) {
                    _result[j] = _vector[j] * _scalar;
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
