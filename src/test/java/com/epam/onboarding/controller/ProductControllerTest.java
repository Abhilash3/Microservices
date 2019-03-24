package com.epam.onboarding.controller;

import com.epam.onboarding.dao.ProductDAO;
import com.epam.onboarding.domain.Product;
import com.epam.onboarding.domain.Review;
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

    @MockBean
    private ReviewController reviewController;

    @Test
    public void fetchProduct() throws Exception {
        Product product = product("A");

        when(productService.getById(product.getProductId())).thenReturn(product);

        mvc.perform(get("/products/" + product.getProductId()).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath(PRODUCT_NAME_EXPRESSION, is(product.getName())));
        verify(productService).getById(product.getProductId());
    }

    @Test
    public void fetchProducts() throws Exception {
        List<Product> products = Arrays.asList(product("A"), product("B"), product("C"));

        when(productService.getAll()).thenReturn(products);

        mvc.perform(get("/products").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name", is(products.get(0).getName())))
                .andExpect(jsonPath("$[1].name", is(products.get(1).getName())))
                .andExpect(jsonPath("$[2].name", is(products.get(2).getName())));
        verify(productService).getAll();
    }

    @Test
    public void createProduct() throws Exception {
        Product product = product(null, "A");

        when(productService.getByName(product.getName())).thenReturn(null);
        when(productService.save(any(Product.class))).thenReturn(product);

        mvc.perform(
                post("/products")
                        .param("name", product.getName())
                        .param("description", product.getDescription())
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath(PRODUCT_NAME_EXPRESSION, is(product.getName())));

        verify(productService).getByName(product.getName());
        verify(productService).save(eq(product));
    }

    @Test
    public void createDuplicateProduct() throws Exception {
        Product product = product("A");

        when(productService.getByName(product.getName())).thenReturn(product);

        mvc.perform(
                post("/products")
                        .param("name", product.getName())
                        .param("description", product.getDescription())
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

        when(productService.getById(product.getProductId())).thenReturn(product);

        mvc.perform(
                put("/products/" + product.getProductId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"property\": \"name\", \"value\": \"B\"}")
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath(PRODUCT_NAME_EXPRESSION, is("B")));

        verify(productService).getById(product.getProductId());
        verify(productService).save(eq(product));
    }

    @Test
    public void deleteProduct() throws Exception {
        Product product = product("B");

        when(productService.removeById(product.getProductId())).thenReturn(product);

        mvc.perform(delete("/products/" + product.getProductId()).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath(PRODUCT_NAME_EXPRESSION, is(product.getName())));

        verify(productService).removeById(product.getProductId());
    }

    @Test
    public void createReview() throws Exception {
        Product product = product("A");
        Review review = review(product.getProductId(), "bad", 2);

        when(reviewController.create(anyLong(), anyString(), anyInt())).thenReturn(review);

        mvc.perform(
                post("/products/" + product.getProductId() + "/reviews")
                        .param("description", review.getDescription())
                        .param("rating", String.valueOf(review.getRating()))
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description", is(review.getDescription())))
                .andExpect(jsonPath("$.rating", is(review.getRating())));

        verify(reviewController).create(product.getProductId(), review.getDescription(), review.getRating());
    }

    @Test
    public void updateReview() throws Exception {
        Product product = product("A");
        Review review = review(product.getProductId(), "bad", 1);

        when(reviewController.update(anyLong(), anyLong(), any(UpdateRequest.class))).thenReturn(review);

        mvc.perform(
                put(String.format("/products/%d/reviews/%d", product.getProductId(), review.getReviewId()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"property\": \"rating\", \"value\": \"1\"}")
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description", is(review.getDescription())))
                .andExpect(jsonPath("$.rating", is(review.getRating())));

        UpdateRequest request = new UpdateRequest().setProperty("rating").setValue("1");
        verify(reviewController).update(eq(product.getProductId()), eq(review.getReviewId()), eq(request));
    }

    @Test
    public void deleteReview() throws Exception {
        Product product = product("A");
        Review review = review(product.getProductId(), "good", 4);

        when(reviewController.delete(anyLong(), anyLong())).thenReturn(review);

        mvc.perform(
                delete(String.format("/products/%d/reviews/%d", product.getProductId(), review.getReviewId()))
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description", is(review.getDescription())))
                .andExpect(jsonPath("$.rating", is(review.getRating())));

        verify(reviewController).delete(product.getProductId(), review.getReviewId());
    }

    private Product product(String name) {
        return product(100L, name);
    }

    private Product product(Long productId, String name) {
        return new Product().setName(name).setDescription(name).setProductId(productId);
    }

    private Review review(Long productId, String description, Integer rating) {
        return new Review().setProductId(productId).setDescription(description).setRating(rating).setReviewId(200L);
    }
}