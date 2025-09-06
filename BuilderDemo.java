public class BuilderDemo {
    public static void main(String[] args) {
        LaptopBuilder builder = new ConcreteLaptopBuilder();
        LaptopDirector director = new LaptopDirector(builder);

        director.constructLaptop();
        Laptop gamingLaptop = builder.getLaptop();

        System.out.println("Built Laptop:");
        System.out.println("CPU: " + gamingLaptop.getCPU());
        System.out.println("RAM: " + gamingLaptop.getRAM());
        System.out.println("Storage: " + gamingLaptop.getStorage());
    }
}
