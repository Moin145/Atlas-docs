// Concrete Adaptee — an actual charger
public class ChargerAdaptee implements IChargerAdaptee {
    @Override
    public void plugIn() {
        System.out.println("Charger: Plugged in and charging.");
    }

    @Override
    public void unplug() {
        System.out.println("Charger: Unplugged and stopped charging.");
    }
}
