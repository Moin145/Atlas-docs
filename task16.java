// Abstract Parent Class
abstract class Person {
    private String name;
    private int age;

    // Constructor
    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

    // Getters and Setters
    public String getName() { return name; }
    public int getAge() { return age; }
    public void setName(String name) { this.name = name; }
    public void setAge(int age) { this.age = age; }

    // Abstract Method
    public abstract String toString();
}

// Child class of Person
class Employee extends Person {
    private String empId;
    private String department;

    public Employee(String name, int age, String empId, String department) {
        super(name, age);
        this.empId = empId;
        this.department = department;
    }

    public String getEmpId() { return empId; }
    public String getDepartment() { return department; }
    public void setEmpId(String empId) { this.empId = empId; }
    public void setDepartment(String department) { this.department = department; }

    public String toString() {
        return "Name: " + getName() + ", Age: " + getAge() +
                ", Emp ID: " + empId + ", Department: " + department;
    }
}

// Sub-child class of Employee
class Manager extends Employee {
    private String team;
    private int experience;

    public Manager(String name, int age, String empId, String department, String team, int experience) {
        super(name, age, empId, department);
        this.team = team;
        this.experience = experience;
    }

    public String getTeam() { return team; }
    public int getExperience() { return experience; }
    public void setTeam(String team) { this.team = team; }
    public void setExperience(int experience) { this.experience = experience; }

    public String toString() {
        return super.toString() +
                ", Team: " + team + ", Experience: " + experience + " years";
    }
}

// Driver class
public class task16 {
    public static void main(String[] args) {
        Manager manager = new Manager("Ravi", 35, "E123", "IT", "Backend Team", 10);
        System.out.println(manager.toString());
    }
}