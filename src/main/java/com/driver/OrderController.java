package com.driver;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("orders")
public class OrderController {

    @Autowired
    OrderService orderService;

    @PostMapping("/add-order")
    public ResponseEntity<String> addOrder(@RequestBody Order order) {
        try {
            orderService.addOrder(order);
            return new ResponseEntity<>("New order added successfully", HttpStatus.CREATED);

        } catch (Exception e) {
            throw new NullPointerException();
        }

    }

    @PostMapping("/add-partner/{partnerId}")
    public ResponseEntity<String> addPartner(@PathVariable String partnerId) {
        try {
            orderService.addPartner(partnerId);
            return new ResponseEntity<>("New delivery partner added successfully", HttpStatus.CREATED);
        } catch (Exception e) {
            throw new NullPointerException();
        }

    }

    @PutMapping("/add-order-partner-pair")
    public ResponseEntity<String> addOrderPartnerPair(@RequestParam String orderId, @RequestParam String partnerId) {
        // This is basically assigning that order to that partnerId
        try {
            orderService.addOrderPartnerPair(orderId, partnerId);
            return new ResponseEntity<>("New order-partner pair added successfully", HttpStatus.CREATED);
        } catch (Exception e) {
            throw new NullPointerException();
        }

    }

    @GetMapping("/get-order-by-id/{orderId}")
    public ResponseEntity<Order> getOrderById(@PathVariable String orderId) {
        // order should be returned with an orderId.
        try {
            Order order = orderService.getOrderById(orderId);
            return new ResponseEntity<>(order, HttpStatus.CREATED);
        } catch (Exception e) {
            throw new NullPointerException();
        }

    }

    @GetMapping("/get-partner-by-id/{partnerId}")
    public ResponseEntity<DeliveryPartner> getPartnerById(@PathVariable String partnerId) {
        // deliveryPartner should contain the value given by partnerId
        try {
            DeliveryPartner deliveryPartner = orderService.getPartnerById(partnerId);
            return new ResponseEntity<>(deliveryPartner, HttpStatus.CREATED);
        } catch (Exception e) {
            throw new NullPointerException();
        }

    }

    @GetMapping("/get-order-count-by-partner-id/{partnerId}")
    public ResponseEntity<Integer> getOrderCountByPartnerId(@PathVariable String partnerId) {
        // orderCount should denote the orders given by a partner-id
        try {
            Integer orderCount = orderService.getOrderCountByPartnerId(partnerId);
            return new ResponseEntity<>(orderCount, HttpStatus.CREATED);
        } catch (Exception e) {
            throw new NullPointerException();
        }

    }

    @GetMapping("/get-orders-by-partner-id/{partnerId}")
    public ResponseEntity<List<String>> getOrdersByPartnerId(@PathVariable String partnerId) {
        // orders should contain a list of orders by PartnerId
        try {
            List<String> orders = orderService.getOrdersByPartnerId(partnerId);
            return new ResponseEntity<>(orders, HttpStatus.CREATED);
        } catch (Exception e) {
            throw new NullPointerException();
        }

    }

    @GetMapping("/get-all-orders")
    public ResponseEntity<List<String>> getAllOrders() {
        // Get all orders
        try {
            List<String> orders = orderService.getAllOrders();
            return new ResponseEntity<>(orders, HttpStatus.CREATED);
        } catch (Exception e) {
            throw new NullPointerException();
        }

    }

    @GetMapping("/get-count-of-unassigned-orders")
    public ResponseEntity<Integer> getCountOfUnassignedOrders() {
        // Count of orders that have not been assigned to any DeliveryPartner
        try {
            Integer countOfOrders = orderService.getCountOfUnassignedOrders();
            return new ResponseEntity<>(countOfOrders, HttpStatus.CREATED);
        } catch (Exception e) {
            throw new NullPointerException();
        }

    }

    @GetMapping("/get-count-of-orders-left-after-given-time/{partnerId}")
    public ResponseEntity<Integer> getOrdersLeftAfterGivenTimeByPartnerId(@PathVariable String time,
            @PathVariable String partnerId) {
        // countOfOrders that are left after a particular time of a DeliveryPartner
        try {
            Integer countOfOrders = orderService.getOrdersLeftAfterGivenTimeByPartnerId(time, partnerId);
            return new ResponseEntity<>(countOfOrders, HttpStatus.CREATED);
        } catch (Exception e) {
            throw new NullPointerException();
        }

    }

    @GetMapping("/get-last-delivery-time/{partnerId}")
    public ResponseEntity<String> getLastDeliveryTimeByPartnerId(@PathVariable String partnerId) {
        // Return the time when that partnerId will deliver his last delivery order.
        try {
            String time = orderService.getLastDeliveryTimeByPartnerId(partnerId);
            return new ResponseEntity<>(time, HttpStatus.CREATED);
        } catch (Exception e) {
            throw new NullPointerException();
        }
    }

    @DeleteMapping("/delete-partner-by-id/{partnerId}")
    public ResponseEntity<String> deletePartnerById(@PathVariable String partnerId) {
        // Delete the partnerId
        // And push all his assigned orders to unassigned orders.
        try {
            orderService.deleteOrderById(partnerId);
            return new ResponseEntity<>(partnerId + " removed successfully", HttpStatus.CREATED);
        } catch (Exception e) {
            throw new NullPointerException();
        }

    }

    @DeleteMapping("/delete-order-by-id/{orderId}")
    public ResponseEntity<String> deleteOrderById(@PathVariable String orderId) {
        // Delete an order and also
        // remove it from the assigned order of that partnerId
        try {
            orderService.deleteOrderById(orderId);
            return new ResponseEntity<>(orderId + " removed successfully", HttpStatus.CREATED);
        } catch (Exception e) {
            throw new NullPointerException();
        }

    }
}
