/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

/**
 *
 * @author apprentice
 */
public class Item {
    private int itemID;
    private String itemName;
    private double materialPricePerSqFt;
    private double laborPricePerSqFt;
    
    public Item() {
        
    }

    public int getItemID() {
        return itemID;
    }

    public void setItemID(int itemID) {
        this.itemID = itemID;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public double getMaterialPricePerSqFt() {
        return materialPricePerSqFt;
    }

    public void setMaterialPricePerSqFt(double materialPricePerSqFt) {
        this.materialPricePerSqFt = materialPricePerSqFt;
    }

    public double getLaborPricePerSqFt() {
        return laborPricePerSqFt;
    }

    public void setLaborPricePerSqFt(double laborPricePerSqFt) {
        this.laborPricePerSqFt = laborPricePerSqFt;
    }
    
    
}
