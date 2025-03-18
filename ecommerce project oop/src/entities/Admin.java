package entities;

import DAO.*;

public class Admin extends User {
    private String role;
    private int workingHours;
    private final static DAOOrder daoOrder = new DAOOrder();
    private final static DAOProduct daoProduct = new DAOProduct();
    private final static DAOCategory daoCategory = new DAOCategory();
    // Constructor
    public Admin(String userName, String password, String dateOfBirth, String role, int workingHours) {
        super(userName, password, dateOfBirth);
        setRole(role);
        setWorkingHours(workingHours);
    }

    // Getter and Setter
    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public int getWorkingHours() {
        return workingHours;
    }

    public void setWorkingHours(int workingHours) {
        if (workingHours < 0) {
            throw new IllegalArgumentException("Working hours cannot be negative");
        }
        else if(workingHours>24){
            throw new IllegalArgumentException("Working hours cannot exceed 24/day");
        }
        else {
            this.workingHours = workingHours;
        }
    }


    @Override
    public String toString() {
        return "Admin{" + "UserName=" + getUserName() + ", Role=" + role + ", WorkingHours=" + workingHours + '}';
    }

    // CRUD methods for managing categories
    public static void addCategory(Category category) {
        daoCategory.create(category);
    }

    public static Category getCategory(Category category) {
        return daoCategory.read(category);
    }

    public static void updateCategory(Category updatedCategory) {
        daoCategory.update(updatedCategory);
    }

    public static void deleteCategory(Category category) {
        daoCategory.delete(category);
    }
    public static void showAllCategories() {
        System.out.println("List of all categories:");
        for (Category category : Database.categories) {
            System.out.println(category.toString());
        }
    }

    //CRUD for products
    public static void addProduct(Product product) {
        daoProduct.create(product);
    }

    public static Product getProduct(Product product) {
        return daoProduct.read(product);
    }

    public static void updateProduct(Product updatedProduct) {
       daoProduct.update(updatedProduct);
    }

    public static void deleteProduct(Product product) {
        daoProduct.delete(product);
    }

    public static void showAllProducts() {
        System.out.println("List of all products:");
        for (Product product : Database.products) {
            System.out.println(product.toString());
        }
    }
    public static void addOrder(Order order){
        daoOrder.create(order);
    }

    public static Order getOrder(Order order) {
        return daoOrder.read(order);
    }

}