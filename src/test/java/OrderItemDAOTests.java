/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import daos.OrderItemDAO;
import java.time.LocalDate;
import models.OrderItem;
import org.junit.After;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 *
 * @author apprentice
 */
public class OrderItemDAOTests {

    private OrderItemDAO orderItemTest;
    private String currentDateString;

    public OrderItemDAOTests() {
    }

    @Before
    public void setup() {
        ApplicationContext ctx = new 
            ClassPathXmlApplicationContext("applicationContext.xml");
        orderItemTest = (OrderItemDAO) ctx.getBean("orderItemTest");
        LocalDate currentDate = LocalDate.now();
        currentDateString = currentDate.toString();
    }

    @After
    public void reset() {

    }

    @Test
    public void testOrderItem() {
        OrderItem testOrderItem = new OrderItem();
        testOrderItem.setItemID(1);
        testOrderItem.setOrderID(1);
        testOrderItem.setLaborPricePerSqFt(5);
        testOrderItem.setMaterialPricePerSqFt(5);
        testOrderItem.setSqFt(10);
        
        // Add one item
        orderItemTest.addOrderItem("Date", testOrderItem);

        // Test the addition
        assertEquals(1, orderItemTest.getOrderItems("Date", 1).size());
        assertEquals(50, orderItemTest.getOrderItems("Date", 1).get(0).getMaterialTotal(), .00005);
        
        // Edit the item
        testOrderItem = new OrderItem();
        testOrderItem = orderItemTest.getOrderItems("Date", 1).get(0);
        testOrderItem.setSqFt(50);
        orderItemTest.updateOrderItem(currentDateString, testOrderItem);

        // Asserts the order remains the same other than the edited part
        assertEquals(1, orderItemTest.getOrderItems("Date", 1).size());
        assertEquals(250, orderItemTest.getOrderItems("Date", 1).get(0).getMaterialTotal(), .00005);
        assertEquals(50, orderItemTest.getOrderItems("Date", 1).get(0).getSqFt());

        // Remove the item
        orderItemTest.removeOrderItem(currentDateString, testOrderItem);

        // Test the removal was successful
        assertEquals(0, orderItemTest.getOrderItems("Date", 1).size());
        
        testOrderItem = new OrderItem();
        testOrderItem.setItemID(1);
        testOrderItem.setOrderID(1);
        testOrderItem.setLaborPricePerSqFt(5);
        testOrderItem.setMaterialPricePerSqFt(5);
        testOrderItem.setSqFt(10);
        
        // Adding another item to an order
        orderItemTest.addOrderItem("Date", testOrderItem);
        
        testOrderItem = new OrderItem();
        testOrderItem.setItemID(2);
        testOrderItem.setOrderID(1);
        testOrderItem.setLaborPricePerSqFt(3);
        testOrderItem.setMaterialPricePerSqFt(3);
        testOrderItem.setSqFt(5);

        // Adding a second item to the same order
        orderItemTest.addOrderItem("Date", testOrderItem);
        
        // Checking that there are two orders now
        assertEquals(2, orderItemTest.getOrderItems("Date", 1).size());
        
        testOrderItem = new OrderItem();
        testOrderItem.setItemID(3);
        testOrderItem.setOrderID(1);
        testOrderItem.setLaborPricePerSqFt(5);
        testOrderItem.setMaterialPricePerSqFt(5);
        testOrderItem.setSqFt(5);

        // Adding a third item to the same order
        orderItemTest.addOrderItem("Date", testOrderItem);
        
        // Checking that there are three orders now
        assertEquals(3, orderItemTest.getOrderItems("Date", 1).size());
        
        // Adding more of the same item - should merge rather than create a new item
        testOrderItem = new OrderItem();
        testOrderItem.setItemID(3);
        testOrderItem.setOrderID(1);
        testOrderItem.setLaborPricePerSqFt(5);
        testOrderItem.setMaterialPricePerSqFt(5);
        testOrderItem.setSqFt(5);

        // Adding to order
        orderItemTest.addOrderItem("Date", testOrderItem);
        
        // Checking that there are still only three
        assertEquals(3, orderItemTest.getOrderItems("Date", 1).size());
        // Checking that the square footage was added correctly
        assertEquals(10, orderItemTest.getOrderItems("Date", 1).get(2).getSqFt());
        
        //Check getSubTotal
        assertEquals(230, orderItemTest.getOrderItemsSubTotal(currentDateString, 1), .000005);
        
        // Removing one order item
        testOrderItem = orderItemTest.getOrderItems(currentDateString, 1).get(2);
        orderItemTest.removeOrderItem(currentDateString, testOrderItem);
        
        // Should be two now for the order
        assertEquals(2, orderItemTest.getOrderItems("Date", 1).size()); 
        
        // Removing an order should take all items with it
        orderItemTest.removeOrder(currentDateString, 1);
        
        // Testing that all are removed
        assertEquals(0, orderItemTest.getOrderItems("Date", 1).size()); 
    }

}
