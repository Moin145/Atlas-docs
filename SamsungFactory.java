public class SamsungFactory {
    public static Mobile getMobile(String model) {
        if ("s23".equalsIgnoreCase(model)) {
            return new SamsungMobile("Here is your Samsung S23");
        }
        if ("s23Ultra".equalsIgnoreCase(model)) {
            return new SamsungMobile("Here is your Samsung S23 Ultra");
        }
        return new NoMobile();
    }
}
