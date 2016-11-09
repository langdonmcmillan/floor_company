/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import daos.TaxDAO;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 *
 * @author apprentice
 */
public class TaxDAOTest {
    private TaxDAO taxTest;
    
    public TaxDAOTest() {
    }
    
    @Before
    public void setup() {
        ApplicationContext ctx = new 
            ClassPathXmlApplicationContext("applicationContext.xml");
        taxTest = (TaxDAO) ctx.getBean("taxTest");
    }
    
    @Test
    public void testTaxDAO() {
        assertEquals(6.25, taxTest.getRate("OH"), .00005);
        assertEquals(6.75, taxTest.getRate("PA"), .00005);
        assertEquals(5.75, taxTest.getRate("MI"), .00005);
        assertEquals(6.00, taxTest.getRate("IN"), .00005);
    }
}
