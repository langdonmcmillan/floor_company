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
public class OrderDAOTests {

    private OrderDAO orderDAOTests;
    String currentDateString;

    public OrderDAOTests() {

    }

    @Before
    public void setup() {
        ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
        orderDAOTests = (OrderDAO) ctx.getBean("orderTest");
        LocalDate currentDate = LocalDate.now();
        currentDateString = currentDate.toString();
        
        // Add 50 orders for setup
        for (int i = 1; i <= 50; i++) {
            Order testOrder = new Order();
            testOrder.setCustomerName("Customer" + i);
            testOrder.setCustomerState("OH");
            testOrder.setTaxRate(6.5);
            testOrder.setTotalCost(50.00 + i);
            testOrder.setOrderID(orderDAOTests.assignID(currentDateString));
            orderDAOTests.addOrder(currentDateString, testOrder);
        }
        
        // Add one different order to test search functionality
        Order testOrder = new Order();
        testOrder.setCustomerName("Larry");
        testOrder.setCustomerState("MI");
        testOrder.setTaxRate(6.5);
        testOrder.setTotalCost(50.00);
        testOrder.setOrderID(orderDAOTests.assignID(currentDateString));
        orderDAOTests.addOrder(currentDateString, testOrder);
    }

    @After
    public void reset() {

    }

    @Test
    public void testAddOrder() {
        // Make sure the setup is correct
        assertEquals(51, orderDAOTests.getOrdersFromDate(currentDateString).size());
        
        // Create order to add
        Order testOrder = new Order();
        testOrder.setCustomerName("John");
        testOrder.setCustomerState("PA");
        testOrder.setTaxRate(6.5);
        testOrder.setTotalCost(50.00);
        testOrder.setOrderID(orderDAOTests.assignID(currentDateString));
        
        // Add another order
        orderDAOTests.addOrder(currentDateString, testOrder);
        
        // Make sure another is added
        assertEquals(52, orderDAOTests.getOrdersFromDate(currentDateString).size());
    }

    @Test
    public void testUpdateOrder() {
        assertEquals(51, orderDAOTests.getOrdersFromDate(currentDateString).size());
        
        // Get the order with OrderID 25
        Order testOrder = orderDAOTests.getOrderWithID(25);
        testOrder.setCustomerName("John");
        testOrder.setCustomerState("PA");
        testOrder.setTaxRate(7.5);
        testOrder.setTotalCost(55.00);
        orderDAOTests.updateOrder(currentDateString, testOrder);
        
        // Make sure still same amount of orders
        assertEquals(51, orderDAOTests.getOrdersFromDate(currentDateString).size());
        
        // Make sure the order with OrderID 25 is changed successfully
        assertEquals("John", orderDAOTests.getOrderWithID(25).getCustomerName());
        assertEquals("PA", orderDAOTests.getOrderWithID(25).getCustomerState());
        assertEquals(25, orderDAOTests.getOrderWithID(25).getOrderID());
    }

    @Test
    public void testRemoveOrder() {
        assertEquals(51, orderDAOTests.getOrdersFromDate(currentDateString).size());
        
        // Get order with OrderID 26
        Order testOrder = orderDAOTests.getOrdersFromDate(currentDateString).get(25);
        
        // Remove order
        orderDAOTests.removeOrder(currentDateString, testOrder);
        
         assertEquals(50, orderDAOTests.getOrdersFromDate(currentDateString).size());
    }

    @Test
    public void testSearchOrdersByNameState() {
        assertEquals(51, orderDAOTests.getOrdersFromDate(currentDateString).size());
        assertEquals(50, orderDAOTests.searchOrdersByNameState("Customer").size());
        assertEquals(50, orderDAOTests.searchOrdersByNameState("OH").size());
        assertEquals(1, orderDAOTests.searchOrdersByNameState("MI").size());
        assertEquals(1, orderDAOTests.searchOrdersByNameState("Larry").size());
    }
    
    @Test
    public void testAssignID() {
        assertEquals(51, orderDAOTests.getOrdersFromDate(currentDateString).size());
        assertEquals(52, orderDAOTests.assignID(currentDateString));
    }
}
