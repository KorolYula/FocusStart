package ru.korol;

public class Rectangle extends Shape {
    private double side1;
    private double side2;

    public Rectangle(double side1, double side2) {
        super("Прямоугольник");

        if (side1 <= 0 || side2 <= 0) {
            throw new IllegalArgumentException("The sides of the rectangle must be> 0");
        }

        this.side1 = side1;
        this.side2 = side2;
    }

    public double getLength() {
        return Math.max(side1, side2);
    }

    public double getWidth() {
        return Math.min(side1, side2);
    }

    public double getDiagonal() {
        return Math.sqrt(side1 * side1 + side2 * side2);
    }

    @Override
    public double getArea() {
        return side1 * side2;
    }

    @Override
    public double getPerimeter() {
        return (side1 + side2) * 2;
    }

    @Override
    public String toString() {
        return super.toString() + String.format("Длина диагонали: %.2f ", getDiagonal()) + UNITS + "\n" + String.format("Длина: %.2f ", getLength()) + UNITS + "\n" + String.format("Ширина: %.2f ", getWidth()) + UNITS;
    }
}