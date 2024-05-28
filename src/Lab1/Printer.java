package Lab1;

import java.text.DecimalFormat;

public class Printer {
    public synchronized static void printMatrix(Double[][] matrix, String logMessage) {
        DecimalFormat df = new DecimalFormat("#.###");

        System.out.println(logMessage);
        for (Double[] doubles : matrix) {
            for (Double aDouble : doubles) {
                String formattedNumber = df.format(aDouble).replace(',', '.');
                System.out.print(formattedNumber + " ");
            }
            System.out.println();
        }
    }

    public synchronized static void printVector(Double[] vector, String logMessage) {
        DecimalFormat df = new DecimalFormat("#.###");

        System.out.println(logMessage);
        for (Double aDouble : vector) {
            String formattedNumber = df.format(aDouble).replace(',', '.');
            System.out.print(formattedNumber + " ");
        }
        System.out.println();
    }

    public static synchronized void printScalar(Double scalar, String logMessage) {
        System.out.println(logMessage);
        System.out.println(scalar);
    }
}
