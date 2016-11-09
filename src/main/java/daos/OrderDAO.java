/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package daos;

import java.util.ArrayList;
import models.Order;

/**
 *
 * @author apprentice
 */
public interface OrderDAO {

    public void addOrder(String date, Order order);

    public int assignID(String date);

    public void updateOrder(String date, Order order);

    public void removeOrder(String date, Order order);

    public ArrayList<Order> getOrdersFromDate(String date);

    public Order getOrderWithID(int orderID);

    public ArrayList<Order> searchOrdersByNameState(String name);

    public void save();
}
