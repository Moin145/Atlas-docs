public class task3d20 {

    // Book: Holds book data
    static class Book {
        private String title;
        private String author;
        private double price;

        public Book(String title, String author, double price) {
            this.title = title;
            this.author = author;
            this.price = price;
        }

        public String getTitle() {
            return title;
        }

        public String getAuthor() {
            return author;
        }

        public double getPrice() {
            return price;
        }
    }

    // BookFormatter: Handles display logic
    static class BookFormatter {
        public String getFormattedTitle(Book book) {
            return "Title: " + book.getTitle().toUpperCase();
        }

        public String getFormattedDetails(Book book) {
            return "Book Details → Title: " + book.getTitle()
                    + ", Author: " + book.getAuthor()
                    + ", Price: ₹" + book.getPrice();
        }
    }

    // BookDiscountCalculator: Handles business logic
    static class BookDiscountCalculator {
        public double calculateDiscountedPrice(Book book, double discountPercentage) {
            return book.getPrice() * (1 - discountPercentage / 100);
        }
    }

    // Main method: Execution starts here
    public static void main(String[] args) {
        Book book = new Book("The Alchemist", "Paulo Coelho", 499.00);

        BookFormatter formatter = new BookFormatter();
        BookDiscountCalculator calculator = new BookDiscountCalculator();

        System.out.println(formatter.getFormattedTitle(book));
        System.out.println(formatter.getFormattedDetails(book));

        double discount = 15; // 15% discount
        double discountedPrice = calculator.calculateDiscountedPrice(book, discount);
        System.out.println("Discounted Price: ₹" + discountedPrice);
    }
}
