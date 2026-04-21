package tests;

import baseTest.BaseTest;
import io.restassured.response.Response;
import models.*;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import retry.RetryAnalyzer;
import services.*;

import java.util.*;

public class ApiChaining extends BaseTest {

    @Test(description = "API Chain: User + Product → Create Cart",retryAnalyzer = RetryAnalyzer.class)
    public void testCreateCartUsingUserAndProductChain(){

        SoftAssert softAssert = new SoftAssert();

        int userId = new UserService()
                .getAllUsers()
                .jsonPath()
                .getInt("[0].id");

        var productJson = new ProductService()
                .getAllProducts()
                .jsonPath();

        int productId = productJson.getInt("[0].id");
        String title = productJson.getString("[0].title");
        double price = productJson.getDouble("[0].price");
        String description = productJson.getString("[0].description");
        String category = productJson.getString("[0].category");
        String image = productJson.getString("[0].image");

        Product product = new Product.Builder()
                .setId(productId)
                .setTitle(title)
                .setPrice(price)
                .setDescription(description)
                .setCategory(category)
                .setImage(image)
                .build();

        Cart cart = new Cart.Builder()
                .setUserId(userId)
                .setProducts(List.of(product))
                .build();

        Response response = new CartService().createCart(cart);

        softAssert.assertEquals(response.statusCode(),201,"Status Code mismatch");

        var json = response.jsonPath();

        softAssert.assertNotNull(json.get("id"), "Cart ID null");
        softAssert.assertEquals(json.getInt("userId"), userId);

        List<Map<String, Object>> products = response.jsonPath().getList("products");
        Map<String,Object> resProduct = products.get(0);

        softAssert.assertEquals(resProduct.get("id"), productId);
        softAssert.assertEquals(resProduct.get("title"), title);
        softAssert.assertEquals(
                Double.parseDouble(resProduct.get("price").toString()),
                price
        );

        softAssert.assertAll();
    }
}
