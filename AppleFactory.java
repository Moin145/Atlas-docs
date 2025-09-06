

public class AppleFactory {
    public static Mobile getMobile(String model) {
        if ("iphone16".equalsIgnoreCase(model)) {
            return new AppleMobile("Here is your iPhone 16");
        }
        if ("iphone16MaxPro".equalsIgnoreCase(model)) {
            return new AppleMobile("Here is your iPhone 16 Max Pro");
        }
        return new NoMobile();
    }
}
