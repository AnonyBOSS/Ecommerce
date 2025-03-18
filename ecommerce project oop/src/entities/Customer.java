package entities;
import java.util.ArrayList;
import java.util.List;

public class Customer extends User {
    

    public void addBalance(double amount) throws IllegalArgumentException {
        if (amount < 0) {
            throw new IllegalArgumentException("Amount must be positive.");
        }
        this.balance += amount;
        System.out.println("Added " + amount + " to balance.");
    }

    public Order checkout(Order.PaymentMethod paymentMethod) throws Exception {
        if(balance>=cart.calculateTotalPrice()){
        balance -= cart.calculateTotalPrice();
        Order o= new Order(this,cart, paymentMethod);
        this.addOrder(o);
        for(Cart.CartItem i: cart.getItems()){
            i.getProduct().setQuantity(i.getProduct().getQuantity()-i.getQuantity());
        }
        cart=new Cart();
        return o;
        }
        else throw new Exception("Insufficient balance");
    }

    public enum Gender {
        MALE, FEMALE,
    }

    private double balance;
    private String Address;
    private Gender Gender;
    private List<String> Interests;
    private Cart cart;

    private final List<Order> orders = new ArrayList<>();

    public List<Order> getOrders() {
        return orders;
    }

    public void addOrder(Order order) {
        orders.add(order);
    }


    // Constructor
    public Customer(String username, String password, String dateOfBirth,String address, Gender gender, List<String> interests) {
        super(username, password, dateOfBirth);

        if (address == null || address.isEmpty()) {
            throw new IllegalArgumentException("Address cannot be null or empty.");
        }
        if (gender == null) {
            throw new IllegalArgumentException("Gender cannot be null.");
        }
        if (interests == null || interests.isEmpty()) {
            throw new IllegalArgumentException("Interests cannot be null or empty.");
        }

        balance = 0;
        this.Address = address;
        this.Gender = gender;
        this.Interests = new ArrayList<>(interests);
        cart= new Cart();
    }

    // Getters and Setters
    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        if (balance < 0) {
            throw new IllegalArgumentException("Balance must be positive.");
        }
        this.balance = balance;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String Address) {
        if (Address == null || Address.isEmpty()) {
            throw new IllegalArgumentException("Address cannot be null or empty.");
        }
        this.Address = Address;
    }

    public Gender getGender() {
        return Gender;
    }

    public void setGender(Gender Gender) {
        if (Gender == null) {
            throw new IllegalArgumentException("Gender cannot be null.");
        }
        this.Gender = Gender;
    }

    public List<String> getInterests() {
        return new ArrayList<>(Interests);
    }

    public void setInterests(List<String> Interests) {
        if (Interests == null || Interests.isEmpty()) {
            throw new IllegalArgumentException("Interests cannot be null or empty.");
        }
        this.Interests = new ArrayList<>(Interests);
    }
    public Cart getCart() {
        return cart;
    }
    public void setCart(Cart cart) {
        this.cart = cart;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "username='" + getUserName() + '\'' +
                ", balance=" + balance +
                ", address='" + Address + '\'' +
                ", gender=" + Gender +
                ", interests=" + Interests +
                '}';
    }

    public void addProductToCart( Product product, int quantity) {
        cart.addCartItem(product, quantity);
        System.out.println("Product added to cart: " + product.getName() + " (x" + quantity + ")");
    }

    public void removeProductFromCart(Product product) {
        if (cart == null) {
            throw new IllegalArgumentException("Cart cannot be null.");
        }
        cart.removeCartItem(product);
        System.out.println("Product removed from cart: " + product.getName());
    }
}
