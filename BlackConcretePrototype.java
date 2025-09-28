public class BlackConcretePrototype implements Colors {
    private String name;

    // Default constructor
    public BlackConcretePrototype() {
        this.name = "Black";
    }

    // Parameterized constructor
    public BlackConcretePrototype(String name) {
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
        return new BlackConcretePrototype(this.name);
    }

    @Override
    public String toString() {
        return "BlackConcretePrototype { name = '" + name + "' }";
    }
}
