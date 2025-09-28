package com.bankingsystem.controller;

import com.bankingsystem.model.Customer;
import com.bankingsystem.repository.CustomerRepository;
import com.bankingsystem.service.AuditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/customers")
@CrossOrigin(origins = "*")
public class CustomerController {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private AuditService auditService;

    /**
     * List all customers
     */
    @GetMapping
    public ResponseEntity<?> listCustomers() {
        List<Customer> customers = customerRepository.findAll();
        return ResponseEntity.ok(Map.of(
                "success", true,
                "customers", customers
        ));
    }

    /**
     * Get a customer by id
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getCustomer(@PathVariable String id) {
        return customerRepository.findById(id)
                .<ResponseEntity<?>>map(customer -> ResponseEntity.ok(Map.of(
                        "success", true,
                        "customer", customer
                )))
                .orElseGet(() -> ResponseEntity.status(404).body(Map.of(
                        "success", false,
                        "message", "Customer not found"
                )));
    }

    /**
     * Create a new customer
     */
    @PostMapping
    public ResponseEntity<?> createCustomer(@Valid @RequestBody CreateCustomerRequest request) {
        try {
            Customer customer = new Customer(
                    request.getFirstName(),
                    request.getLastName(),
                    request.getEmail(),
                    request.getMobileNumber(),
                    request.getAddress(),
                    request.getCity(),
                    request.getState(),
                    request.getPincode()
            );

            customer = customerRepository.save(customer);

            // audit
            auditService.logSuccess(request.getUserId(), "CREATE_CUSTOMER", "CUSTOMER", customer.getId(),
                    "Created customer " + customer.getFullName());

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Customer created successfully",
                    "customer", customer
            ));
        } catch (Exception e) {
            auditService.logFailure(request.getUserId(), "CREATE_CUSTOMER", "CUSTOMER", null,
                    "Failed to create customer", e.getMessage());
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", e.getMessage()
            ));
        }
    }

    // Request DTO
    public static class CreateCustomerRequest {
        @NotBlank
        private String firstName;
        @NotBlank
        private String lastName;
        @NotBlank
        private String email;
        @NotBlank
        private String mobileNumber;
        @NotBlank
        private String address;
        private String city;
        private String state;
        private String pincode;
        private String userId;

        public String getFirstName() { return firstName; }
        public void setFirstName(String firstName) { this.firstName = firstName; }
        public String getLastName() { return lastName; }
        public void setLastName(String lastName) { this.lastName = lastName; }
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getMobileNumber() { return mobileNumber; }
        public void setMobileNumber(String mobileNumber) { this.mobileNumber = mobileNumber; }
        public String getAddress() { return address; }
        public void setAddress(String address) { this.address = address; }
        public String getCity() { return city; }
        public void setCity(String city) { this.city = city; }
        public String getState() { return state; }
        public void setState(String state) { this.state = state; }
        public String getPincode() { return pincode; }
        public void setPincode(String pincode) { this.pincode = pincode; }
        public String getUserId() { return userId; }
        public void setUserId(String userId) { this.userId = userId; }
    }
}


