package DAO;

import entities.Category;
import entities.Database;
import exceptions.InvalidCategoryException;

public class DAOCategory implements CRUD<Category> {
    @Override
    public void create(Category category) {
        if (category == null) {
            System.out.println("Category cannot be null.");
            throw new IllegalArgumentException("Category cannot be null.");
        }
        Database.categories.add(category);
        System.out.println("Category added successfully");
    }
    @Override
    public Category read(Category category) {
        for (Category c : Database.categories) {
            if (c.getId() == category.getId()) {
                return c;
            }
        }
        System.out.println( "Category with ID " + category.getId() + " not found.");
        throw new IllegalArgumentException("Category not found.");
    }
    @Override
    public void update(Category category) {
        for (Category c : Database.categories) {
            if (c.getId() == category.getId()) {
                try {
                    c.setName(category.getName());
                    System.out.println("Category details updated: " + c);
                }catch (InvalidCategoryException e){
                    System.out.println(e.getMessage());
                }
                return;
            }
        }

        System.out.println("Category with ID " + category.getId() + " not found.");
    }
    @Override
    public void delete(Category category) {
        if (Database.categories.removeIf(c -> c.getId()==category.getId())) {
            System.out.println("Category deleted: " + category);
            Database.products.removeIf(p->p.getCategory()==category);
        } else {
            System.out.println( "Category with ID " + category.getId() + " not found.");
        }
    }
}
