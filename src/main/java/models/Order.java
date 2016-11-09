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
public class Order {
    private int orderID;
    private String customerName;
    private String customerState;
    private double taxRate;
    private double totalCost;

    public Order() {
        this.customerName = "";
        this.customerState = "";
        this.taxRate = 0;
        this.totalCost = 0;
    }
    
    public Order(Order otherOrder) {
        this.orderID = otherOrder.getOrderID();
        this.customerName = otherOrder.customerName;
        this.customerState = otherOrder.customerState;
        this.taxRate = otherOrder.getTaxRate();
        this.totalCost = otherOrder.getTotalCost();
    }
    
    public int getOrderID() {
        return orderID;
    }

    public void setOrderID(int orderID) {
        this.orderID = orderID;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerState() {
        return customerState;
    }

    public void setCustomerState(String customerState) {
        this.customerState = customerState;
    }

    public double getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(double taxRate) {
        this.taxRate = taxRate;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }
    
    
}
