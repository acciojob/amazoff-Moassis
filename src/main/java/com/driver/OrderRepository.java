package com.driver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;

import org.springframework.stereotype.Repository;

@Repository
public class OrderRepository {

    HashMap<String, Order> orderDb = new HashMap<>();
    HashMap<String, DeliveryPartner> partnerDb = new HashMap<>();
    HashMap<String, List<String>> pairDb = new HashMap<>();
    HashMap<String, String> assignedDb = new HashMap<>(); // <orderId, partnerId>

    public String addOrder(Order order) {
        orderDb.put(order.getId(), order);
        return "Added";
    }

    public String addPartner(String partnerId) {
        DeliveryPartner partner = new DeliveryPartner(partnerId);
        partnerDb.put(partnerId, partner);
        return "Added";
    }

    public String addOrderPartnerPair(String orderId, String partnerId) {
        // This is basically assigning that order to that partnerId
        List<String> list = pairDb.getOrDefault(partnerId, new ArrayList<>());
        list.add(orderId);
        assignedDb.put(orderId, partnerId);
        return "Added";
    }

    public Order getOrderById(String orderId) {
        // order should be returned with an orderId.
        for (String s : orderDb.keySet()) {
            if (s.equals(orderId)) {
                return orderDb.get(s);
            }
        }
        return null;
    }

    public DeliveryPartner getPartnerById(String partnerId) {
        // deliveryPartner should contain the value given by partnerId
        if (partnerDb.containsKey(partnerId)) {
            return partnerDb.get(partnerId);
        }
        return null;
    }

    public Integer getOrderCountByPartnerId(String partnerId) {
        // orderCount should denote the orders given by a partner-id
        Integer orders = pairDb.getOrDefault(partnerId, new ArrayList<>()).size();
        return orders;
    }

    public List<String> getOrdersByPartnerId(String partnerId) {
        // orders should contain a list of orders by PartnerId
        List<String> orders = pairDb.getOrDefault(partnerId, new ArrayList<>());
        return orders;
    }

    public List<String> getAllOrders() {
        // Get all orders
        List<String> orders = new ArrayList<>();
        for (String s : orderDb.keySet()) {
            orders.add(s);
        }
        return orders;
    }

    public Integer getCountOfUnassignedOrders() {
        // Count of orders that have not been assigned to any DeliveryPartner
        Integer countOfOrders = 0;
        for (String s : orderDb.keySet()) {
            if (!assignedDb.containsKey(s)) {
                countOfOrders++;
            }
        }
        return countOfOrders;
    }

    public Integer getOrdersLeftAfterGivenTimeByPartnerId(String time, String partnerId) {
        // countOfOrders that are left after a particular time of a DeliveryPartner
        Integer countOfOrders = 0;
        List<String> list = pairDb.get(partnerId);
        int deliveryTime = Integer.parseInt(time.substring(0, 2)) * 60 + Integer.parseInt(time.substring(3));
        for (String s : list) {
            Order order = orderDb.get(s);
            if (order.getDeliveryTime() > deliveryTime) {
                countOfOrders++;
            }
        }
        return countOfOrders;
    }

    public String getLastDeliveryTimeByPartnerId(String partnerId) {
        // Return the time when that partnerId will deliver his last delivery order.
        String time = "";
        List<String> list = pairDb.get(partnerId);
        int deliveryTime = 0;
        for (String s : list) {
            Order order = orderDb.get(s);
            deliveryTime = Math.max(deliveryTime, order.getDeliveryTime());
        }
        int hour = deliveryTime / 60;
        int min = deliveryTime % 60;
        time = String.valueOf(hour) + ":" + String.valueOf(min);

        return time;
    }

    public String deletePartnerById(String partnerId) {
        // Delete the partnerId
        // And push all his assigned orders to unassigned orders.
        partnerDb.remove(partnerId);
        List<String> list = pairDb.get(partnerId);
        for (String s : list) {
            assignedDb.remove(s);
        }
        pairDb.remove(partnerId);
        return "Deleted";
    }

    public String deleteOrderById(String orderId) {

        // Delete an order and also
        // remove it from the assigned order of that partnerId
        orderDb.remove(orderId);
        String partnerId = assignedDb.get(orderId);
        assignedDb.remove(orderId);
        List<String> list = pairDb.get(partnerId);

        ListIterator<String> itr = list.listIterator();
        while (itr.hasNext()) {
            String s = itr.next();
            if (s.equals(orderId)) {
                itr.remove();
            }
        }
        pairDb.put(partnerId, list);

        return "Deleted";
    }
}
