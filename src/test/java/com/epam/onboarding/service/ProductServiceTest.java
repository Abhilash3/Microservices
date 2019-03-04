package com.epam.onboarding.service;

import com.epam.onboarding.dao.ProductDAO;
import com.epam.onboarding.domain.Product;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @MockBean
    private ProductDAO productDAO;

    @Test
    public void saveProduct() {
        when(productDAO.findAll()).thenReturn(Collections.emptyList());

        Product productA = product("A");
        productService.save(productA);

        verify(productDAO).save(productA);
    }

    @Test
    public void findAllProducts() {
        List<Product> products = Arrays.asList(product("A"), product("B"), product("C"));
        when(productDAO.findAll()).thenReturn(products);

        assertEquals(products, productService.getAll());
        verify(productDAO).findAll();
    }

    @Test
    public void findProductByName() {
        List<Product> products = Arrays.asList(product("A"), product("B"), product("C"));
        when(productDAO.findAll()).thenReturn(products);

        assertEquals(products.get(0), productService.getByName("A"));
        assertEquals(products.get(1), productService.getByName("B"));
        assertEquals(products.get(2), productService.getByName("C"));

        verify(productDAO, times(3)).findAll();
    }

    @Test
    public void findProductById() {
        List<Product> products = Arrays.asList(product(10L, "A"), product(20L, "B"), product(30L, "C"));
        products.forEach(product -> when(productDAO.findOne(product.getId())).thenReturn(product));

        assertEquals(product(10L, "A"), productService.getById(10L));
        assertEquals(product(20L, "B"), productService.getById(20L));

        verify(productDAO, times(2)).findOne(anyLong());
    }

    @Test
    public void removeProduct() {
        Product product = product("ABC");
        assertNotNull(product.getId());

        productService.remove(product);

        verify(productDAO).delete(product);
        assertNull(product.getId());
    }

    @Test
    public void removeProductById() {
        Product product = product(10L, "ABC");

        when(productDAO.findOne(product.getId())).thenReturn(product);

        productService.removeById(10L);

        verify(productDAO).findOne(10L);
        verify(productDAO).delete(10L);
    }

    private Product product(String name) {
        return product(100L, name);
    }

    private Product product(Long id, String name) {
        return new Product().setName(name).setId(id);
    }
}