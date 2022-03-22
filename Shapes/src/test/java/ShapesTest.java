import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.korol.Circle;
import ru.korol.Rectangle;
import ru.korol.Triangle;

public class ShapesTest {
    @Test
    public void createCircleWithError() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> new Circle(-5));
    }

    @Test
    public void getCircleArea() {
        Circle circle = new Circle(5);
        double result = Math.round(circle.getArea() * 100) / 100.00;
        Assertions.assertEquals(78.54, result);
    }

    @Test
    public void getCirclePerimeter() {
        Circle circle = new Circle(5);
        double result = Math.round(circle.getPerimeter() * 100) / 100.00;
        Assertions.assertEquals(31.42, result);
    }

    @Test
    public void getCircleRadius() {
        Circle circle = new Circle(5);
        double result = circle.getRadius() ;
        Assertions.assertEquals(5, result);
    }

    @Test
    public void getCircleDiameter() {
        Circle circle = new Circle(5);
        double result = circle.getDiameter() ;
        Assertions.assertEquals(10, result);
    }

    @Test
    public void createRectangleWithError() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> new Rectangle(2,-3));
    }

    @Test
    public void getRectangleArea() {
        Rectangle rectangle = new Rectangle(2,5);
        double result = rectangle.getArea() ;
        Assertions.assertEquals(10, result);
    }

    @Test
    public void getRectanglePerimeter() {
        Rectangle rectangle = new Rectangle(2,5);
        double result = rectangle.getPerimeter() ;
        Assertions.assertEquals(14, result);
    }

    @Test
    public void getRectangleDiagonal() {
        Rectangle rectangle = new Rectangle(2,5);
        double result = Math.round(rectangle.getDiagonal() * 100) / 100.00 ;
        Assertions.assertEquals(5.39, result);
    }

    @Test
    public void getRectangleLength() {
        Rectangle rectangle = new Rectangle(2,5);
        double result = rectangle.getLength() ;
        Assertions.assertEquals(5, result);
    }

    @Test
    public void getRectangleWidth() {
        Rectangle rectangle = new Rectangle(2,5);
        double result = rectangle.getWidth() ;
        Assertions.assertEquals(2, result);
    }

    @Test
    public void createTriangleWithError() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> new Triangle(3, 4, 1));
    }

    @Test
    public void createTriangle() {
        Triangle triangle = new Triangle(3, 4, 5);
        double result= triangle.getAngle(0)+ triangle.getAngle(1)+ triangle.getAngle(2);
        Assertions.assertEquals(180, result);
    }

    @Test
    public void getTriangleArea() {
        Triangle triangle = new Triangle(3, 4, 5);
        double result = Math.round(triangle.getArea() * 100) / 100.00;
        Assertions.assertEquals(77.77, result);
    }

    @Test
    public void getTrianglePerimeter() {
        Triangle triangle = new Triangle(3, 4, 5);
        double result = triangle.getPerimeter();
        Assertions.assertEquals(12, result);
    }
}

