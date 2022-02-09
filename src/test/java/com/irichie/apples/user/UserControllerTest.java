package com.irichie.apples.user;

import com.irichie.apples.cart.CartService;
import com.irichie.apples.util.TestUtils;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserControllerTest {

    private UserController userController;
    private UserService userService = mock(UserService.class);
    private CartService cartService = mock(CartService.class);
    private BCryptPasswordEncoder encoder = mock(BCryptPasswordEncoder.class);

    private static User user;

    @BeforeClass
    public static void init() {
        String userName = "testUser";
        String password = "testPassword";
        user = new User();
        user.setPassword(password);
        user.setUsername(userName);
    }

    @Before
    public void setUp() {
        userController = new UserController();
        TestUtils.injectObjects(userController, "userService", userService);
        TestUtils.injectObjects(userController, "cartService", cartService);
        TestUtils.injectObjects(userController, "bCryptPasswordEncoder", encoder);
    }

    @Test
    public void create_user_happy_path() throws Exception { //happy path convention for sanity tests
        when(encoder.encode(user.getPassword())).thenReturn("HashedSuccessFully"); //stubbing

        UserRequestDTO userRequest = createUserRequest();
        final ResponseEntity<User> response = userController.createUser(userRequest);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        User savedUser = response.getBody();

        assertNotNull(savedUser);
        assertEquals(user.getUsername(), savedUser.getUsername());
        assertEquals("HashedSuccessFully", savedUser.getPassword());
    }

    @Test
    public void verify_create_user_unhappy_path() {
        UserRequestDTO userRequest = createUserRequest();
        userRequest.setConfirmPassword("badPwd");

        final ResponseEntity<User> response = userController.createUser(userRequest);
        assertEquals(400, response.getStatusCodeValue());
    }


    @Test
    public void verify_find_by_username() throws Exception{
        UserRequestDTO userRequest = createUserRequest();
        final ResponseEntity<User> userResponse = userController.createUser(userRequest);
        User savedUser = userResponse.getBody();

        when(userService.findByUsername(savedUser.getUsername())).thenReturn(savedUser);

        ResponseEntity<User> uNameResponse = userController.findByUserName(savedUser.getUsername());
        User returnedUser = uNameResponse.getBody();

        assertNotNull(returnedUser);
        assertEquals(savedUser.getUsername(), returnedUser.getUsername());
        assertNotEquals(user.getPassword(), returnedUser.getPassword());
    }

    @Test
    public void verify_find_by_id() throws Exception {
        UserRequestDTO userRequest = createUserRequest();
        final ResponseEntity<User> userResponse = userController.createUser(userRequest);
        User savedUser = userResponse.getBody();


        when(userService.find(savedUser.getId())).thenReturn(savedUser);

        ResponseEntity<User> uNameResponse = userController.findById(savedUser.getId());
        User returnedUser = uNameResponse.getBody();

        assertNotNull(returnedUser);
        assertEquals(savedUser.getUsername(), returnedUser.getUsername());
        assertNotEquals(user.getPassword(), returnedUser.getPassword());
    }

    private UserRequestDTO createUserRequest() {
        UserRequestDTO userRequest = new UserRequestDTO();
        userRequest.setUsername(user.getUsername());
        userRequest.setPassword(user.getPassword());
        userRequest.setConfirmPassword(user.getPassword());

        return userRequest;
    }
}
