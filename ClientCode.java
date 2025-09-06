public class ClientCode {
    public static void main(String[] args) {
        // Creating original prototypes
        WhiteConcretePrototype white1 = new WhiteConcretePrototype("Winter White");
        BlackConcretePrototype black1 = new BlackConcretePrototype("Midnight Black");

        // Cloning
        WhiteConcretePrototype white2 = (WhiteConcretePrototype) white1.clone();
        BlackConcretePrototype black2 = (BlackConcretePrototype) black1.clone();


        System.out.println("Before modification:");
        System.out.println("Original White: " + white1);
        System.out.println("Clone White:    " + white2);
        System.out.println("Original Black: " + black1);
        System.out.println("Clone Black:    " + black2);

        // Modifying only the clones
        white2.setName("Spring White");
        black2.setName("Ebony Black");


        System.out.println("\nAfter modification:");
        System.out.println("Original White: " + white1);
        System.out.println("Clone White:    " + white2);
        System.out.println("Original Black: " + black1);
        System.out.println("Clone Black:    " + black2);
    }
}
