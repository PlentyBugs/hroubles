package org.hroubles.mir;

import org.hroubles.mir.controller.ProductController;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.xpath;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/application-test.properties")
@Sql(value = {"/create-user-before.sql", "/products-list-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/products-list-after.sql", "/create-user-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@WithUserDetails("12")
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductController controller;

    @Test
    public void productListTest() throws Exception {
        mockMvc.perform(get("/product"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(xpath("//div[@id='product-list']/a/div").nodeCount(4));
    }

    @Test
    public void specificProductTest() throws Exception {
        mockMvc.perform(get("/product/1"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(xpath("//div[@id='main']/span/div/div/div[2]/h3").string(containsString("My opinion")))
                .andExpect(xpath("//div[@id='main']/span/div/div/div[2]/div/a").string(containsString("admin")))
                .andExpect(xpath("//div[@id='main']/span/div/div/div[3]/div/span").string(containsString("100")))
                .andExpect(xpath("//div[@id='main']/span/div/div/div[2]/div[2]").string(containsString("Useless")))
                .andExpect(xpath("//div[@id='main']/span/div/div/div[1]/img").exists());
    }
}
