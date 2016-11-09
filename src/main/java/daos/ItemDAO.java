/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package daos;

import java.util.ArrayList;
import models.Item;

/**
 *
 * @author apprentice
 */
public interface ItemDAO {
    public ArrayList<Item> getAllItems();
}
