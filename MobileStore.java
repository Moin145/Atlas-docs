

public class MobileStore {
    private MobileStore() {} // Prevent instantiation

    public static Mobile getMobile(String brand, String model) {
        if ("Apple".equalsIgnoreCase(brand)) {
            System.out.println("Apple Models:");
            return AppleFactory.getMobile(model);
        }
        if ("Samsung".equalsIgnoreCase(brand)) {
            System.out.println("Samsung Models:");
            return SamsungFactory.getMobile(model);
        }
        if ("OnePlus".equalsIgnoreCase(brand)) {
            System.out.println("OnePlus Models:");
            return OnePlusFactory.getMobile(model);
        }
        return new NoMobile();
    }
}
