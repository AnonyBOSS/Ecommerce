package Service;

import DAO.DAOAdmin;
import DAO.DAOCustomer;
import entities.Admin;
import entities.Customer;
import entities.Database;
import entities.User;

public class UserManager {
    private static final DAOCustomer daoCustomer = new DAOCustomer();
    private static final DAOAdmin daoAdmin = new DAOAdmin();
    public static void registerUser(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null.");
        }

        for (User existingUser : Database.customers) {
            if (existingUser.getUserName().equals(user.getUserName())) {
                throw new IllegalArgumentException("Username already exists.");
            }
        }
        for (User existingUser : Database.admins) {
            if (existingUser.getUserName().equals(user.getUserName())) {
                throw new IllegalArgumentException("Username already exists.");
            }
        }

        if (user instanceof Admin) {
            daoAdmin.create((Admin) user);
        } else if (user instanceof Customer) {
            daoCustomer.create((Customer) user);
        }

        System.out.println("User registered successfully: " + user.getUserName());
    }

    public static User loginUser(String username, String password) {
        for (User user : Database.customers) {
            if (user.getUserName().equals(username) && user.getPassword().equals(password)) {
                System.out.println("Login successful for customer: " + user.getUserName());
                return user;
            }
        }

        for (User user : Database.admins) {
            if (user.getUserName().equals(username) && user.getPassword().equals(password)) {
                System.out.println("Login successful for admin: " + user);
                return user;
            }
        }
        throw new IllegalArgumentException("Invalid username or password.");
    }
    public static void showAllAdmins() {
        System.out.println("List of all admins:");
        for (Admin admin : Database.admins) {
            System.out.println(admin);
        }
    }


    public static void showAllCustomers() {
        System.out.println("List of all customers:");
        for (Customer customer : Database.customers) {
            System.out.println(customer);
        }
    }
}
