package com.irichie.apples.order;

import com.irichie.apples.user.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends CrudRepository<Order, Long> {
    public List<Order> findByUser(User user);
}
