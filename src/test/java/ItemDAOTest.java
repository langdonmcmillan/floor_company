/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import daos.ItemDAO;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 *
 * @author apprentice
 */
public class ItemDAOTest {
    private ItemDAO itemTest;
    
    public ItemDAOTest() {
        
    }
    
    @Before
    public void setup() {
        ApplicationContext ctx = new 
            ClassPathXmlApplicationContext("applicationContext.xml");
        itemTest = (ItemDAO) ctx.getBean("itemTest");
    }
    
    @Test
    public void testItemDAO() {
        assertEquals(7, itemTest.getAllItems().size());
        assertEquals("Laminate", itemTest.getAllItems().get(1).getItemName());
        assertEquals(2.25, itemTest.getAllItems().get(0).getMaterialPricePerSqFt(), .00005);
        assertEquals(4.15, itemTest.getAllItems().get(2).getLaborPricePerSqFt(), .00005);
        assertEquals(4, itemTest.getAllItems().get(3).getItemID());
    }
}
