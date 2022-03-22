package ru.korol;

public abstract class Shape {
    static final String UNITS = "мм";
    String name;

    public Shape(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    abstract double getArea();

    abstract double getPerimeter();

    public String toString() {
        return "Тип фигуры: " + name + "\n" + String.format("Площадь: %.2f кв.", getArea()) + UNITS + "\n" + String.format("Периметр: %.2f " ,getPerimeter())+UNITS + "\n";
    }
}
