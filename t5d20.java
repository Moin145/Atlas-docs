// Interface for all shapes
interface Shape {
    double area(); // Using double for accuracy
}

// Circle class implements Shape
class Circle implements Shape {
    int radius;

    // Constructor
    Circle(int radius) {
        this.radius = radius;
    }

    // Area of a circle
    @Override
    public double area() {
        return Math.PI * radius * radius;
    }
}

// Square class implements Shape
class Square implements Shape {
    int height;

    // Constructor
    Square(int height) {
        this.height = height;
    }

    // Area of a square
    @Override
    public double area() {
        return height * height;
    }
}

// Class that follows the Open-Closed Principle
public class t5d20 {

    // Method to compare areas of two shapes
    public int compareArea(Shape a, Shape b) {
        return Double.compare(a.area(), b.area());
    }

    // Main method to test the program
    public static void main(String[] args) {
        // Create shape objects
        Shape circle = new Circle(3);
        Shape square = new Square(4);

        // Create OpenClosedExample object
        t5d20 example = new t5d20();

        // Compare their areas
        int result = example.compareArea(circle, square);

        // Output the result
        if (result > 0) {
            System.out.println("Circle has a bigger area.");
        } else if (result < 0) {
            System.out.println("Square has a bigger area.");
        } else {
            System.out.println("Both have equal area.");
        }
    }
}