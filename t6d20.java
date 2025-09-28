// File: t6d20.java

interface Shape1 {
    int area();
}

class Circle1 implements Shape1 {
    int r;

    Circle1(int radius) {
        this.r = radius;
    }

    public int area() {
        return (int) (Math.PI * r * r);
    }
}

class Square1 implements Shape1 {
    int height;

    Square1(int height) {
        this.height = height;
    }

    public int area() {
        return height * height;
    }
}

class Book {
    String title;
    String author;
    double price;

    Book(String title, String author, double price) {
        this.title = title;
        this.author = author;
        this.price = price;
    }
}

class BookPrinter {
    void print(Book book) {
        System.out.println("Book: " + book.title + " by " + book.author + ", price: $" + book.price);
    }
}

class PriceCalculator {
    double calculatePrice(Book book) {
        return book.price;
    }
}

public class t6d20 {
    public static void main(String[] args) {
        // Use Circle1 and Square1 here
        Shape1 c = new Circle1(5);
        Shape1 s = new Square1(4);

        System.out.println("Circle area: " + c.area());
        System.out.println("Square area: " + s.area());

        Book book = new Book("Diagram", "Mohd Moin", 29.99);
        BookPrinter printer = new BookPrinter();
        PriceCalculator calculator = new PriceCalculator();

        printer.print(book);
        System.out.println("Price: $" + calculator.calculatePrice(book));
    }
}
