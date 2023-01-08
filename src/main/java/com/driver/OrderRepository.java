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
        try {
            orderDb.put(order.getId(), order);
            return "Added";
        } catch (Exception e) {
            throw new NullPointerException();
        }

    }

    public String addPartner(String partnerId) {
        try {
            DeliveryPartner partner = new DeliveryPartner(partnerId);
            partnerDb.put(partnerId, partner);
            return "Added";
        } catch (Exception e) {
            throw new NullPointerException();
        }
    }

    public String addOrderPartnerPair(String orderId, String partnerId) {
        // This is basically assigning that order to that partnerId
        try {
            List<String> list = pairDb.getOrDefault(partnerId, new ArrayList<>());
            list.add(orderId);
            assignedDb.put(orderId, partnerId);
            return "Added";
        } catch (Exception e) {
            throw new NullPointerException();
        }

    }

    public Order getOrderById(String orderId) {
        // order should be returned with an orderId.
        try {
            for (String s : orderDb.keySet()) {
                if (s.equals(orderId)) {
                    return orderDb.get(s);
                }
            }
            return null;
        } catch (Exception e) {
            throw new NullPointerException();
        }

    }

    public DeliveryPartner getPartnerById(String partnerId) {
        // deliveryPartner should contain the value given by partnerId
        try {
            if (partnerDb.containsKey(partnerId)) {
                return partnerDb.get(partnerId);
            }
            return null;
        } catch (Exception e) {
            throw new NullPointerException();
        }

    }

    public Integer getOrderCountByPartnerId(String partnerId) {
        // orderCount should denote the orders given by a partner-id
        try {
            Integer orders = pairDb.getOrDefault(partnerId, new ArrayList<>()).size();
            return orders;
        } catch (Exception e) {
            throw new NullPointerException();
        }
    }

    public List<String> getOrdersByPartnerId(String partnerId) {
        // orders should contain a list of orders by PartnerId
        try {
            List<String> orders = pairDb.getOrDefault(partnerId, new ArrayList<>());
            return orders;
        } catch (Exception e) {
            throw new NullPointerException();
        }

    }

    public List<String> getAllOrders() {
        // Get all orders
        try {
            List<String> orders = new ArrayList<>();
            for (String s : orderDb.keySet()) {
                orders.add(s);
            }
            return orders;
        } catch (Exception e) {
            throw new NullPointerException();
        }

    }

    public Integer getCountOfUnassignedOrders() {
        // Count of orders that have not been assigned to any DeliveryPartner
        try {
            Integer countOfOrders = 0;
            for (String s : orderDb.keySet()) {
                if (!assignedDb.containsKey(s)) {
                    countOfOrders++;
                }
            }
            return countOfOrders;
        } catch (Exception e) {
            throw new NullPointerException();
        }

    }

    public Integer getOrdersLeftAfterGivenTimeByPartnerId(String time, String partnerId) {
        // countOfOrders that are left after a particular time of a DeliveryPartner
        try {
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
        } catch (Exception e) {
            throw new NullPointerException();
        }

    }

    public String getLastDeliveryTimeByPartnerId(String partnerId) {
        // Return the time when that partnerId will deliver his last delivery order.
        try {
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
        } catch (Exception e) {
            throw new NullPointerException();
        }

    }

    public String deletePartnerById(String partnerId) {
        // Delete the partnerId
        // And push all his assigned orders to unassigned orders.
        try {
            partnerDb.remove(partnerId);
            List<String> list = pairDb.get(partnerId);
            for (String s : list) {
                assignedDb.remove(s);
            }
            pairDb.remove(partnerId);
            return "Deleted";
        } catch (Exception e) {
            throw new NullPointerException();
        }

    }

    public String deleteOrderById(String orderId) {

        // Delete an order and also
        // remove it from the assigned order of that partnerId
        try {
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
        } catch (Exception e) {
            throw new NullPointerException();
        }

    }
}
