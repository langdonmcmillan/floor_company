/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package daos;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import models.Order;

/**
 *
 * @author apprentice
 */
public class OrderDAOFileImpl implements OrderDAO {

    private final String FILE_PATH_BASE = "Orders/Orders-";
    private final String DELIMITER = "::";
    private String filePathDate;
    private HashMap<Integer, Order> orders;

    public OrderDAOFileImpl() {
        orders = new HashMap<>();
    }

    @Override
    public void addOrder(String date, Order order) {
        checkDate(date);
        orders.put(order.getOrderID(), order);
    }

    @Override
    public int assignID(String date) {
        checkDate(date);
        int id = (orders.size() > 0) ? Collections.max(orders.keySet()) + 1 : 1;
        return id;
    }

    @Override
    public void updateOrder(String date, Order order) {
        checkDate(date);
        orders.replace(order.getOrderID(), order);
    }

    @Override
    public ArrayList<Order> getOrdersFromDate(String date) {
        checkDate(date);
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
        checkDate(date);
        orders.remove(order.getOrderID());
    }

    @Override
    public void save() {
        try {
            PrintWriter output = new PrintWriter(new FileWriter(FILE_PATH_BASE + filePathDate + ".txt"));
            encode().stream().forEach(s -> output.println(s));
            output.flush();
            output.close();
        } catch (IOException ex) {
            System.out.println("Error saving");
        }

    }

    private void loadDate() {
        orders = new HashMap<>();
        try {
            Scanner sc = new Scanner(new BufferedReader(new FileReader(FILE_PATH_BASE + filePathDate + ".txt")));
            while (sc.hasNextLine()) {
                Order decodedOrder = decode(sc.nextLine());
                orders.put(decodedOrder.getOrderID(), decodedOrder);
            }
        } catch (FileNotFoundException ex) {
        }
    }

    private Order decode(String encodedLine) {
        Order decodedOrder = new Order();
        String[] encodedArray = encodedLine.split(DELIMITER);
        decodedOrder.setOrderID(Integer.parseInt(encodedArray[0]));
        decodedOrder.setCustomerName(encodedArray[1]);
        decodedOrder.setCustomerState(encodedArray[2]);
        decodedOrder.setTaxRate(Double.parseDouble(encodedArray[3]));
        decodedOrder.setTotalCost(Double.parseDouble(encodedArray[4]));
        return decodedOrder;
    }

    private ArrayList<String> encode() {
        ArrayList<String> encodedOrders = new ArrayList<>();
        orders.values().stream().forEach(o -> encodedOrders
                .add("" + o.getOrderID() + DELIMITER + o.getCustomerName()
                        + DELIMITER + o.getCustomerState() + DELIMITER + o.getTaxRate()
                        + DELIMITER + o.getTotalCost()));
        return encodedOrders;
    }

    private void checkDate(String date) {
        if (!date.equals(filePathDate)) {
            filePathDate = date;
            loadDate();
        }
    }
}
