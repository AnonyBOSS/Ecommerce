import entities.Admin;
import entities.Customer;
import entities.User;
import Service.UserManager;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        try {
            Admin admin = new Admin("admin1", "adminPass123", "2000-01-01", "Manager", 22);

            UserManager.registerUser(admin);  // Register entities.Admin
            UserManager.registerUser(new Customer("john_doe", "password123", "1990-05-10", "123 Main St", Customer.Gender.MALE, List.of("Electronics", "Gaming")));

            User loggedInAdmin = UserManager.loginUser("admin1", "adminPass123");
            System.out.println("Logged in as Admin: " + loggedInAdmin);

            User loggedInCustomer = UserManager.loginUser("john_doe", "password123");
            System.out.println("Logged in as Customer: " + loggedInCustomer);
            Admin.showAllProducts();
            Admin.showAllCategories();
            UserManager.showAllAdmins();
            UserManager.showAllCustomers();

        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}