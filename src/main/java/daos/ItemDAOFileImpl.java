/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package daos;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import models.Item;

/**
 *
 * @author apprentice
 */
public class ItemDAOFileImpl implements ItemDAO {

    private final String FILE_PATH = "References/Inventory.txt";
    private final String DELIMITER = "::";
    ArrayList<Item> itemList;

    public ItemDAOFileImpl() {
        itemList = new ArrayList<>();
        loadItemList();
    }

    @Override
    public ArrayList<Item> getAllItems() {
        return itemList;
    }

    private void loadItemList() {
        try {
            Scanner sc = new Scanner(new BufferedReader(new FileReader(FILE_PATH)));
            while (sc.hasNextLine()) {
                itemList.add(decode(sc.nextLine()));
            }
        } catch (FileNotFoundException ex) {
            System.out.println("Error Loading Inventory");
        }
    }
    
    private Item decode(String encodedLine) {
        String[] encodedArray = encodedLine.split(DELIMITER);
        Item decodedItem = new Item();
        decodedItem.setItemID(Integer.parseInt(encodedArray[0]));
        decodedItem.setItemName(encodedArray[1]);
        decodedItem.setMaterialPricePerSqFt(Double.parseDouble(encodedArray[2]));
        decodedItem.setLaborPricePerSqFt(Double.parseDouble(encodedArray[3]));
        return decodedItem;
    }

}
