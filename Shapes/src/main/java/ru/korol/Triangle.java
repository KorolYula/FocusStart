package ru.korol;

import java.util.Arrays;

public class Triangle extends Shape {
    private final double[] side = new double[3];
    private final double[] angle = new double[3];


    public double getAngle(int i) {
        return angle[i];
    }


    private static double calculateAngle(double x, double y, double z) {
        return Math.acos((x * x + y * y - z * z) / (2 * x * y)) * 180 / Math.PI;
    }

    public Triangle(double side1, double side2, double side3) {
        super("Треугольник");

        if (side1 <= 0 || side2 <= 0 || side3 <= 0) {
            throw new IllegalArgumentException("The sides of the triangle must be> 0");
        }

        this.side[0] = side1;
        this.side[1] = side2;
        this.side[2] = side3;

        Arrays.sort(side);

        if (side[2] >= side[0] + side[1]) {
            throw new IllegalArgumentException("Invalid triangle side lengths");
        }

        this.angle[0] = calculateAngle(side[1], side[2], side[0]);
        this.angle[1] = calculateAngle(side[0], side[2], side[1]);
        this.angle[2] = calculateAngle(side[0], side[1], side[2]);
    }

    @Override
    public double getArea() {
        double semiPerimeter = getPerimeter();
        return Math.sqrt(semiPerimeter * (semiPerimeter - side[0]) * (semiPerimeter - side[1]) * (semiPerimeter - side[2]));
    }

    @Override
    public double getPerimeter() {
        return Arrays.stream(side).sum();
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append(super.toString());

        for (int i = 0; i < 3; i++) {
            result.append(String.format("Длина %d-й стороны : %.2f ", i + 1, side[i]))
                    .append(UNITS)
                    .append("\n")
                    .append(String.format("Противолежащий %d-й стороне угол: %.2f градусов", i + 1, angle[i]))
                    .append("\n");
        }

        return result.toString();
    }
}