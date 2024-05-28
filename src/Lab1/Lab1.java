package Lab1;

import java.io.File;
import java.io.IOException;

public class Lab1 {
    public static final String FILE_PATH_PREFIX = "./src/Data/";

    public static final String MTFile = FILE_PATH_PREFIX + "MT.json";
    public static final String MDFile = FILE_PATH_PREFIX + "MD.json";
    public static final String MCFile = FILE_PATH_PREFIX + "MC.json";
    public static final String MMFile = FILE_PATH_PREFIX + "MM.json";
    public static final String MAFile = FILE_PATH_PREFIX + "MA.json";

    public static final String AFile = FILE_PATH_PREFIX + "A.json";
    public static final String BFile = FILE_PATH_PREFIX + "B.json";
    public static final String CFile = FILE_PATH_PREFIX + "C.json";
    public static final String DFile = FILE_PATH_PREFIX + "D.json";

    public static final String scalarAFile = FILE_PATH_PREFIX + "scalarA.json";

    public static Double[][] MT;
    public static Double[][] MD;
    public static Double[][] MC;
    public static Double[][] MM;
    public static Double[][] MA;

    public static Double[] A;
    public static Double[] B;
    public static Double[] C;
    public static Double[] D;

    public static double a;

    public static final int VECTORS_SIZE = 10;


    public static void main(String[] args) throws InterruptedException, IOException {
        generateData();
        getData();
        long start = System.nanoTime();
        makeComputation();
        long end = System.nanoTime();
        System.out.println("\nTime (ns): \n"+ (end - start));
        System.out.println("Time (s): \n"+ (end - start) / 1_000_000_000.0);
        setResult();
    }

    private static void generateData() throws IOException {
        generateFile(MTFile, DataGenerator.generateSquareMatrix(VECTORS_SIZE));
        generateFile(MDFile, DataGenerator.generateSquareMatrix(VECTORS_SIZE));
        generateFile(MCFile, DataGenerator.generateSquareMatrix(VECTORS_SIZE));
        generateFile(MMFile, DataGenerator.generateSquareMatrix(VECTORS_SIZE));
        generateFile(BFile, DataGenerator.generateVector(VECTORS_SIZE));
        generateFile(CFile, DataGenerator.generateVector(VECTORS_SIZE));
        generateFile(DFile, DataGenerator.generateVector(VECTORS_SIZE));
        generateFile(scalarAFile, DataGenerator.generateScalar());
    }

    private static void generateFile(String filePath, Object data) throws IOException {
        File file = new File(filePath);
        if (!file.exists()) {
            file.createNewFile();
            if (data instanceof Double[][]) {
                Serializer.serialize((Double[][]) data, filePath);
            } else if (data instanceof Double[]) {
                Serializer.serialize((Double[]) data, filePath);
            } else if (data instanceof Double) {
                Serializer.serialize((double) data, filePath);
            } else {
                throw new IllegalArgumentException("Unsupported data type");
            }
        }
    }

    private static void getData() {
        MT = Serializer.deserializeMatrix(MTFile);
        MD = Serializer.deserializeMatrix(MDFile);
        MC = Serializer.deserializeMatrix(MCFile);
        MM = Serializer.deserializeMatrix(MMFile);

        B = Serializer.deserializeVector(BFile);
        C = Serializer.deserializeVector(CFile);
        D = Serializer.deserializeVector(DFile);

        a = Serializer.deserializeScalar(scalarAFile);
    }

    private static void setResult() {
        Serializer.serialize(MA, MAFile);
        Serializer.serialize(A, AFile);
    }

    private static void makeComputation() throws InterruptedException {
        // A = C*MC+D*MM*a-B*MT
        // MA = max(B-D)*MD*MT-MC*(MT+MM)
        VectorAndMatrixMultiplier cMultMc =
                new VectorAndMatrixMultiplier(C, MC, "\nC * MC result:\n", 6);
        VectorAndMatrixMultiplier dMultMm =
                new VectorAndMatrixMultiplier(D, MM, "\nD * MM result:\n", 6);
        VectorAndMatrixMultiplier bMultMt =
                new VectorAndMatrixMultiplier(B, MT, "\nB * MT result:\n", 6);
        VectorSummer bMinusD =
                new VectorSummer(B, D, VectorSummer.SUBTRACTION, "\nB - D result:", 4);
        MatrixSummer MtPlusMm =
                new MatrixSummer(MT, MM, MatrixSummer.SUM, "\nMT + MM result:", 4);

        Thread cMultMcThread = new Thread(() -> {
            try {
                cMultMc.runParallelComputation();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        Thread dMultMmThread = new Thread(() -> {
            try {
                dMultMm.runParallelComputation();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        Thread bMultMtThread = new Thread(() -> {
            try {
                bMultMt.runParallelComputation();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        Thread bMinusDThread = new Thread(() -> {
            try {
                bMinusD.runParallelComputation();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        Thread MtPlusMmThread = new Thread(() -> {
            try {
                MtPlusMm.runParallelComputation();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        cMultMcThread.start();
        dMultMmThread.start();
        bMultMtThread.start();
        bMinusDThread.start();
        MtPlusMmThread.start();

        cMultMcThread.join();
        dMultMmThread.join();
        bMultMtThread.join();
        bMinusDThread.join();
        MtPlusMmThread.join();

        VectorScalarMultiplier dMultMmMulta =
                new VectorScalarMultiplier(dMultMm.getResult(), a, "\nD * MM * a result:\n", 6);
        VectorMaximumFinder maxbMinusd =
                new VectorMaximumFinder(bMinusD.getResult(), "\nmax(B - D) result:", 4);
        MatrixMultiplier MCMultMtPlusMm =
                new MatrixMultiplier(MC, MtPlusMm.getResult(),"\nMC*(MT + MM) result:", 4);

        Thread dMultMmMultaThread = new Thread(() -> {
            try {
                dMultMmMulta.runParallelComputation();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        Thread maxbMinusdThread = new Thread(() -> {
            try {
                maxbMinusd.runParallelComputation();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        Thread MCMultMtPlusMmThread = new Thread(() -> {
            try {
                MCMultMtPlusMm.runParallelComputation();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        dMultMmMultaThread.start();
        maxbMinusdThread.start();
        MCMultMtPlusMmThread.start();

        dMultMmMultaThread.join();
        maxbMinusdThread.join();
        MCMultMtPlusMmThread.join();

        VectorSummer cMultMcPlusdMultMmMulta =
                new VectorSummer(cMultMc.getResult(), dMultMmMulta.getResult(), VectorSummer.SUM,"\nC * MC + D * MM * a result:\n", 6);
        MatrixScalarMultiplier maxbMinusdMultMd =
                new MatrixScalarMultiplier(MD, maxbMinusd.getResult(), "\nmax(B - D)*MD result:", 4);

        Thread cMultMcPlusdMultMmMultaThread = new Thread(() -> {
            try {
                cMultMcPlusdMultMmMulta.runParallelComputation();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        Thread maxbMinusdMultMdThread = new Thread(() -> {
            try {
                maxbMinusdMultMd.runParallelComputation();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        cMultMcPlusdMultMmMultaThread.start();
        maxbMinusdMultMdThread.start();

        cMultMcPlusdMultMmMultaThread.join();
        maxbMinusdMultMdThread.join();

        VectorSummer Aresult =
                new VectorSummer(cMultMcPlusdMultMmMulta.getResult(), bMultMt.getResult(), VectorSummer.SUBTRACTION,"\nA = C * MC + D * MM * a - B * MT:\n", 6);
        MatrixMultiplier maxbMinusdMultMdMultMt =
                new MatrixMultiplier(maxbMinusdMultMd.getResult(), MT, "\nmax(B - D)*MD*MT result:", 4);

        Thread AresultThread = new Thread(() -> {
            try {
                Aresult.runParallelComputation();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        Thread maxbMinusdMultMdMultMtThread = new Thread(() -> {
            try {
                maxbMinusdMultMdMultMt.runParallelComputation();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        AresultThread.start();
        maxbMinusdMultMdMultMtThread.start();

        AresultThread.join();
        maxbMinusdMultMdMultMtThread.join();

        MatrixMultiplier MAresult =
                new MatrixMultiplier(maxbMinusdMultMdMultMt.getResult(), MCMultMtPlusMm.getResult(),"\nMA = max(B-D)*MD*MT-MC*(MT+MM):", 4);

        Thread MAresultThread = new Thread(() -> {
            try {
                MAresult.runParallelComputation();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        MAresultThread.start();
        MAresultThread.join();

        A = Aresult.getResult();
        MA = MAresult.getResult();
    }
}