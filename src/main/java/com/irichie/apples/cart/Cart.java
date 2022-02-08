package com.irichie.apples.cart;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.irichie.apples.product.Product;
import com.irichie.apples.user.User;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty
    private Long id;

    @ManyToMany
    @JsonProperty
    private List<Product> products;

    @OneToOne(mappedBy = "cart")
    @JsonProperty
    private User user;

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    @JsonProperty
    private BigDecimal total;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void addItem(Product product) {
        if(products == null) {
            products = new ArrayList<>();
        }
        products.add(product);
        if(total == null) {
            total = new BigDecimal(0);
        }
        total = total.add(product.getPrice());
    }

    public void removeItem(Product product) {
        if(products == null) {
            products = new ArrayList<>();
        }
        products.remove(product);
        if(total == null) {
            total = new BigDecimal(0);
        }
        total = total.subtract(product.getPrice());
    }
}
