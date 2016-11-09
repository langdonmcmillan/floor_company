/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import daos.OrderDAO;
import java.time.LocalDate;
import java.util.ArrayList;
import models.Order;
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
public class OrderDAOEmptyTests {
    private OrderDAO orderTest;
    String currentDateString;
    
    public OrderDAOEmptyTests() {
        
    }
    
    @Before
    public void setup() {
        ApplicationContext ctx = new 
            ClassPathXmlApplicationContext("applicationContext.xml");
        orderTest = (OrderDAO) ctx.getBean("orderTest");
        LocalDate currentDate = LocalDate.now();
        currentDateString = currentDate.toString();
    }
    
    @After
    public void reset() {
        
    }
    
    @Test
    public void testOneEntry() {
        Order testOrder = new Order();
        testOrder.setCustomerName("Larry");
        testOrder.setCustomerState("OH");
        testOrder.setTaxRate(6.5);
        testOrder.setTotalCost(50.00);
        
        // Add one order
        orderTest.addOrder(currentDateString, testOrder);
        
        // Test the addition
        assertEquals(1, orderTest.getOrdersFromDate(currentDateString).size());
        assertEquals("Larry", orderTest.getOrdersFromDate(currentDateString).get(0).getCustomerName());
        
        // Get and edit order
        testOrder = orderTest.getOrdersFromDate(currentDateString).get(0);
        testOrder.setCustomerName("Bobby");
        orderTest.updateOrder(currentDateString, testOrder);
        
        // Test the edit
        assertEquals(1, orderTest.getOrdersFromDate(currentDateString).size());
        assertEquals("Bobby", orderTest.getOrdersFromDate(currentDateString).get(0).getCustomerName());
        
        // Test the search by name
        ArrayList<Order> nameListTest = orderTest.searchOrdersByNameState("Bobby");
        assertEquals(1, nameListTest.size());
        assertEquals("Bobby", nameListTest.get(0).getCustomerName());
        
        // Test remove order
        orderTest.removeOrder(currentDateString, testOrder);
        
        assertEquals(0, orderTest.getOrdersFromDate(currentDateString).size());
    }
}
