package com.irichie.apples.cart;

import com.irichie.apples.product.Product;
import com.irichie.apples.product.ProductService;
import com.irichie.apples.user.User;
import com.irichie.apples.user.UserService;
import com.irichie.apples.util.TestUtils;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CartControllerTest {
    private CartController cartController;

    private CartService cartService = mock(CartService.class);
    private UserService userService = mock(UserService.class);
    private ProductService productService = mock(ProductService.class);

    private static Cart cart;
    private static User user;
    private static Product product;
    private static CartRequestDTO requestDTO;

    @BeforeClass
    public static void init() {
        user = new User();
        user.setUsername("testUser");
        user.setId(1L);

        product = new Product();
        product.setPrice(BigDecimal.valueOf(2.99));
        product.setDescription("a test product");
        product.setName("test product");
        product.setId(1L);

        cart = new Cart();
        cart.setId(1L);
        cart.setProducts(new ArrayList<>());
        cart.setTotal(product.getPrice());
        cart.setUser(user);

        user.setCart(cart);

        requestDTO = new CartRequestDTO();
        requestDTO.setItemId(product.getId());
        requestDTO.setQuantity(2);
        requestDTO.setUsername(user.getUsername());
    }

    @Before
    public void setup() {
        cartController = new CartController();

        TestUtils.injectObjects(cartController, "userService", userService);
        TestUtils.injectObjects(cartController, "cartService", cartService);
        TestUtils.injectObjects(cartController, "productService", productService);

        when(userService.findByUsername(user.getUsername())).thenReturn(user);
        when(userService.findByUsername(argThat(n -> !n.equals(user.getUsername())))).thenReturn(null);
        when(productService.find(product.getId())).thenReturn(product);
        when(productService.find(argThat(aLong -> aLong != user.getId()))).thenThrow(UnsupportedOperationException.class);
        when(cartService.findByUser(user)).thenReturn(cart);
    }

    @Test
    public void add_to_cart_happy_path()throws Exception {
        final ResponseEntity<Cart> response = cartController.addToCart(requestDTO);

        assertNotNull(response);
        assertEquals(cart.getId(), response.getBody().getId());
    }

    @Test
    public void add_to_cart_unhappy_path()throws Exception {
        requestDTO.setUsername("fakeUsername");
        final ResponseEntity<Cart> response = cartController.addToCart(requestDTO);

        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());

        requestDTO.setUsername(user.getUsername());
        requestDTO.setItemId(5);
        final ResponseEntity<Cart> response2 = cartController.addToCart(requestDTO);

        assertEquals(404, response2.getStatusCodeValue());
    }

    @Test
    public void get_cart_happy_path() throws Exception{}

    @Test
    public void get_cart_unhappy_path()throws Exception {}

    @Test
    public void remove_from_cart_happy_path() throws Exception {}

    @Test
    public void remove_from_cart_unhappy_path() throws Exception {}
}
