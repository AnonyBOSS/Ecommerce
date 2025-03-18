package DAO;

import entities.Database;
import entities.Order;


public class DAOOrder implements CRUD<Order> {
    @Override
    public void create(Order order) {
        if (order == null) {
            throw new IllegalArgumentException("Order cannot be null.");
        }
        Database.orders.add(order);
        System.out.println("Order added successfully");
    }
    @Override
    public Order read(Order order) {
        for (Order o : Database.orders) {
            if (o.getId() == order.getId()) {
                return o;
            }
        }
        System.out.println( "Order with ID " + order.getId() + " not found.");
        throw new IllegalArgumentException("Order not found.");
    }
    @Override
    public void update(Order order) {
        for (Order o : Database.orders) {
            if (o.getId() == order.getId()) {
                try {
                    o.setCustomer(order.getCustomer());
                    o.setOrderDate(order.getOrderDate());
                    o.setItems(order.getItems());
                    o.setPaymentMethod(order.getPaymentMethod());
                    o.setStatus(order.getStatus());
                    o.setTotalPrice(order.getTotalPrice());
                    System.out.println("Order details updated: " + o);
                }
                catch (IllegalArgumentException e) {
                    System.out.println(e.getMessage());
                }
                return;
            }
        }
        throw new IllegalArgumentException("Order with ID " + order.getId() + " not found.");
    }
    @Override
    public void delete(Order order) {
        if (Database.orders.removeIf(o -> o.getId()==order.getId())) {
            System.out.println("Order deleted: " + order.getId());
        } else {
            System.out.println( "Order with ID " + order.getId() + " not found.");
        }
    }
}
