package ru.korol;

import java.io.PrintWriter;
import java.util.Scanner;

public class Main {
    static final int MAX = 32;
    static final String SYMBOL1 = "-";
    static final String SYMBOL2 = "+";

    public static int getTableSize(int max) {
        Scanner scanner = new Scanner(System.in);

        String s;
        int size;

        do {
            System.out.print("Введите размер таблицы умножения,это целое число от 1 до " + max + " : ");
            try {
                s = scanner.nextLine();
                size = Integer.parseInt(s);

                if (size > 0 && size <= max) {
                    break;
                }

                if (size <= 0) {
                    System.out.println("Размер таблицы умножения должен быть больше 0!");
                }

                if (size > max) {
                    System.out.println("Размер таблицы умножения должен быть меньше " + max);
                }

            } catch (Exception e) {
                System.out.println("Вы ввели не число!");

            }

        } while (true);

        return size;
    }

    public static void firstRowPrint(int size, int firstCellLength, String cellFormat, PrintWriter writer) {
        writer.print(" ".repeat(firstCellLength));

        for (int i = 1; i <= size; i++) {
            writer.printf(cellFormat, i);
        }

        writer.println();
    }

    public static void tablePrint(int size, StringBuilder cellsBorderRow, String cellFormat, String firstCellFormat, PrintWriter writer) {
        writer.println(cellsBorderRow);

        for (int i = 1; i <= size; i++) {
            writer.printf(firstCellFormat, i);

            for (int j = 1; j <= size; j++) {
                writer.printf(cellFormat, i * j);
            }

            writer.println();
            writer.println(cellsBorderRow);
        }
    }

    public static void main(String[] args) {
        int size = getTableSize(MAX);

        int firstCellLength = (int) (Math.log10(size)) + 1;
        int cellLength = (int) (Math.log10(size * size)) + 1;
        int rowLength = (cellLength + 1) * size + firstCellLength;

        String cellFormat = "|%" + cellLength + "d";
        String firstCellFormat = "%" + firstCellLength + "d";
        String repeated = SYMBOL2 + SYMBOL1.repeat(cellLength);
        StringBuilder cellsBorderRow = new StringBuilder(rowLength);
        cellsBorderRow.append(SYMBOL1.repeat(firstCellLength)).append(repeated.repeat(size));

        PrintWriter writer = new PrintWriter(System.out);
        firstRowPrint(size, firstCellLength, cellFormat, writer);
        tablePrint(size, cellsBorderRow, cellFormat, firstCellFormat, writer);
        writer.flush();
        writer.close();
    }
}