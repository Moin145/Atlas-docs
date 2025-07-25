import java.util.Arrays;
import java.util.List;

public class streamdistinct {
    public static void main(String[] args) {
        List<Integer> numbers = Arrays.asList(2, 4, 2, 5, 6, 4, 7, 5);

        numbers.stream()
                .distinct()
                .forEach(System.out::println);
    }
}