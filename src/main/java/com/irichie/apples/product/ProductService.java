package com.irichie.apples.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    public Product create(Product product) {
        return productRepository.save(product);
    }

    public List<Product> findAll() {
        return (List<Product>) productRepository.findAll();
    }

    public Product find(Long id) {
        return productRepository.findById(id)
                .orElseThrow(UnsupportedOperationException::new);
    }

    public List<Product> findByName(String name) {
        return productRepository.findByName(name);
    }
}
