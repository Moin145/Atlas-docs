public class OnePlusFactory {
    public static Mobile getMobile(String model) {
        if ("oneplus12".equalsIgnoreCase(model)) {
            return new OnePlusMobile("Here is your OnePlus 12");
        }
        if ("oneplus12Pro".equalsIgnoreCase(model)) {
            return new OnePlusMobile("Here is your OnePlus 12 Pro");
        }
        return new NoMobile();
    }
}
