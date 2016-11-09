/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 *
 * @author apprentice
 */
public class OrderItem {
    private int orderID;
    private int itemID;
    private double materialPricePerSqFt;
    private double laborPricePerSqFt;
    private int sqFt;
    private DecimalFormat df = new DecimalFormat("#.##");
    
    public OrderItem() {
    
    }

    public OrderItem(int orderID, int itemID, ArrayList<Item> itemList) {
        Item item = itemList.get(itemID -1);
        this.sqFt = 0;
        this.orderID = orderID;
        this.itemID = itemID;
        this.materialPricePerSqFt = item.getMaterialPricePerSqFt();
        this.laborPricePerSqFt = item.getLaborPricePerSqFt();
    }

    public OrderItem(OrderItem otherOrderItem) {
        this.orderID = otherOrderItem.getOrderID();
        this.itemID = otherOrderItem.getItemID();
        this.materialPricePerSqFt = otherOrderItem.getMaterialPricePerSqFt();
        this.laborPricePerSqFt = otherOrderItem.getLaborPricePerSqFt();
        this.sqFt = otherOrderItem.getSqFt();
    }
    
    public int getOrderID() {
        return orderID;
    }

    public void setOrderID(int orderID) {
        this.orderID = orderID;
    }

    public int getItemID() {
        return itemID;
    }

    public void setItemID(int itemID) {
        this.itemID = itemID;
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
    
    public double getMaterialTotal() {
        return Double.valueOf(df.format(materialPricePerSqFt * sqFt));
    }
    
    public double getLaborTotal() {
        return Double.valueOf(df.format(laborPricePerSqFt * sqFt));
    }
    
    public double getSubTotal() {
        return getLaborTotal() + getMaterialTotal();
    }

    public int getSqFt() {
        return sqFt;
    }

    public void setSqFt(int sqFt) {
        this.sqFt = sqFt;
    }
}
