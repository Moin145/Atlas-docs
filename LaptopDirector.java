public class LaptopDirector {
    private LaptopBuilder builder;

    public LaptopDirector(LaptopBuilder builder) {
        this.builder = builder;
    }

    public void constructLaptop() {
        builder.buildCPU();
        builder.buildRAM();
        builder.buildStorage();
    }
}
