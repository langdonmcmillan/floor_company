/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package daos;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import models.Order;

/**
 *
 * @author apprentice
 */
public class OrderDAOInMem implements OrderDAO {

    private HashMap<Integer, Order> orders;

    public OrderDAOInMem() {
        orders = new HashMap<>();
    }

    @Override
    public void addOrder(String date, Order order) {
        orders.put(order.getOrderID(), order);
    }

    @Override
    public int assignID(String date) {
        int id = (orders.size() > 0) ? Collections.max(orders.keySet()) + 1 : 1;
        return id;
    }

    @Override
    public void updateOrder(String date, Order order) {
        orders.replace(order.getOrderID(), order);
    }

    @Override
    public ArrayList<Order> getOrdersFromDate(String date) {
        List<Order> orderList = new ArrayList<>();
        orders.values().stream()
                .forEach(o -> orderList.add(new Order(o)));
        return (ArrayList<Order>) orderList;
    }
    
    @Override
    public Order getOrderWithID(int orderID) {
        return orders.get(orderID);
    }

    @Override
    public ArrayList<Order> searchOrdersByNameState(String searchTerm) {
        List<Order> orderList = new ArrayList<>();
        orders.values().stream()
                .filter((o1 -> o1.getCustomerName().toLowerCase().contains(searchTerm.toLowerCase())
                        || o1.getCustomerState().equalsIgnoreCase(searchTerm)))
                .forEach(o -> orderList.add(new Order(o)));
        return (ArrayList<Order>) orderList;
    }

    @Override
    public void removeOrder(String date, Order order) {
        orders.remove(order.getOrderID());
    }

    @Override
    public void save() {
    }
}
