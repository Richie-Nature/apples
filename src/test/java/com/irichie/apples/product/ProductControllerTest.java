package com.irichie.apples.product;

import com.irichie.apples.order.Order;
import com.irichie.apples.util.TestUtils;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ProductControllerTest {
    private ProductController productController;
    private ProductService productService = mock(ProductService.class);

    private static List<Product> products;

    @BeforeClass
    public static void init() {
        products = new ArrayList<>();

        Product product = new Product();
        product.setId(1L);
        product.setDescription("This is a test product");
        product.setName("testProduct");
        product.setPrice(BigDecimal.valueOf(2.99));

        products.add(product);
    }

    @Before
    public void setup() {
        productController = new ProductController();
        TestUtils.injectObjects(productController, "productService", productService);

        when(productService.findAll()).thenReturn(products);
        when(productService.find(products.get(0).getId())).thenReturn(products.get(0));
        when(productService.findByName(products.get(0).getName())).thenReturn(products);

        when(productService.find(argThat(aLong -> aLong != 1L))).thenThrow(UnsupportedOperationException.class);
        when(productService.findByName(argThat(name -> name != products.get(0).getName())))
                .thenReturn(null);
    }

    @Test
    public void get_products_happy_path() throws Exception {

        final ResponseEntity<List<Product>> response = productController.getProducts();

        assertNotNull(response);
        assertArrayEquals(products.toArray(), response.getBody().toArray());
    }

    @Test
    public void get_product_happy_path() throws Exception {
        final ResponseEntity<Product> response =
                productController.getProductById(products.get(0).getId());

        assertNotNull(response);
        assertEquals(products.get(0).getId(), response.getBody().getId());
    }

    @Test
    public void get_product_unhappy_path() throws Exception {
        final ResponseEntity<Product> response =
                productController.getProductById(2L);

        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void get_product_by_name_happy_path() throws Exception {
        final ResponseEntity<List<Product>> response =
                productController.getProductsByName(products.get(0).getName());

        assertNotNull(response);
        assertArrayEquals(products.toArray(), response.getBody().toArray());
    }

    @Test
    public void get_product_by_name_unhappy_path() throws Exception {
        final ResponseEntity<List<Product>> response =
                productController.getProductsByName("fakeName");

        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }
}
