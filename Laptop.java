public class Laptop {
    private String CPU;
    private String RAM;
    private String storage;

    // Getters
    public String getCPU() {
        return CPU;
    }

    public String getRAM() {
        return RAM;
    }

    public String getStorage() {
        return storage;
    }

    // Setters
    public void setCPU(String CPU) {
        this.CPU = CPU;
    }

    public void setRAM(String RAM) {
        this.RAM = RAM;
    }

    public void setStorage(String storage) {
        this.storage = storage;
    }

    @Override
    public String toString() {
        return "Laptop [CPU=" + CPU + ", RAM=" + RAM + ", Storage=" + storage + "]";
    }
}
