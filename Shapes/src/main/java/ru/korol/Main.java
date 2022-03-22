package ru.korol;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

public class Main {
    private static final Logger log = LoggerFactory.getLogger(Main.class.getName());

    public static String getStringFromFile(String inputFile) throws IOException {
        StringBuilder result = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile))) {
            result.append(reader.readLine()).append(" ").append(reader.readLine());
        }

        return result.toString();
    }

    private static void printShape(String outFile, Shape shape) throws IOException {
        try (PrintWriter printWriter = outFile == null ? new PrintWriter(System.out) : new PrintWriter(outFile)) {
            printWriter.println(shape);
            log.info("Figure is recorded in {}", outFile);
        }
    }

    private static String[] getFilesNamesFromArgs(String[] args) {
        String[] fileNames = new String[2];

        if (args.length == 0) {
            log.error("No input file specified");
        }

        fileNames[1] = null;
        fileNames[0] = args[0];
        log.info("Input file name {} ", fileNames[0]);

        if (args.length > 1) {
            fileNames[1] = args[1];
            log.info("The shape will be written to file {}", fileNames[1]);
        }
        return fileNames;
    }

    public static void main(String[] args) {
        String[] fileNames = getFilesNamesFromArgs(args);

        try {
            String shapeParameters = getStringFromFile(fileNames[0]);

            ShapeFactory shapeFactory = new ShapeFactory();
            Shape shape = shapeFactory.createShape(shapeParameters);
            log.info("Shape read from file");
            printShape(fileNames[1], shape);

        } catch (IllegalArgumentException | FileNotFoundException e) {
            log.error(String.valueOf(e));
        } catch (IOException e) {
            log.error("Error reading file {} ", fileNames[0]);
        }
    }
}