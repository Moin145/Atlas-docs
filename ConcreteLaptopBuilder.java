public class ConcreteLaptopBuilder implements LaptopBuilder {
    private Laptop laptop = new Laptop();

    @Override
    public void buildCPU() {
        laptop.setCPU("Intel Core i9 14th Gen");
    }

    @Override
    public void buildRAM() {
        laptop.setRAM("32GB DDR5");
    }

    @Override
    public void buildStorage() {
        laptop.setStorage("2TB SSD");
    }

    @Override
    public Laptop getLaptop() {
        return laptop;
    }
}
