package org.hroubles.mir.service;


import org.hroubles.mir.domain.Product;
import org.hroubles.mir.domain.User;
import org.hroubles.mir.domain.enums.Tag;
import org.hroubles.mir.repository.ProductRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource("/application-test.properties")
public class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @MockBean
    private ProductRepository productRepository;

    @Test
    public void addProductWithoutArguments() {
        Product product = new Product();

        assertTrue(productService.addProduct(product));

        Mockito.verify(productRepository, Mockito.times(1)).save(product);
    }

    @Test
    public void addProductWithArguments() throws IOException {
        Product product = new Product();
        User user = new User();
        user.setUsername("JoJo");

        assertTrue(productService.addProduct(
                product,
                user,
                Collections.singleton(Tag.ARTS),
                new MockMultipartFile("file.png", "123123123".getBytes()))
        );

        Mockito.verify(productRepository, Mockito.times(1)).save(product);
    }
}
