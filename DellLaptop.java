// Client — Dell laptop that uses ILaptopTarget to charge
public class DellLaptop {
    private ILaptopTarget powerSocket;

    public DellLaptop(ILaptopTarget powerSocket) {
        this.powerSocket = powerSocket;
    }

    public void chargeLaptop() {
        System.out.println("DellLaptop: Starting to charge.");
        powerSocket.charge();
    }

    public void stopCharging() {
        System.out.println("DellLaptop: Stopping charging.");
        powerSocket.removeCharge();
    }
}
