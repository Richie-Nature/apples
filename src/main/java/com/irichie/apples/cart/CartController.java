package com.irichie.apples.cart;

import com.irichie.apples.product.Product;
import com.irichie.apples.product.ProductService;
import com.irichie.apples.user.User;
import com.irichie.apples.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.stream.IntStream;

@RestController
@RequestMapping("/api/cart")
public class CartController {
    @Autowired
    private CartService cartService;

    @Autowired
    private UserService userService;

    @Autowired
    private ProductService productService;

    @PostMapping("/add")
    public ResponseEntity<Cart> addToCart(@RequestBody CartRequestDTO request) {
        User user = userService.findByUsername(request.getUsername());
        Product product;
        if(user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        try {
            product = productService.find(request.getItemId());
        } catch (UnsupportedOperationException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        Cart cart = user.getCart();
        IntStream.range(0, request.getQuantity())
                .forEach(i -> cart.addItem(product));
        request.getQuantity();
        cartService.update(cart);
        return ResponseEntity.ok(cart);
    }

    @GetMapping("/{username}")
    public ResponseEntity<Cart> getCart(@PathVariable String username) {
        User user = userService.findByUsername(username);
        if(user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(cartService.findByUser(user));
    }

    @PostMapping("/remove")
    public ResponseEntity<Cart> removeFromCart(@RequestBody CartRequestDTO request) {
        User user = userService.findByUsername(request.getUsername());
        Product product;
        if(user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        try {
            product = productService.find(request.getItemId());
        } catch (UnsupportedOperationException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        Cart cart = user.getCart();
        IntStream.range(0, request.getQuantity())
                .forEach(i -> cart.removeItem(product));
        cartService.update(cart);
        return ResponseEntity.ok(cart);
    }
}
