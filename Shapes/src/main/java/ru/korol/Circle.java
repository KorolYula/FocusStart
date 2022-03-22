package ru.korol;

public class Circle extends Shape {
    private double radius;

    public Circle(double radius) {
        super("Круг");

        if (radius <= 0) {
            throw new IllegalArgumentException("The radius of the circle must be> 0");
        }

        this.radius = radius;
    }

    public double getRadius() {
        return radius;
    }

    public double getDiameter() {
        return radius * 2;
    }

    @Override
    public double getArea() {
        return radius * radius * Math.PI;
    }

    @Override
    public double getPerimeter() {
        return 2 * radius * Math.PI;
    }

    @Override
    public String toString() {
        return super.toString() + String.format("Радиус: %.2f ", getRadius()) + UNITS + "\n" + String.format("Диаметр: %.2f ", getDiameter()) + UNITS;
    }
}