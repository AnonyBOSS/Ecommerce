package DAO;

import entities.Customer;
import entities.Database;

public class DAOCustomer implements CRUD<Customer>{
    
    @Override
    public void create(Customer customer) {
        Database.customers.add(customer);
    }
    
    @Override
    public Customer read(Customer customer) {
        for (Customer c : Database.customers) {
            if (c.getUserName().equals(customer.getUserName())) {
                System.out.println("Customer found: " + c);
                return c;
            }
        }
        throw new IllegalArgumentException("Customer not found.");
    }

    @Override
    public void update(Customer customer) {
        for (Customer c : Database.customers) {
            if (c.getUserName().equals(customer.getUserName())) {
                try {
                    c.setPassword(customer.getPassword());
                    c.setDateOfBirth(customer.getDateOfBirth());
                    c.setBalance(customer.getBalance());
                    c.setAddress(customer.getAddress());
                    c.setGender(customer.getGender());
                    c.setInterests(customer.getInterests());
                    c.setCart((customer.getCart()));
                    System.out.println("Customer details updated: " + c);
                }catch (IllegalArgumentException e){
                    System.out.println(e.getMessage());
                }
                return;
            }
        }
        System.out.println("Customer not found for update.");
    }

    @Override
    public void delete(Customer customer) {
        if (Database.customers.removeIf(c -> c.getUserName().equals(customer.getUserName()))) {
            System.out.println("Customer deleted: " + customer);
        } else {
            System.out.println( "Customer with username " + customer.getUserName() + " not found.");
        }
    }
}
