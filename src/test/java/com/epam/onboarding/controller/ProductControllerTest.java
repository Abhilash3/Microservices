package com.epam.onboarding.controller;

import com.epam.onboarding.common.Utils;
import com.epam.onboarding.dao.ProductDAO;
import com.epam.onboarding.domain.Product;
import com.epam.onboarding.service.IProductService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(ProductController.class)
@ActiveProfiles("test")
public class ProductControllerTest {

    private static final String PRODUCT_NAME_EXPRESSION = "$.name";

    @Autowired
    private MockMvc mvc;

    @MockBean
    private ProductDAO productDAO;

    @MockBean
    private IProductService productService;

    @Test
    public void fetchProduct() throws Exception {
        Product product = product("A");

        when(productService.getById(product.getId())).thenReturn(product);

        mvc.perform(get("/products/" + product.getId()).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath(PRODUCT_NAME_EXPRESSION, is(product.getName())));
        verify(productService).getById(product.getId());
    }

    @Test
    public void fetchProducts() throws Exception {
        List<Product> products = Arrays.asList(product("A"), product("B"), product("C"));

        when(productService.getAll()).thenReturn(products);

        mvc.perform(get("/products").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(products.get(0).getId())))
                .andExpect(jsonPath("$[0].name", is(products.get(0).getName())))
                .andExpect(jsonPath("$[1].id", is(products.get(1).getId())))
                .andExpect(jsonPath("$[1].name", is(products.get(1).getName())))
                .andExpect(jsonPath("$[2].id", is(products.get(2).getId())))
                .andExpect(jsonPath("$[2].name", is(products.get(2).getName())));
        verify(productService).getAll();
    }

    @Test
    public void createProduct() throws Exception {
        Product product = product("A");

        when(productService.getByName(product.getName())).thenReturn(null);
        when(productService.save(any(Product.class))).thenReturn(product);

        mvc.perform(
                post("/products")
                        .param("productName", product.getName())
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath(PRODUCT_NAME_EXPRESSION, is(product.getName())));

        verify(productService).getByName(product.getName());
        verify(productService).save(any(Product.class));
    }

    @Test
    public void createDuplicateProduct() throws Exception {
        Product product = product("A");

        when(productService.getByName(product.getName())).thenReturn(product);

        mvc.perform(
                post("/products")
                        .param("productName", product.getName())
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath(PRODUCT_NAME_EXPRESSION, is(product.getName())));

        verify(productService).getByName(product.getName());
        verify(productService, times(0)).save(any(Product.class));
    }

    @Test
    public void updateProduct() throws Exception {
        Product product = product("A");

        when(productService.getById(product.getId())).thenReturn(product);

        mvc.perform(
                put("/products/" + product.getId())
                        .param("productName", "B")
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath(PRODUCT_NAME_EXPRESSION, is("B")));

        verify(productService).getById(product.getId());
        verify(productService).save(any(Product.class));
    }

    @Test
    public void deleteProduct() throws Exception {
        Product product = product("B");

        when(productService.removeById(product.getId())).thenReturn(product);

        mvc.perform(delete("/products/" + product.getId()).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath(PRODUCT_NAME_EXPRESSION, is(product.getName())));

        verify(productService).removeById(product.getId());
    }

    private Product product(String name) {
        return product(Utils.randomLong(), name);
    }

    private Product product(Long id, String name) {
        Product product = new Product().setName(name);
        product.setId(id);
        return product;
    }
}