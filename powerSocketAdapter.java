
public class powerSocketAdapter implements ILaptopTarget {
    private IChargerAdaptee charger; // reference to adaptee

    public powerSocketAdapter(IChargerAdaptee charger) {
        this.charger = charger;
    }

    @Override
    public void charge() {
        System.out.println("Adapter: Translating charge() to plugIn()");
        charger.plugIn();
    }

    @Override
    public void removeCharge() {
        System.out.println("Adapter: Translating removeCharge() to unplug()");
        charger.unplug();
    }
}
