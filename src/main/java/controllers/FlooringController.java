/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import daos.ItemDAO;
import daos.OrderDAO;
import daos.OrderItemDAO;
import daos.TaxDAO;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import models.Order;
import models.OrderItem;
import ui.UserInterface;

/**
 *
 * @author apprentice
 */
public class FlooringController {

    private UserInterface ui;
    private ItemDAO inventory;
    private TaxDAO taxRates;
    private OrderDAO orders;
    private OrderItemDAO orderItems;
    private String currentDate;
    private DecimalFormat df;
    private boolean unsavedChanges;

    public FlooringController(OrderDAO orders, OrderItemDAO orderItems,
            TaxDAO taxRates, ItemDAO inventory) {
        ui = new UserInterface();
        this.orders = orders;
        this.orderItems = orderItems;
        this.taxRates = taxRates;
        this.inventory = inventory;
        df = new DecimalFormat("#.##");
        currentDate = LocalDate.now().toString();
        unsavedChanges = false;
    }

    public void startFlooring() {
        mainMenu();
    }

    private void mainMenu() {
        while (true) {
            int choice = ui.getMainMenuChoice(currentDate);
            switch (choice) {
                case 1:
                    findOrder();
                    break;
                case 2:
                    addOrder();
                    break;
                case 3:
                    orders.save();
                    orderItems.save();
                    unsavedChanges = false;
                    break;
                case 0:
                    if (ui.checkExit()) {
                        System.exit(0);
                    }
                    break;
            }
        }
    }

    private void findOrder() {
        // Asks if the user wants to change the date or stay with current date. 
        // Also warns about unsaved data.
        if (ui.changeDate(currentDate, unsavedChanges)) {
            currentDate = ui.getDate();
        } 
        ArrayList<Order> ordersFromDate = orders.getOrdersFromDate(currentDate);
        // Gives user option to refine searches if results > 10
        if (ordersFromDate.size() > 10) {
            ordersFromDate = orders.searchOrdersByNameState(ui.getSearchString());
            if (ordersFromDate.isEmpty()) {
                ui.printNoOrdersFromSearchMessage(currentDate);
                return;
            }
        }
        if (ordersFromDate.isEmpty()) {
            ui.printNoOrdersDateMessage(currentDate);
            return;
        }
        ui.printOrders(ordersFromDate, currentDate);
        selectOrder(ordersFromDate);
    }

    private void selectOrder(ArrayList<Order> orderList) {
        int orderID = ui.getOrderSelection(orderList);
        if (orderID == 0) {
            return;
        }
        Order selectedOrder = orders.getOrderWithID(orderID);
        checkOrderDate(selectedOrder);
    }

    private void checkOrderDate(Order order) {
        LocalDate today = LocalDate.now();
        LocalDate selectedDate = LocalDate.parse(currentDate);
        Period p = selectedDate.until(today);
        // If order older than 7 days, can't edit
        if (p.getDays() > 7) {
            orderMenuOld(order);
        } else {
            orderMenu(order);
        }
    }

    private void orderMenuOld(Order order) {
        ui.printOrderProcessedMessage();
        ui.printOrder(order, currentDate);
        ui.printOrderItems(orderItems.getOrderItems(currentDate, order.getOrderID()), inventory.getAllItems());
        ui.getEnterToContinue();
    }

    private void orderMenu(Order order) {
        ui.printOrder(order, currentDate);
        ui.printOrderItems(orderItems.getOrderItems(currentDate, order.getOrderID()), inventory.getAllItems());
        switch (ui.getOrderMenuChoice()) {
            case 1:
                editMenu(order);
                break;
            case 2:
                removeOrder(order);
                break;
            case 0:
                return;
        }
    }

    private void editMenu(Order order) {
        int choice = -1;
        while (choice != 0) {
            ui.printOrder(order, currentDate);
            ui.printOrderItems(orderItems.getOrderItems(currentDate, order.getOrderID()), inventory.getAllItems());
            choice = ui.getEditMenuChoice();
            switch (choice) {
                case 1:
                    editCustomerName(order);
                    break;
                case 2:
                    editCustomerState(order);
                    break;
                case 3:
                    addOrderItem(order);
                    break;
                case 4:
                    removeOrderItem(order);
                    if (askToDeleteEmptyOrder(order)) {
                        choice = 0;
                    }
                    break;
                case 5:
                    selectOrderItem(order);
                    break;
                case 0:
                    return;
            }
        }
    }

    private void editCustomerName(Order order) {
        String oldName = order.getCustomerName();
        String newName = ui.getCustomerName(order);
        order.setCustomerName(newName);
        if (!confirmOrderEdit(order)) {
            order.setCustomerName(oldName);
        }
    }

    private void editCustomerState(Order order) {
        String oldState = order.getCustomerState();
        String newState = ui.getState(order, taxRates.getAllStates());
        order.setCustomerState(newState);
        if (!confirmOrderEdit(order)) {
            order.setCustomerState(oldState);
        }
        updateOrderTotal(order);
    }

    private boolean confirmOrderEdit(Order order) {
        ui.printOrder(order, currentDate);
        if (ui.confirmEdit()) {
            orders.updateOrder(currentDate, order);
            unsavedChanges = true;
            return true;
        }
        return false;
    }

    private void addOrderItem(Order order) {
        ui.printInventory(inventory.getAllItems());
        int itemID = ui.getOrderItemID(inventory.getAllItems());
        OrderItem currentOrderItem = new OrderItem(order.getOrderID(), itemID, inventory.getAllItems());
        currentOrderItem.setSqFt(ui.getOrderItemArea());
        confirmAddOrderItem(currentOrderItem);
        updateOrderTotal(order);
    }

    private void confirmAddOrderItem(OrderItem orderItem) {
        ui.printOrderItem(orderItem, inventory.getAllItems());
        if (ui.confirmAddOrderItem()) {
            orderItems.addOrderItem(currentDate, orderItem);
            unsavedChanges = true;
        }
    }

    private void removeOrderItem(Order order) {
        ArrayList<OrderItem> orderItemList = orderItems.getOrderItems(currentDate, order.getOrderID());
        ui.printOrderItems(orderItemList, inventory.getAllItems());
        int itemIDToRemove = ui.getOrderItemSelection(orderItemList);
        if (itemIDToRemove == 0) {
            return;
        }
        OrderItem itemToRemove = orderItems.getOrderItem(currentDate, order.getOrderID(), itemIDToRemove);
        confirmRemoveOrderItem(itemToRemove);
        updateOrderTotal(order);
    }

    private void confirmRemoveOrderItem(OrderItem orderItem) {
        ui.printOrderItem(orderItem, inventory.getAllItems());
        if (ui.confirmRemoveOrderItem()) {
            orderItems.removeOrderItem(currentDate, orderItem);
            unsavedChanges = true;
        }
    }

    private boolean askToDeleteEmptyOrder(Order order) {
        if (orderItems.getOrderItems(currentDate, order.getOrderID()).isEmpty()) {
            ui.printOrder(order, currentDate);
            if (ui.askToDeleteEmptyOrder()) {
                removeOrder(order);
                return true;
            }
        }
        return false;
    }

    private void selectOrderItem(Order order) {
        ArrayList<OrderItem> orderItemList = orderItems.getOrderItems(currentDate, order.getOrderID());
        ui.printOrderItems(orderItemList, inventory.getAllItems());
        int itemIDToEdit = ui.getOrderItemSelection(orderItemList);
        if (itemIDToEdit == 0) {
            return;
        }
        editOrderItem(order, itemIDToEdit);
    }

    private void editOrderItem(Order order, int itemIDToEdit) {
        OrderItem itemToEdit = orderItems.getOrderItem(currentDate, order.getOrderID(), itemIDToEdit);
        itemToEdit.setSqFt(ui.getOrderItemArea());
        confirmOrderItemEdit(itemToEdit);
        updateOrderTotal(order);
    }

    private void confirmOrderItemEdit(OrderItem orderItem) {
        ui.printOrderItem(orderItem, inventory.getAllItems());
        if (ui.confirmEdit()) {
            orderItems.updateOrderItem(currentDate, orderItem);
            unsavedChanges = true;
        }
    }

    private void removeOrder(Order order) {
        ui.printOrder(order, currentDate);
        ui.printOrderItems(orderItems.getOrderItems(currentDate, order.getOrderID()), inventory.getAllItems());
        if (ui.confirmRemoveOrder()) {
            orderItems.removeOrder(currentDate, order.getOrderID());
            orders.removeOrder(currentDate, order);
            unsavedChanges = true;
        }
    }

    private void addOrder() {
        if (!currentDate.equals(LocalDate.now().toString())) {
            if (!ui.confirmChangeDateToAdd(currentDate, unsavedChanges)) {
                return;
            }
        }
        currentDate = LocalDate.now().toString();
        Order currentOrder = new Order();
        currentOrder.setOrderID(orders.assignID(currentDate));
        do {
            addOrderItemNewOrder(currentOrder);
        } while (ui.confirmAddAnotherItem());
        currentOrder.setCustomerName(ui.getCustomerName(currentOrder));
        currentOrder.setCustomerState(ui.getState(currentOrder, taxRates.getAllStates()));
        currentOrder.setTaxRate(taxRates.getRate(currentOrder.getCustomerState()));
        updateOrderTotal(currentOrder);
        ui.printOrder(currentOrder, currentDate);
        ui.printOrderItems(orderItems.getOrderItems(currentDate, currentOrder.getOrderID()), inventory.getAllItems());
        confirmAddOrder(currentOrder);
    }

    private void updateOrderTotal(Order order) {
        // Get subtotal
        double orderSubtotal = orderItems.getOrderItemsSubTotal(currentDate, order.getOrderID());
        // Add tax
        order.setTotalCost(Double.valueOf(df.format((orderSubtotal) * (1 + order.getTaxRate() / 100))));
        orders.updateOrder(currentDate, order);
    }

    private void addOrderItemNewOrder(Order order) {
        ui.printInventory(inventory.getAllItems());
        int itemID = ui.getOrderItemID(inventory.getAllItems());
        OrderItem currentOrderItem = new OrderItem(order.getOrderID(), itemID, inventory.getAllItems());
        currentOrderItem.setSqFt(ui.getOrderItemArea());
        confirmAddOrderItemNewOrder(currentOrderItem);
        updateOrderTotal(order);
    }

    private void confirmAddOrderItemNewOrder(OrderItem orderItem) {
        ui.printOrderItem(orderItem, inventory.getAllItems());
        if (ui.confirmAddOrderItem()) {
            orderItems.addOrderItem(currentDate, orderItem);
        }
    }

    private void confirmAddOrder(Order order) {
        ui.printOrder(order, currentDate);
        ui.printOrderItems(orderItems.getOrderItems(currentDate, order.getOrderID()), inventory.getAllItems());
        if (ui.confirmAddOrder()) {
            orders.addOrder(currentDate, order);
            unsavedChanges = true;
        } else {
            orderItems.removeOrder(currentDate, order.getOrderID());
        }
    }

}
