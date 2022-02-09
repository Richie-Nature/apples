package com.irichie.apples.user;

import com.irichie.apples.cart.Cart;
import com.irichie.apples.cart.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private CartService cartService;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @GetMapping("/id/{id}")
    public ResponseEntity<User> findById(@PathVariable Long id) {
        User user;
        try {
            user = userService.find(id);
        } catch (UnsupportedOperationException e) {
          return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(user);
    }

    @GetMapping("/{username}")
    public ResponseEntity<User> findByUserName(@PathVariable String username) {
        User user = userService.findByUsername(username);
        return user == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(user);
    }

    @PostMapping("/create")
    public ResponseEntity<User> createUser(@RequestBody UserRequestDTO request) {
        if(!isReqValid(request)) {
            return ResponseEntity.badRequest().build();
        }
//        logger.info("{} attempts to create account", user.getUsername());

        User user = new User();
        user.setUsername(request.getUsername());
        Cart cart = new Cart();
        cartService.create(cart);
        user.setCart(cart);
        user.setPassword(bCryptPasswordEncoder.encode(request.getPassword()));
        userService.create(user);
        return ResponseEntity.ok(user);
    }

    private static Boolean isReqValid(UserRequestDTO request) {
        if(!new UserRequestDTO().equals(request)) {
            System.out.println("Enter username, password & setPassword");
            return false;
        }

        if(request.getPassword().length() < 8 ||
                !request.getPassword().equals(request.getConfirmPassword())) {
            System.out.println("Invalid password");
            return false;
        }

        return true;
    }
}
