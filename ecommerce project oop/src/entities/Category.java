package entities;
import java.util.Objects;
import DAO.DAOCategory;
import exceptions.InvalidCategoryException;

public class Category {
    private int id;
    private int generateCategoryId() {
        return Database.categories.size() + 1;
    }
    private String name;
    private static final DAOCategory daoCategory = new DAOCategory();
    public Category(String name) throws InvalidCategoryException {
        if (name == null || name.trim().isEmpty()) {
            throw new InvalidCategoryException("Category name cannot be null or empty.");
        }
        id=generateCategoryId();
        setName(name);
        daoCategory.create(this);
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) throws InvalidCategoryException {
        if (id <= 0) {
            throw new InvalidCategoryException("Category ID must be greater than 0");
        }
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) throws InvalidCategoryException {
        if (name == null || name.trim().isEmpty()) {
            throw new InvalidCategoryException("Category name cannot be null or empty.");
        }
        if (isCategoryNameDuplicate(name)) {
            throw new InvalidCategoryException("Category name must be unique.");
        }
        this.name = name.trim();
    }

    private boolean isCategoryNameDuplicate(String name) {
        for (Category category : Database.categories) {
            if (category.getName().equalsIgnoreCase(name)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Category category = (Category) o;
        return id == category.id && Objects.equals(name, category.name);
    }
    @Override
    public String toString() {
        return "Category{" + "id=" + id + ", name='" + name + '\'' + '}';
    }
}

