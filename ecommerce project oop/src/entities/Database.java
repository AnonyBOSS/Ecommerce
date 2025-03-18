package entities;

import Service.UserManager;

import java.util.ArrayList;
import java.util.List;

public class Database {
    public static List<Customer> customers = new ArrayList<>();
    public static List<Admin> admins = new ArrayList<>();
    public static List<Category> categories = new ArrayList<>();
    public static List<Product> products = new ArrayList<>();
    public static List<Order> orders = new ArrayList<>();
    public static List<Cart> carts=new ArrayList<>();

    // dummy data
    static {
        try {
            Category electronics = new Category("Electronics");
            Category clothing = new Category("Clothing");
            Category books = new Category("Books");
            Category homeAppliances = new Category("Home Appliances");
            Category toys = new Category("Toys");
            Category furniture = new Category("Furniture");
            Category sportsEquipment = new Category("Sports Equipment");
            Category groceries = new Category("Groceries");
            Category gardening = new Category("Gardening");
            Category automotive = new Category("Automotive");
            Category petSupplies = new Category("Pet Supplies");
            Category officeSupplies = new Category("Office Supplies");

            new Product("Laptop", "High-performance laptop", 1200.0, 10, electronics);
            new Product("Smartphone", "S24 Ultra", 800.0, 20, electronics);
            new Product("Tablet", "Lightweight tablet", 500.0, 15, electronics);
            new Product("Gaming Console", "Play station 5", 600.0, 10, electronics);
            new Product("Headphones", "Noise-cancelling headphones", 150.0, 50, electronics);
            new Product("Refrigerator", "Double-door fridge", 1500.0, 5, homeAppliances);
            new Product("Microwave", "800W microwave oven", 200.0, 15, homeAppliances);
            new Product("Dishwasher", "Energy-efficient dishwasher", 800.0, 3, homeAppliances);
            new Product("T-shirt", "Cotton T-shirt", 25.0, 50, clothing);
            new Product("Jeans", "Denim jeans", 40.0, 30, clothing);
            new Product("Dress", "Elegant evening dress", 120.0, 10, clothing);
            new Product("Winter Jacket", "Waterproof winter jacket", 100.0, 20, clothing);
            new Product("Novel", "Bestselling novel", 15.0, 100, books);
            new Product("Cookbook", "Healthy recipes cookbook", 25.0, 40, books);
            new Product("Science Book", "Informative science book", 50.0, 30, books);
            new Product("Puzzle", "500-piece jigsaw puzzle", 20.0, 25, toys);
            new Product("Action Figure", "Superhero action figure", 30.0, 20, toys);
            new Product("Chess Set", "Classic wooden chess set", 50.0, 10, toys);
            new Product("RC Car", "Remote-controlled car", 80.0, 15, toys);
            new Product("Sofa", "Comfortable three-seater sofa", 700.0, 3, furniture);
            new Product("Dining Table", "Modern dining table", 500.0, 5, furniture);
            new Product("Office Chair", "Ergonomic office chair", 150.0, 20, furniture);
            new Product("Yoga Mat", "Non-slip yoga mat", 30.0, 50, sportsEquipment);
            new Product("Treadmill", "Electric treadmill", 1000.0, 2, sportsEquipment);
            new Product("Basketball", "Professional basketball", 25.0, 30, sportsEquipment);
            new Product("Garden Hose", "Durable garden hose", 40.0, 25, gardening);
            new Product("Lawn Mower", "Electric lawn mower", 200.0, 8, gardening);
            new Product("Car Wax", "Premium car wax", 15.0, 50, automotive);
            new Product("Motor Oil", "Synthetic motor oil", 30.0, 40, automotive);
            new Product("Dog Food", "Premium dog food", 50.0, 60, petSupplies);
            new Product("Cat Toy", "Interactive cat toy", 10.0, 100, petSupplies);
            new Product("Printer Paper", "500 sheets of printer paper", 5.0, 200, officeSupplies);
            new Product("Stapler", "Heavy-duty stapler", 20.0, 40, officeSupplies);
            new Product("Milk", "1-liter organic milk", 2.5, 200, groceries);
            new Product("Bread", "Whole grain bread", 3.0, 150, groceries);
            new Product("Cheese","Parmesan cheese",10.0,50,groceries);

            UserManager.registerUser(new Customer(
                    "ABDOOOOOOOOOOOO",
                    "password123",
                    "2002-09-23",
                    "123 Main St",
                    Customer.Gender.MALE,
                    List.of("Electronics")
            ));
            UserManager.registerUser(new Customer(
                    "SEIIIIIIIIIFOOOOOOOOOOO",
                    "ezzzzzzzz123",
                    "2006-11-23",
                    "456 Elm St",
                    Customer.Gender.MALE,
                    List.of("Clothing")
            ));

            UserManager.registerUser(new Admin(
                    "admin",
                    "adminPass123",
                    "2003-08-23",
                    "Manager",
                    12
            ));
            UserManager.registerUser(new Customer(
                    "JohnDoe",
                    "john12345",
                    "1990-01-15",
                    "789 Pine St",
                    Customer.Gender.MALE,
                    List.of("Books", "Electronics")
            ));
            UserManager.registerUser(new Customer(
                    "JaneSmith",
                    "jane12345",
                    "1985-05-20",
                    "321 Oak St",
                    Customer.Gender.FEMALE,
                    List.of("Clothing", "Furniture")
            ));
            UserManager.registerUser(new Customer(
                    "AliceGreen",
                    "alice12345",
                    "1995-07-10",
                    "654 Maple St",
                    Customer.Gender.FEMALE,
                    List.of("Toys", "Home Appliances")
            ));
            UserManager.registerUser(new Customer(
                    "BobBrown",
                    "bob12345",
                    "1980-11-02",
                    "987 Cedar St",
                    Customer.Gender.MALE,
                    List.of("Automotive", "Sports Equipment")
            ));
            UserManager.registerUser(new Customer(
                    "EmilyWhite",
                    "emily12345",
                    "2000-04-25",
                    "123 Willow St",
                    Customer.Gender.FEMALE,
                    List.of("Groceries", "Pet Supplies")
            ));
            UserManager.registerUser(new Customer(
                    "ChrisBlack",
                    "chris12345",
                    "1992-03-18",
                    "456 Birch St",
                    Customer.Gender.MALE,
                    List.of("Office Supplies", "Gardening")
            ));
            UserManager.registerUser(new Customer(
                    "SophiaGray",
                    "sophia12345",
                    "1997-08-12",
                    "789 Aspen St",
                    Customer.Gender.FEMALE,
                    List.of("Electronics", "Clothing", "Books")
            ));
            UserManager.registerUser(new Customer(
                    "DanielBlue",
                    "daniel12345",
                    "1999-12-30",
                    "1010 Spruce St",
                    Customer.Gender.MALE,
                    List.of("Sports Equipment", "Furniture")
            ));

        } catch (Exception e) {
            System.err.println("Error initializing dummy data: " + e.getMessage());
        }
    }

}