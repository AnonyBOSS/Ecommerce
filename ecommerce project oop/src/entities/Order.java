package entities;

import DAO.DAOOrder;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Order {
    public enum OrderStatus {
        PENDING,PROCESSING,SHIPPED,DELIVERED,CANCELED
    }
    public enum PaymentMethod {
        CREDIT_CARD,DEBIT_CARD,PAYPAL,CASH_ON_DELIVERY,BANK_TRANSFER
    }
    private int id;
    private Customer customer;
    private List<Cart.CartItem> items;
    private double totalPrice;
    private Date orderDate;
    private OrderStatus status;
    private PaymentMethod paymentMethod;
    private static final DAOOrder daoOrder = new DAOOrder();
    private int generateOrderId() {
        return Database.orders.size() + 1;
    }
    public Order(Customer customer, Cart cart, PaymentMethod paymentMethod) {
        if (customer == null) {
            throw new IllegalArgumentException("Customer cannot be null.");
        }
        if (cart == null || cart.isEmpty()) {
            throw new IllegalArgumentException("Cart cannot be null or empty.");
        }
        if (paymentMethod == null) {
            throw new IllegalArgumentException("Payment method cannot be null.");
        }

        this.id =generateOrderId();
        this.customer = customer;
        this.totalPrice = cart.calculateTotalPrice();
        this.orderDate = new Date();
        this.status = OrderStatus.PENDING;
        this.paymentMethod = PaymentMethod.CREDIT_CARD;
        items=cart.getItems();
        daoOrder.create(this);
    }

    public void setStatus(OrderStatus status) {
        if (status == null) {
            throw new IllegalArgumentException("Order status cannot be null.");
        }
        this.status = status;
    }

    // Getters
    public int getId() {
        return id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public List<Cart.CartItem> getItems() {
        return new ArrayList<>(items); // Defensive copy
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    @Override
    public String toString() {
        StringBuilder orderDetails = new StringBuilder("Order Details:\n");
        orderDetails.append("Order ID: ").append(id).append("\n");
        orderDetails.append("Customer: ").append(customer.getUserName()).append("\n");
        orderDetails.append("Order Date: ").append(orderDate).append("\n");
        orderDetails.append("Status: ").append(status).append("\n");
        orderDetails.append("Payment Method: ").append(paymentMethod).append("\n");
        orderDetails.append("Items:\n");

        for (Cart.CartItem i:items) {
            orderDetails.append(i.getProduct().getName())
                    .append(" (x")
                    .append(i.getQuantity())
                    .append(")\n");
        }
        orderDetails.append("Total Price: $").append(totalPrice);
        return orderDetails.toString();
    }
    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public void setItems(List<Cart.CartItem> items) {
        this.items= items;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
}