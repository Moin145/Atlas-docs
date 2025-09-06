

public class NoMobile implements Mobile {
    @Override
    public void getDesc() {
        System.out.println("Sorry, invalid brand or model.");
    }
}
