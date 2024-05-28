package Lab1;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Serializer {
    public static void serialize(Double[][] matrix, String path) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(matrix);

        try (FileWriter writer = new FileWriter(path)) {
            writer.write(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void serialize(Double[] vector, String path) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(vector);

        try (FileWriter writer = new FileWriter(path)) {
            writer.write(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void serialize(double scalar, String path) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(scalar);

        try (FileWriter writer = new FileWriter(path)) {
            writer.write(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Double[][] deserializeMatrix(String path) {
        Gson gson = new GsonBuilder().create();
        Double[][] matrix = null;

        try (FileReader reader = new FileReader(path)) {
            matrix = gson.fromJson(reader, Double[][].class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return matrix;
    }

    public static Double[] deserializeVector(String path) {
        Gson gson = new GsonBuilder().create();
        Double[] vector = null;

        try (FileReader reader = new FileReader(path)) {
            vector = gson.fromJson(reader, Double[].class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return vector;
    }

    public static Double deserializeScalar(String path) {
        Gson gson = new GsonBuilder().create();
        Double scalar = null;

        try (FileReader reader = new FileReader(path)) {
            scalar = gson.fromJson(reader, Double.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return scalar;
    }
}
