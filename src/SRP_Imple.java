import java.io.FileWriter;
import java.io.IOException;


public class SRP_Imple {
    public static void main(String[] args) {
        Customer cobj = new Customer("Prasunamba", "C001");
        ManagingFiles mobj = new ManagingFiles();
        mobj.saveData(cobj);
    }
}

class Customer {
    private String name;
    private String custID;

    public Customer(String name, String custID) {
        this.name = name;
        this.custID = custID;
    }

    public String getName() {
        return name;
    }

    public String getCustID() {
        return custID;
    }
}

class ManagingFiles {
    public void saveData(Customer customer) {
        try {
            FileWriter fw = new FileWriter(customer.getName() + ".txt");
            fw.write("The customer name is " + customer.getName() + "\t");
            fw.write("The customer ID is " + customer.getCustID() + "\t");
            fw.close();
            System.out.println("The data is saved in the file with your name.");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
