package com.irichie.apples.cart;

import com.irichie.apples.user.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository extends CrudRepository<Cart, Long> {
    Cart findByUser(User user);
}
