public class WhiteConcretePrototype implements Colors {
    private String name;

    // Default constructor
    public WhiteConcretePrototype() {
        this.name = "White";
    }

    // Parameterized constructor
    public WhiteConcretePrototype(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    // Setter for name so we can modify cloned objects
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public Colors clone() {
        return new WhiteConcretePrototype(this.name);
    }

    @Override
    public String toString() {
        return "WhiteConcretePrototype { name = '" + name + "' }";
    }
}
