
public class ClientAdapter {
    public static void main(String[] args) {
        IChargerAdaptee charger = new ChargerAdaptee();


        ILaptopTarget socketAdapter = new powerSocketAdapter(charger);


        DellLaptop laptop = new DellLaptop(socketAdapter);


        laptop.chargeLaptop();
        laptop.stopCharging();
    }
}
