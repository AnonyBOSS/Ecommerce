package DAO;
import entities.Database;
import entities.Product;
import exceptions.InvalidCategoryException;
import exceptions.InvalidProductException;

public class DAOProduct implements CRUD<Product> {
    @Override
    public void create(Product product) {
        if (product == null) {
            throw new IllegalArgumentException("Product cannot be null.");
        }
        Database.products.add(product);
        System.out.println("Product added successfully");
    }
    @Override
    public Product read(Product product) {
        for (Product p : Database.products) {
            if (p.getId()==product.getId() || p.getName().equals(product.getName())) {
                return p;
            }
        }
        System.out.println( "Product with ID " + product.getId() + " not found.");
        throw new IllegalArgumentException("Product not found.");
    }
    @Override
    public void update(Product product) {
        for (Product p : Database.products) {
            if (p.getId() == product.getId()) {
                try {
                    p.setName(product.getName());
                    p.setCategory(product.getCategory());
                    p.setDescription(product.getDescription());
                    p.setPrice(product.getPrice());
                    p.setQuantity(product.getQuantity());
                    System.out.println("Product details updated: " + p);
                }
                catch (InvalidProductException | InvalidCategoryException e) {
                    System.out.println(e.getMessage());
                }
                return;
            }
        }
        throw new IllegalArgumentException("Product with ID " + product.getId() + " not found.");
    }
    @Override
    public void delete(Product product) {
        if (Database.products.removeIf(p -> p.getId()==product.getId())) {
            System.out.println("Product deleted: " + product.getId());
        } else {
            System.out.println( "Product with ID " + product.getId() + " not found.");
        }
    }

}
