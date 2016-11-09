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
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import models.OrderItem;

/**
 *
 * @author apprentice
 */
public class OrderItemDAOFileImpl implements OrderItemDAO {
    
    private HashMap<Integer, ArrayList<OrderItem>> orderItems;
    private final String FILE_PATH_BASE = "Orders/Order_Items-";
    private final String DELIMITER = "::";
    private String filePathDate = "";
    
    public OrderItemDAOFileImpl() {
        orderItems = new HashMap<>();
    }
    
    @Override
    public void addOrderItem(String date, OrderItem orderItem) {
        checkDate(date);
        if (orderItems.containsKey(orderItem.getOrderID())) {
            orderItems.get(orderItem.getOrderID()).add(orderItem);
            mergeDuplicates(orderItem);
        } else {
            orderItems.put(orderItem.getOrderID(), new ArrayList<>());
            orderItems.get(orderItem.getOrderID()).add(orderItem);
        }
    }
    
    private void mergeDuplicates(OrderItem orderItem) {
        if (orderItems.get(orderItem.getOrderID()).stream()
                .filter(oi -> oi.getItemID() == orderItem.getItemID()).count() > 1) {
            int sqFtSum = orderItems.get(orderItem.getOrderID()).stream()
                    .filter(oi -> oi.getItemID() == orderItem.getItemID()).mapToInt(OrderItem::getSqFt).sum();
            orderItem.setSqFt(sqFtSum);
            updateOrderItem(filePathDate, orderItem);
        }        
    }
    
    @Override    
    public void updateOrderItem(String date, OrderItem orderItem) {
        checkDate(date);
        removeOrderItem(filePathDate, orderItem);
        addOrderItem(filePathDate, orderItem);
    }
    
    @Override
    public void removeOrderItem(String date, OrderItem orderItem) {
        checkDate(date);
        orderItems.get(orderItem.getOrderID()).removeAll(orderItems.get(orderItem.getOrderID())
                .stream().filter(oi -> oi.getItemID() == orderItem.getItemID()).collect(Collectors.toList()));
    }
    
    @Override
    public void removeOrder(String date, int orderID) {
        checkDate(date);
        orderItems.remove(orderID);
    }
    
    @Override
    public ArrayList<OrderItem> getOrderItems(String date, int orderID) {
        checkDate(date);
        List<OrderItem> orderItemList = new ArrayList<>();
        if (orderItems.containsKey(orderID)) {
            orderItems.get(orderID).stream()
                    .sorted((oi1, oi2) -> Integer.compare(oi1.getItemID(), oi2.getItemID()))
                    .forEach(oi -> orderItemList.add(new OrderItem(oi)));
        }        
        return (ArrayList<OrderItem>) orderItemList;
    }
    
    @Override
    public OrderItem getOrderItem(String date, int orderID, int itemID) {
        checkDate(date);
        OrderItem orderItem = orderItems.get(orderID).stream().filter(oi -> oi.getItemID() == itemID).findFirst().get();
        return new OrderItem(orderItem);
    }
    
    @Override
    public double getOrderItemsSubTotal(String date, int orderID) {
        double subTotal = getOrderItems(date, orderID)
                .stream().mapToDouble(OrderItem::getSubTotal).sum();
        return subTotal;
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
        orderItems = new HashMap<>();
        try {
            Scanner sc = new Scanner(new BufferedReader(new FileReader(FILE_PATH_BASE + filePathDate + ".txt")));
            while (sc.hasNextLine()) {
                OrderItem decodedOrderItem = decode(sc.nextLine());
                if (orderItems.containsKey(decodedOrderItem.getOrderID())) {
                    orderItems.get(decodedOrderItem.getOrderID()).add(decodedOrderItem);
                } else {
                    orderItems.put(decodedOrderItem.getOrderID(), new ArrayList<>());
                    orderItems.get(decodedOrderItem.getOrderID()).add(decodedOrderItem);
                }
            }
        } catch (FileNotFoundException ex) {
        }
    }
    
    private OrderItem decode(String encodedLine) {
        OrderItem decodedOrderItem = new OrderItem();
        String[] encodedArray = encodedLine.split(DELIMITER);
        decodedOrderItem.setOrderID(Integer.parseInt(encodedArray[0]));
        decodedOrderItem.setItemID(Integer.parseInt(encodedArray[1]));
        decodedOrderItem.setMaterialPricePerSqFt(Double.parseDouble(encodedArray[2]));
        decodedOrderItem.setLaborPricePerSqFt(Double.parseDouble(encodedArray[3]));
        decodedOrderItem.setSqFt(Integer.parseInt(encodedArray[4]));
        return decodedOrderItem;
    }
    
    private ArrayList<String> encode() {
        ArrayList<String> encodedOrderItems = new ArrayList<>();
        ArrayList<OrderItem> allOrderItems = new ArrayList<>();
        orderItems.values().stream()
                .forEach(oiList -> allOrderItems.addAll(oiList));
        allOrderItems.stream().forEach(oi -> encodedOrderItems.add("" + oi.getOrderID() + DELIMITER + oi.getItemID()
                + DELIMITER + oi.getMaterialPricePerSqFt() + DELIMITER
                + oi.getLaborPricePerSqFt() + DELIMITER + oi.getSqFt()));
        return encodedOrderItems;
    }
    
    private void checkDate(String date) {
        if (!date.equals(filePathDate)) {
            filePathDate = date;            
            loadDate();
        }
    }
}
