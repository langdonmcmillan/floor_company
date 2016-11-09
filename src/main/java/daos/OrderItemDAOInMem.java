/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package daos;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import models.Order;
import models.OrderItem;

/**
 *
 * @author apprentice
 */
public class OrderItemDAOInMem implements OrderItemDAO {

    private HashMap<Integer, ArrayList<OrderItem>> orderItems;

    public OrderItemDAOInMem() {
        orderItems = new HashMap<>();
    }

    @Override
    public void addOrderItem(String date, OrderItem orderItem) {
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
            updateOrderItem("", orderItem);
        }
    }

    @Override
    public void updateOrderItem(String date, OrderItem orderItem) {
        removeOrderItem(date, orderItem);
        addOrderItem(date, orderItem);
    }

    @Override
    public void removeOrderItem(String date, OrderItem orderItem) {
        orderItems.get(orderItem.getOrderID()).removeAll(orderItems.get(orderItem.getOrderID())
                .stream().filter(oi -> oi.getItemID() == orderItem.getItemID()).collect(Collectors.toList()));
    }

    @Override
    public void removeOrder(String date, int orderID) {
        orderItems.remove(orderID);
    }

    @Override
    public ArrayList<OrderItem> getOrderItems(String date, int orderID) {
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
        return orderItems.get(orderID).stream().filter(oi -> oi.getItemID() == itemID).findFirst().get();
    }
    
    @Override
    public double getOrderItemsSubTotal(String date, int orderID) {
        double subTotal = getOrderItems(date, orderID)
                .stream().mapToDouble(OrderItem::getSubTotal).sum();
        return subTotal;
    }

    @Override
    public void save() {
    }

}
