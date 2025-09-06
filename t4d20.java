public class t4d20 {
    // 1. Employee class (only manages employee data)
    static class Employee {
        private String name;
        private String email;
        private double salary;

        public Employee(String name, String email, double salary) {
            this.name = name;
            this.email = email;
            this.salary = salary;
        }

        public String getName() { return name; }
        public String getEmail() { return email; }
        public double getSalary() { return salary; }
    }

    // 2. Report Generator (only handles PDF reports)
    static class EmployeeReportGenerator {
        public void generatePdfReport(Employee employee) {
            System.out.println("Generating PDF report for: " + employee.getName());
            // Actual PDF generation logic would go here
        }
    }

    // 3. Email Service (only handles sending emails)
    static class EmailService {
        public void sendEmail(Employee employee, String subject, String body) {
            System.out.println("Sending email to: " + employee.getEmail());
            System.out.println("Subject: " + subject);
            System.out.println("Body: " + body);
            // Actual email sending logic would go here
        }
    }

    // Main method to demonstrate usage
    public static void main(String[] args) {
        // Create an employee
        Employee employee = new Employee("Moin Mohd", "Moin@gmail.com", 50000);

        // Generate report
        EmployeeReportGenerator reportGenerator = new EmployeeReportGenerator();
        reportGenerator.generatePdfReport(employee);

        // Send email
        EmailService emailService = new EmailService();
        emailService.sendEmail(
                employee,
                "Monthly Salary Report",
                "Dear " + employee.getName() + ", your salary details are attached."
        );
    }
}