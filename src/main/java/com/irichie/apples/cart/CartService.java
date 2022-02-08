package com.irichie.apples.cart;

import com.irichie.apples.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service
public class CartService {
    @Autowired
    private CartRepository cartRepository;

    public Cart create(Cart cart) {
        return cartRepository.save(cart);
    }

    public List<Cart> findAll() {
        return (List<Cart>) cartRepository.findAll();
    }

    public Cart findByUser(User user) {
        return cartRepository.findByUser(user);
    }

    public Cart update(Cart cart) {
        return cartRepository.findById(cart.getId())
                .map(c -> cartRepository.save(c))
                .orElseThrow(UnsupportedOperationException::new);
    }
}
