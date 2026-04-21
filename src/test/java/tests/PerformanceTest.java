package tests;

import baseTest.BaseTest;
import io.restassured.response.Response;
import org.testng.annotations.Test;
import services.CartService;
import services.ProductService;
import services.UserService;

import static org.testng.Assert.assertTrue;

public class PerformanceTest extends BaseTest {

    private static final long MAX_RESPONSE_TIME = 2000;

    @Test(description = "Verify response time of GET /users API")
    public void testUsersApiResponseTime(){

        Response response = new UserService().getAllUsers();
        long time = response.getTime();
        assertTrue(time <MAX_RESPONSE_TIME,"User API slow: " + time + " ms");
    }

    @Test(description = "Verify response time of GET /carts API")
    public void testCartsApiResponseTime(){

        Response response = new CartService().getAllCart();
        long time = response.getTime();
        assertTrue(time<MAX_RESPONSE_TIME ,"Cart API slow: " + time + " ms");
    }

    @Test(description = "Verify response time of GET /products API")
    public void testProductsApiResponseTime(){

        Response response = new ProductService().getAllProducts();
        long time = response.time();
        assertTrue(time<MAX_RESPONSE_TIME, "Product API slow: " + time + " ms");
    }
}
