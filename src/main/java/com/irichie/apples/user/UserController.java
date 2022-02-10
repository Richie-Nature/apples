package com.irichie.apples.user;

import com.irichie.apples.cart.Cart;
import com.irichie.apples.cart.CartService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/user")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

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
            logger.error("User not found: {}",e.getMessage());
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
            logger.error("{} account creation failed. Bad request", request.getUsername());
            return ResponseEntity.badRequest().build();
        }

        User user = new User();
        Cart cart = new Cart();
        cartService.create(cart);


        user.setUsername(request.getUsername());
        user.setCart(cart);
        user.setPassword(bCryptPasswordEncoder.encode(request.getPassword()));

        User savedUser = userService.create(user);
        logger.info("{} created account successfully", user.getUsername());
        return ResponseEntity.ok(user);
    }

    private static Boolean isReqValid(UserRequestDTO request) {
        if(!new UserRequestDTO().equals(request)) {
            logger.error("Improperly formed user request");
            return false;
        }

        if(request.getPassword().length() < 8 ||
                !request.getPassword().equals(request.getConfirmPassword())) {
            logger.error("Invalid password");
            return false;
        }

        return true;
    }
}
