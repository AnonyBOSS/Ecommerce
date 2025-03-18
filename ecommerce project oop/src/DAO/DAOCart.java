package DAO;

import entities.Cart;
import entities.Database;


public class DAOCart implements CRUD<Cart> {
    @Override
    public void create(Cart cart) {
        if (cart == null) {
            throw new IllegalArgumentException("Cart cannot be null.");
        }
    Database.carts.add(cart);
    System.out.println("Cart created");
    }
    @Override
    public Cart read(Cart cart) {
        for (Cart c : Database.carts) {
            if (c.getId() == cart.getId()) {
                return c;
            }
        }
        System.out.println( "Cart with ID " + cart.getId() + " not found.");
        throw new IllegalArgumentException("Cart not found.");
    }
    @Override
    public void update(Cart cart) {
        boolean updated = false;
        for (Cart c : Database.carts) {
            if (c.getId() == cart.getId()) {
                c.setCartItems(cart.getItems());
                updated = true;
                System.out.println("Cart details updated.");
                break;
            }
        }
        if (!updated) {
            throw new IllegalArgumentException("Cart with ID " + cart.getId() + " not found.");
        }
    }

    @Override
    public void delete(Cart cart) {
        if (Database.carts.removeIf(c -> c.getId()==cart.getId())) {
            System.out.println("Cart deleted: " + cart.getId());
        } else {
            throw new IllegalArgumentException( "Cart with ID " + cart.getId() + " not found.");
        }
    }
}
