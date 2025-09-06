public class ClientAbstractFactory {
    public static void main(String[] args) {
        Mobile m1 = MobileStore.getMobile("Apple", "iphone16");
        m1.getDesc();

        Mobile m2 = MobileStore.getMobile("Samsung", "s23Ultra");
        m2.getDesc();

        Mobile m3 = MobileStore.getMobile("OnePlus", "oneplus12Pro");
        m3.getDesc();

        Mobile m4 = MobileStore.getMobile("Nokia", "Lumia"); // Invalid
        m4.getDesc();
    }
}
