package com.irichie.apples.order;

import com.irichie.apples.user.User;
import com.irichie.apples.user.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);
    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

    @PostMapping("/new/{username}")
    public ResponseEntity<Order> createOrder(@PathVariable String username) {
        User user = userService.findByUsername(username);
        if(user == null) {
            logger.error("Order creation failed: username {} not found", username);
            return ResponseEntity.notFound().build();
        }
        Order order = Order.createFromCart(user.getCart());
        orderService.create(order);
        logger.info("Order created successfully: {}",username);
        return ResponseEntity.ok(order);
    }

    @GetMapping("/history/{username}")
    public ResponseEntity<List<Order>> getOrdersForUser(@PathVariable String username) {
        User user = userService.findByUsername(username);
        if(user == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(orderService.findByUser(user));
    }
}
