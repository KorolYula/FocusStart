package ru.korol;

public class ShapeFactory {
    public Shape createShape(String shapeParameter) {
        String[] parameters = shapeParameter.split(" ");

        try {
            return switch (parameters[0]) {
                case "CIRCLE" -> new Circle(Double.parseDouble(parameters[1]));
                case "RECTANGLE" -> new Rectangle(Double.parseDouble(parameters[1]), Double.parseDouble(parameters[2]));
                case "TRIANGLE" -> new Triangle(Double.parseDouble(parameters[1]), Double.parseDouble(parameters[2]), Double.parseDouble(parameters[3]));
                default -> throw new IllegalArgumentException("Invalid shape value " + parameters[0]);
            };

        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid shape parameters");
        } catch (IndexOutOfBoundsException e) {
            throw new IllegalArgumentException("Wrong number of shape parameters ");
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(String.valueOf(e));
        }
    }
}