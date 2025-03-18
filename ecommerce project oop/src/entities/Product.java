package entities;

import DAO.DAOProduct;
import exceptions.InvalidCategoryException;
import exceptions.InvalidProductException;

public class Product{
    private int id;
    private String name;
    private String description;
    private double price;
    private int quantity;
    private Category category;
    private static final DAOProduct daoProduct = new DAOProduct();
    private int generateProductId() {
        return Database.products.size() + 1;
    }
    public Product(String name, String description, double price, int quantity, Category category)
            throws InvalidProductException, InvalidCategoryException {
        id=generateProductId();
        setName(name.trim());
        setDescription(description);
        setPrice(price);
        setQuantity(quantity);
        setCategory(category);
        daoProduct.create(this);
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) throws InvalidProductException {
        if (id <= 0) {
            throw new InvalidProductException("Product ID must be greater than 0.");
        }
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) throws InvalidProductException {
        if (name == null || name.trim().isEmpty()) {
            throw new InvalidProductException("Product name cannot be null or empty.");
        }
        this.name = name.trim();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? "" : description.trim();
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) throws InvalidProductException {
        if (price < 0) {
            throw new InvalidProductException("Product price cannot be negative.");
        }
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) throws InvalidProductException {
        if (quantity < 0) {
            throw new InvalidProductException("Product quantity cannot be negative.");
        }
        this.quantity = quantity;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) throws InvalidCategoryException {
        if (category == null) {
            throw new InvalidCategoryException("Product must belong to a valid category.");
        }
        this.category = category;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", quantity=" + quantity +
                ", category=" + category.getName() +
                '}';
    }
}

