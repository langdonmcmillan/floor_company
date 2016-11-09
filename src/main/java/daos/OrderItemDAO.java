/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package daos;

import java.util.ArrayList;
import java.util.HashMap;
import models.OrderItem;

/**
 *
 * @author apprentice
 */
public interface OrderItemDAO {

    public void addOrderItem(String date, OrderItem orderItem);
    
    public void updateOrderItem(String date, OrderItem orderItem);

    public void removeOrderItem(String date, OrderItem orderItem);
    
    public void removeOrder(String date, int orderID);

    public ArrayList<OrderItem> getOrderItems(String date, int orderID);
    
    public OrderItem getOrderItem(String date, int orderID, int itemID);
    
    public double getOrderItemsSubTotal(String date, int orderID);

    public void save();
}
