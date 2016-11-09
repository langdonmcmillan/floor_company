/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import models.Item;
import models.Order;
import models.OrderItem;

/**
 *
 * @author apprentice
 */
public class UserInterface {

    private ConsoleIO console;

    public UserInterface() {
        console = new ConsoleIO();
    }

    public int getMainMenuChoice(String date) {
        printBreak();
        console.printStringln("Current Working Date: " + date + ".");
        console.printStringln("What would you like to do? (Enter the corresponding number)");
        console.printStringln("1. Find Order");
        console.printStringln("2. Add Order");
        console.printStringln("3. Save Changes");
        console.printStringln("0. Exit");
        printBreak();
        return console.getInteger("Select an option:", 0, 3);
    }

    public boolean changeDate(String currentDate, boolean unsavedChanges) {
        boolean changeDate = false;
        if (getYN("Current date is " + currentDate + ". View orders from a different date? Y/N")) {
            if (unsavedChanges) {
                if (getYN("Loading orders from a different date will result in the "
                        + "loss of unsaved changes. Continue? Y/N")) {
                    changeDate = true;
                }
            } else {
                changeDate = true;
            }
        }
        return changeDate;
    }

    public String getDate() {
        printBreak();
        console.printStringln("Find orders from what date?");
        int year = getYear();
        int month = getMonth();
        int day = getDay(month, year);
        return String.format("%1$04d-%2$02d-%3$02d", year, month, day);
    }

    private int getMonth() {
        return console.getInteger("Please enter the month number (1 = January, 12 = December):", 1, 12);
    }

    private int getDay(int month, int year) {
        int maxDay = 0;
        if (month == 2) {
            maxDay = (year % 4 == 0) ? 29 : 28;
        } else if (month == 4 || month == 6 || month == 9 || month == 11) {
            maxDay = 30;
        } else {
            maxDay = 31;
        }
        return console.getInteger("Please enter the day (" + maxDay + " days in this month):", 1, maxDay);
    }

    private int getYear() {
        return console.getInteger("Please enter the year (1990 - present):", 1990, LocalDate.now().getYear());
    }

    public void printNoOrdersDateMessage(String date) {
        printBreak();
        console.printStringln("There are no orders from " + date + ".");
    }

    public void printNoOrdersFromSearchMessage(String date) {
        printBreak();
        console.printStringln("There are no orders matching your search query from " + date + ".");
    }

    public void printOrders(ArrayList<Order> orderList, String date) {
        printBreak();
        console.printStringln("Orders from " + date);
        console.printStringln("ID    |     Customer Name    | St |  Total   |");
        orderList.stream().forEach(o -> console.printStringln(
                String.format("%1$-5d", o.getOrderID()) + " | "
                + String.format("%1$-20.20s", o.getCustomerName()) + " | "
                + o.getCustomerState() + " | "
                + String.format("%1$-8.2f", o.getTotalCost()) + " |"));
        printBreak();
    }

    public String getSearchString() {
        printBreak();
        String searchString = "";
        if (getYN("There are more than 10 orders on this date. Would you like to refine by name or state?")) {
            searchString = console.getString("Please enter a keyword to refine the results:");
        }
        return searchString;
    }

    public int getOrderSelection(ArrayList<Order> orderList) {
        List<Integer> orderIDs = orderList.stream().map(Order::getOrderID).collect(Collectors.toList());
        int choice = -1;
        while (!orderIDs.contains(choice) && choice != 0) {
            choice = console.getInteger("Please select an order by entering the Order ID, or 0 to return.");
        }
        return choice;
    }

    public void printOrderProcessedMessage() {
        console.printStringln("This order has been processed and cannot be edited. You may only edit orders within the past 7 days.");
    }

    public void printOrder(Order order, String date) {
        printBreak();
        console.printStringln("Orders from " + date);
        console.printStringln("ID    |     Customer Name    | St |  Total   |");
        console.printStringln(
                String.format("%1$-5d", order.getOrderID()) + " | "
                + String.format("%1$-20.20s", order.getCustomerName()) + " | "
                + order.getCustomerState() + " | "
                + String.format("%1$-8.2f", order.getTotalCost()) + " |");
    }

    public void printOrderItems(ArrayList<OrderItem> orderItemList, ArrayList<Item> itemList) {
        if (orderItemList.size() == 0) {
            return;
        }
        printBreak();
        console.printStringln("Prod |   Product  |  Mat. |  Lab. |  Sq  |  Mat   |  Lab   |   Sub   |");
        console.printStringln(" ID  |    Name    | /sqFt | /sqFt |  Ft  |  Cost  |  Cost  |  Total  |");
        orderItemList.stream().forEach(oi -> console.printStringln(
                String.format("%1$-4d", oi.getItemID()) + " | "
                + String.format("%1$-10.10s", itemList.get(oi.getItemID() - 1).getItemName()) + " | "
                + String.format("%1$-5.2f", oi.getMaterialPricePerSqFt()) + " | "
                + String.format("%1$-5.2f", oi.getLaborPricePerSqFt()) + " | "
                + String.format("%1$-4d", oi.getSqFt()) + " | "
                + String.format("%1$-6.2f", oi.getMaterialTotal()) + " | "
                + String.format("%1$-6.2f", oi.getLaborTotal()) + " | "
                + String.format("%1$-7.2f", oi.getSubTotal()) + " |"));
        printBreak();
    }

    public void printOrderItem(OrderItem orderItem, ArrayList<Item> itemList) {
        printBreak();
        console.printStringln("Prod |   Product  |  Mat. |  Lab. |  Sq  |  Mat   |  Lab   |   Sub   |");
        console.printStringln(" ID  |    Name    | /sqFt | /sqFt |  Ft  |  Cost  |  Cost  |  Total  |");
        console.printStringln(
                String.format("%1$-4d", orderItem.getItemID()) + " | "
                + String.format("%1$-10.10s", itemList.get(orderItem.getItemID() - 1).getItemName()) + " | "
                + String.format("%1$-5.2f", orderItem.getMaterialPricePerSqFt()) + " | "
                + String.format("%1$-5.2f", orderItem.getLaborPricePerSqFt()) + " | "
                + String.format("%1$-4d", orderItem.getSqFt()) + " | "
                + String.format("%1$-6.2f", orderItem.getMaterialTotal()) + " | "
                + String.format("%1$-6.2f", orderItem.getLaborTotal()) + " | "
                + String.format("%1$-7.2f", orderItem.getSubTotal()) + " |");
        printBreak();
    }

    public void getEnterToContinue() {
        console.enterToContinue();
    }

    public int getOrderMenuChoice() {
        console.printStringln("1. Edit Order");
        console.printStringln("2. Remove Order");
        console.printStringln("0. Back to Main Menu");
        printBreak();
        return console.getInteger("What would you like to do?", 0, 2);
    }

    public int getEditMenuChoice() {
        console.printStringln("1. Change Customer Name");
        console.printStringln("2. Change Customer State");
        console.printStringln("3. Add Item to Order");
        console.printStringln("4. Remove Item from Order");
        console.printStringln("5. Change Order Item Quantity");
        console.printStringln("0. Return to Main Menu");
        printBreak();
        return console.getInteger("What would you like to do?", 0, 5);
    }

    public String getCustomerName(Order order) {
        String customerName;
        if (order.getCustomerName().isEmpty()) {
            customerName = console.getString("Customer Name :");
        } else {
            customerName = console.getString("Customer Name (" + order.getCustomerName() + ") :");
        }
        return customerName;
    }

    public String getState(Order order, List<String> stateList) {
        String[] states = stateList.toArray(new String[0]);
        String customerState = "";
        console.printString("We currently serve ");
        stateList.stream().forEach(s -> console.printString(s + " "));
        console.printStringln("");
        if (order.getCustomerName().isEmpty()) {
            customerState = console.getString("Customer State :", states);
        } else {
            customerState = console.getString("Customer State (" + order.getCustomerState() + ") :", states);
        }
        return customerState;
    }

    public boolean confirmEdit() {
        return getYN("Confirm Edit? Y/N");
    }

    public void printInventory(ArrayList<Item> itemList) {
        printBreak();
        console.printStringln("Current Inventory");
        console.printStringln("Prod |   Product  |  Mat. |  Lab. |");
        console.printStringln(" ID  |    Name    | /sqFt | /sqFt |");
        itemList.stream().forEach(i -> console.printStringln(
                String.format("%1$-4d", i.getItemID()) + " | "
                + String.format("%1$-10.10s", i.getItemName()) + " | "
                + String.format("%1$-5.2f", i.getMaterialPricePerSqFt()) + " | "
                + String.format("%1$-5.2f", i.getLaborPricePerSqFt()) + " | "));
        printBreak();
    }

    public int getOrderItemID(ArrayList<Item> inventory) {
        return console.getInteger("Product ID: ", 1, inventory.size());
    }

    public int getOrderItemArea() {
        return console.getInteger("Square Feet in Whole Numbers (min - 1, max - 1,000,000: ", 1, 1000000);
    }

    public boolean confirmAddOrderItem() {
        return getYN("Confirm Addition?");
    }

    public int getOrderItemSelection(ArrayList<OrderItem> orderItemList) {
        List<Integer> itemIDs = orderItemList.stream().map(OrderItem::getItemID)
                .collect(Collectors.toList());
        int choice = -1;
        while (!itemIDs.contains(choice) && choice != 0) {
            choice = console.getInteger("Please Enter the Desired Item ID or 0 to return:");
        }
        return choice;
    }

    public boolean confirmRemoveOrderItem() {
        return getYN("Confirm Item Removal?");
    }

    public boolean askToDeleteEmptyOrder() {
        return getYN("There are no longer any items in this order. Delete Order? Y/N");
    }

    public boolean confirmRemoveOrder() {
        return getYN("Are you sure you want to delete this order?");
    }

    public boolean confirmChangeDateToAdd(String currentDate, boolean unsavedChanges) {
        boolean yn = false;
        if (unsavedChanges) {
            yn = getYN("You may only add orders to the current date. "
                + "Any unchanged saves to " + currentDate + " will be lost if you continue. "
                + "Continue? Y/N");
        } else {
            yn = getYN("You may only add orders to the current date. Change working "
                    + "date to today? Y/N");
        }
        return yn;
    }

    public boolean confirmAddAnotherItem() {
        return getYN("Add another item to order? Y/N");
    }

    public boolean confirmAddOrder() {
        return getYN("Confirm this order?");
    }

    public boolean checkExit() {
        if (getYN("Really exit? All unsaved changes will be lost. Y/N")) {
            return true;
        }
        return false;
    }

    private boolean getYN(String prompt) {
        boolean isYes = false;
        if (console.getString(prompt, new String[]{"Y", "N"}).equalsIgnoreCase("Y")) {
            isYes = true;
        }
        return isYes;
    }

    private void printBreak() {
        console.printStringln("===========================================================================");
    }
}
