package com.irichie.apples.order;




import com.irichie.apples.cart.Cart;
import com.irichie.apples.product.Product;
import com.irichie.apples.user.User;
import com.irichie.apples.user.UserController;
import com.irichie.apples.user.UserService;
import com.irichie.apples.util.TestUtils;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OrderControllerTest {
    private OrderController orderController;
    private OrderService orderService = mock(OrderService.class);
    private UserService userService = mock(UserService.class);

    private static User user;
    private static Cart cart;

    @BeforeClass
    public static void init() {
        user = new User();
        user.setUsername("testUser");

        cart = new Cart();
        cart.setId(1L);
        cart.setProducts(Collections.singletonList(new Product()));
        cart.setTotal(BigDecimal.valueOf(0.00));
        cart.setUser(user);

        user.setCart(cart);
    }

    @Before
    public void setUp() {
        orderController = new OrderController();
        TestUtils.injectObjects(orderController, "userService", userService);
        TestUtils.injectObjects(orderController, "orderService", orderService);

        when(userService.findByUsername(user.getUsername())).thenReturn(user);
    }

    @Test
    public void create_order_happy_path() throws Exception {
        final ResponseEntity<Order> response = orderController.createOrder(user.getUsername());

        assertNotNull(response);
        assertEquals(cart.getTotal(), response.getBody().getTotal());
    }

    @Test
    public void create_order_unhappy_path() throws Exception {
        final ResponseEntity<Order> response = orderController.createOrder("fakeUser");

        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void get_user_history_happy_path() throws Exception {
        final ResponseEntity<List<Order>> response =
                orderController.getOrdersForUser(user.getUsername());

        assertNotNull(response);
        assertEquals(200,response.getStatusCodeValue());
    }

    @Test
    public void get_user_history_unhappy_path() throws Exception {
        final ResponseEntity<List<Order>> response =
                orderController.getOrdersForUser("fakeUser");

        assertEquals(404,response.getStatusCodeValue());
    }

    private static Order createFromCart(Cart cart) {
        Order order = new Order();
        order.setProducts(cart.getProducts().stream().collect(Collectors.toList()));
        order.setTotal(cart.getTotal());
        order.setUser(cart.getUser());
        return order;
    }
}
