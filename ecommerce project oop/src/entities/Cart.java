package entities;

import DAO.DAOCart;

import java.util.ArrayList;
import java.util.List;

public class Cart {
    private static final DAOCart daoCart = new DAOCart();

    int id;
    private int generateCartId() {
        return Database.carts.size() + 1;
    }
    public static class CartItem {
        private final Product product;
        private int quantity;

        public CartItem(Product product, int quantity) {
            this.product = product;
            this.quantity = quantity;
        }

        public Product getProduct() {
            return product;
        }

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }
    }
    private List<CartItem> items;

    public Cart() {
        items=new ArrayList<>();
        id=generateCartId();
        daoCart.create(this);
    }
    public int getId() {
        return id;
    }

    public void setId(int cartId) {
        this.id = cartId;
    }
    public void setCartItems(List<CartItem> items) {
        this.items = items;
    }

    public void addCartItem(Product product, int quantity) {
        if (product == null || quantity <= 0) {
            throw new IllegalArgumentException("Product cannot be null, and quantity must be positive.");
        }
        else if(product.getQuantity()<quantity){
            throw new IllegalArgumentException("Quantity of product is greater than the stock");
        }
        for (CartItem i:items) {
            if (i.product == product) {
                i.quantity += quantity;
                return;
            }
        }
        items.add(new CartItem(product,quantity));
    }

    public void removeCartItem(Product product) {
        if (product == null) {
            throw new IllegalArgumentException("Product cannot be null.");
        }
        items.removeIf(i -> i.product == product);
    }

    public double calculateTotalPrice() {
        double totalPrice = 0;
        for (CartItem i:items) {
            totalPrice += i.product.getPrice() * i.quantity;
        }
        return totalPrice;
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

    @Override
    public String toString() {
        if (items.isEmpty()) {
            return "Cart is empty.";
        }

        StringBuilder cartDetails = new StringBuilder("Cart Items:\n");
        for (CartItem i:items) {
            cartDetails.append(i.product.getName())
                    .append(" (x")
                    .append(i.quantity)
                    .append(")\n");
        }
        cartDetails.append("Total Price: $").append(calculateTotalPrice());
        return cartDetails.toString();
    }

    public List<CartItem> getItems() {
        return new ArrayList<>(items);
    }
}
