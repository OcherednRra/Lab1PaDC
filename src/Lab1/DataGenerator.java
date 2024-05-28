package Lab1;

import java.text.DecimalFormat;
import java.util.Random;

public class DataGenerator {
    public static Double[][] generateSquareMatrix(int size) {
        Double[][] result = new Double[size][size];
        Random generator = new Random();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                result[i][j] = nextDouble(generator);
            }
        }
        return result;
    }

    public static Double generateScalar() {
        Random generator = new Random();
        return nextDouble(generator);
    }

    public static Double[] generateVector(int size) {
        Double[] result = new Double[size];
        Random generator = new Random();
        for (int i = 0; i < size; i++) {
            result[i] = nextDouble(generator);
        }
        return result;
    }

    private static Double nextDouble(Random generator) {
        DecimalFormat precisionFormat = new DecimalFormat("#." + "#".repeat(generator.nextInt(1, 3)));
        Double tempRandom = generator.nextDouble() * 200 - 100;

        return Double.parseDouble(precisionFormat.format(tempRandom).replace(",", "."));
    }
}
