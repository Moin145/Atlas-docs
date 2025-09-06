import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;

public class InsertCustomerData {
    public static void main(String[] args) {
        try {
            AmazonDynamoDBClient client = new AmazonDynamoDBClient()
                    .withEndpoint("http://localhost:8000"); // Connect to local DynamoDB
            DynamoDB dynamoDB = new DynamoDB(client);

            String tableName = "Customer";
            Table table = dynamoDB.getTable(tableName);

            // Create an item to add
            Item item = new Item()
                    .withPrimaryKey("CustomerId", "2")
                    .withString("Name", "Moin Mohd")
                    .withString("Email", "Moinmohd@example.com");

            table.putItem(item);
            System.out.println("Item inserted successfully into " + tableName);
        } catch (Exception e) {
            System.err.println("Failed to insert item: " + e.getMessage());
        }
    }
}
