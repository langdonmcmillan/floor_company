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
public class ItemDAOInMem implements ItemDAO {

    ArrayList<Item> itemList;
    
    public ItemDAOInMem() {
        itemList = new ArrayList<>();
        loadItemList();
    }
    
    @Override
    public ArrayList<Item> getAllItems() {
        return itemList;
    }

    private void loadItemList() {
        Item item = new Item();
        item.setItemID(1);
        item.setItemName("Carpet");
        item.setMaterialPricePerSqFt(2.25);
        item.setLaborPricePerSqFt(2.10);
        itemList.add(item);
        item = new Item();
        item.setItemID(2);
        item.setItemName("Laminate");
        item.setMaterialPricePerSqFt(1.75);
        item.setLaborPricePerSqFt(2.10);
        itemList.add(item);
        item = new Item();
        item.setItemID(3);
        item.setItemName("Tile");
        item.setMaterialPricePerSqFt(3.5);
        item.setLaborPricePerSqFt(4.15);
        itemList.add(item);
        item = new Item();
        item.setItemID(4);
        item.setItemName("Wood");
        item.setMaterialPricePerSqFt(5.15);
        item.setLaborPricePerSqFt(4.75);
        itemList.add(item);
    }
    
}
